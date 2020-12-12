package fxdemo.controller;


import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.ResourceBundle;
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

public class AppController {

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

        System.out.println("about to show dialog");

        connectDialog
                .showAndWait()
                .filter(response -> response.size() != 0)
                .ifPresent(response -> openCluster(response));

    }

    private void openCluster(Map<String, String> response) {
        System.out.println(response);
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
