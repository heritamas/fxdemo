package fxdemo.controller;


import fxdemo.model.AppModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.apache.kafka.clients.admin.Admin;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.ListTopicsResult;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;


public class AppController {

    private AppModel model = new AppModel();

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private MenuItem connectMenu;

    @FXML
    private ListView<?> topicList;

    @FXML
    private TableView<?> topicTable;

    @FXML
    private TableColumn<?, ?> topicKeys;

    @FXML
    private TableColumn<?, ?> topicValues;

    @FXML
    void connectDialog(ActionEvent event) throws IOException {
        URL location = getClass().getResource("connect.fxml");
        FXMLLoader loader = new FXMLLoader(location, resources);
        DialogPane root = (DialogPane)loader.load();
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
                .ifPresent(response -> model.setClusterProps(response));

    }

    private void openCluster(Map<String, String> response) {
        Properties props = new Properties();
        props.putAll(model.getClusterProps());
        Admin admin = AdminClient.create(props);

        ListTopicsResult lsres = admin.listTopics();
    }

    @FXML
    void initialize() {
        assert connectMenu != null : "fx:id=\"connectMenu\" was not injected: check your FXML file 'fxdemo.fxml'.";
        assert topicList != null : "fx:id=\"topicList\" was not injected: check your FXML file 'fxdemo.fxml'.";
        assert topicTable != null : "fx:id=\"topicTable\" was not injected: check your FXML file 'fxdemo.fxml'.";
        assert topicKeys != null : "fx:id=\"topicKeys\" was not injected: check your FXML file 'fxdemo.fxml'.";
        assert topicValues != null : "fx:id=\"topicValues\" was not injected: check your FXML file 'fxdemo.fxml'.";

    }
}
