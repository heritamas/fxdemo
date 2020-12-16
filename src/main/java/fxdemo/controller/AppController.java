package fxdemo.controller;


import fxdemo.model.AppModel;
import fxdemo.model.Pair;
import fxdemo.model.Stoppable;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.util.StringConverter;
import org.apache.kafka.clients.admin.DescribeTopicsResult;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.TopicDescription;
import org.apache.kafka.clients.admin.TopicListing;
import org.apache.kafka.common.Node;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.StringJoiner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;
import java.util.stream.Collectors;


public class AppController implements Stoppable {

    private AppModel model = new AppModel();
    private Logger log = Logger.getLogger("fx");
    private ExecutorService service = Executors.newFixedThreadPool(10);

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private MenuItem connectMenu;

    @FXML
    private ListView<TopicListing> topicList;

    @FXML
    private TableView<Pair<String, String>> topicTable;

    @FXML
    private TableColumn<Pair<String, String>, String> topicKeys;

    @FXML
    private TableColumn<Pair<String, String>, String> topicValues;

    @FXML
    private Label messageLabel;


    private ReadOnlyObjectProperty<TopicListing> selectedTopic;


    @FXML
    void connectDialog(ActionEvent event) throws IOException {
        URL location = getClass().getResource("connect.fxml");
        FXMLLoader loader = new FXMLLoader(location, resources);
        DialogPane root = (DialogPane) loader.load();
        ConnectController ctrl = loader.<ConnectController>getController();

        Dialog<Map<String, String>> connectDialog = new Dialog<>();
        connectDialog.setDialogPane(root);
        connectDialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.APPLY) {
                return ctrl.getTableMap();
            }
            return Collections.emptyMap();
        });

        connectDialog
                .showAndWait()
                .filter(response -> response.size() != 0)
                .ifPresent(response -> {
                    model.setClusterProps(response);
                });

    }

    @FXML
    void openCluster(ActionEvent event)  {

        Task<Void> readCluster = new Task<Void>() {

            @Override
            protected Void call() throws Exception {

                ListTopicsResult ltr = model.getAdminClient().listTopics();
                updateMessage(resources.getString("msg.progress"));

                ltr.namesToListings().whenComplete((res, exc) -> {
                    model.getTopicListing().clear();
                   if (res != null) {
                       updateMessage(resources.getString("msg.done"));
                       updateTopicListing(res);
                   } else if (exc != null) {
                       updateMessage(resources.getString("msg.error"));
                       showAlertDialog(exc);
                   }
                }).get();
                return null;
            }

        };

        messageLabel.textProperty().bind(readCluster.messageProperty());
        service.submit(readCluster);
    }


    @FXML
    void initialize() {
        assert connectMenu != null : "fx:id=\"connectMenu\" was not injected: check your FXML file 'fxdemo.fxml'.";
        assert topicList != null : "fx:id=\"topicList\" was not injected: check your FXML file 'fxdemo.fxml'.";
        assert topicTable != null : "fx:id=\"topicTable\" was not injected: check your FXML file 'fxdemo.fxml'.";
        assert topicKeys != null : "fx:id=\"topicKeys\" was not injected: check your FXML file 'fxdemo.fxml'.";
        assert topicValues != null : "fx:id=\"topicValues\" was not injected: check your FXML file 'fxdemo.fxml'.";

        topicList.setItems(model.getSortedTopicListing());
        topicList.setCellFactory(TextFieldListCell.<TopicListing>forListView(new StringConverter<TopicListing>() {
            @Override
            public String toString(TopicListing object) {
                return String.format("%s%s", object.name(), object.isInternal() ? " (intr)" : "");
            }

            @Override
            public TopicListing fromString(String string) {
                // should not be used
                return new TopicListing(string, false);
            }
        }));

        selectedTopic = topicList.getSelectionModel().selectedItemProperty();
        selectedTopic.addListener((observable, oldValue, newValue) -> {
            updateTopicTable(newValue);
        });

        topicKeys.setCellValueFactory(new PropertyValueFactory<Pair<String, String>, String>("key"));
        topicValues.setCellValueFactory(new PropertyValueFactory<Pair<String, String>, String>("value"));
        topicTable.setItems(model.getTablerows());

    }


    private void updateTopicListing(Map<String, TopicListing> topicListing) {
        topicListing.entrySet()
                .forEach(ent -> model.getTopicListing().add(ent.getValue()));
    }

    private void updateTopicTable(TopicListing topic) {
        Task<Void> readTopic = new Task<Void>() {

            @Override
            protected Void call() throws Exception {
                DescribeTopicsResult descriptonResult = model.getAdminClient().describeTopics(Collections.singleton(topic.name()));
                updateMessage(resources.getString("msg.progress"));

                descriptonResult.all().whenComplete((stringTopicDescriptionMap, throwable) -> {
                    model.getTablerows().clear();
                    updateMessage(resources.getString("msg.done"));
                    if (stringTopicDescriptionMap != null) {
                        TopicDescription topicDescripton = stringTopicDescriptionMap.get(topic.name());
                        updateTopicTableWithDescription(topicDescripton);
                    } else if (throwable != null) {
                        updateMessage(resources.getString("msg.error"));
                        showAlertDialog(throwable);
                    }
                });

                return null;
            }
        };

        messageLabel.textProperty().bind(readTopic.messageProperty());
        service.submit(readTopic);
    }

    private void updateTopicTableWithDescription(TopicDescription topicDescripton) {

        model.getTablerows().addAll(topicDescripton.partitions().stream()
                .map(tpi -> {
                    StringJoiner value = new StringJoiner(";");
                    value.add(String.format("leader: %d", tpi.leader().id()));
                    value.add("replicas");
                    tpi.replicas().stream()
                            .map(Node::id)
                            .map(String::valueOf)
                            .forEach(value::add);
                    value.add("isr");

                    tpi.isr().stream()
                            .map(Node::id)
                            .map(String::valueOf)
                            .forEach(value::add);

                    Pair<String, String> info = new Pair<>(
                            String.format("partition %d", tpi.partition()),
                            value.toString());
                    return info;
                })
                .collect(Collectors.toList()));

        model.getTablerows().addAll(topicDescripton.authorizedOperations().stream()
                .map(acl -> {
                    Pair<String, String> info = new Pair<>(
                            acl.name(),
                            acl.toString());
                    return info;
                })
                .collect(Collectors.toList())
        );
    }

    private void showAlertDialog(Throwable exc) {
        Alert alert = new Alert(Alert.AlertType.ERROR, exc.getMessage());
        alert.showAndWait();
    }

    @Override
    public void stop() {
        service.shutdown();
    }
}
