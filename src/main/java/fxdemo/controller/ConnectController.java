package fxdemo.controller;

import fxdemo.model.ConnectModel;
import fxdemo.model.Pair;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ConnectController {


    Logger log = Logger.getLogger("fx");

    ConnectModel model = new ConnectModel();

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private DialogPane pane;

    @FXML
    private TableView<Pair<String, String>> parameterTable;

    @FXML
    private TableColumn<Pair<String, String>, String> parameterColumn;

    @FXML
    private TableColumn<Pair<String, String>, String> valueColumn;

    @FXML
    void initialize() {
        assert pane != null : "fx:id=\"pane\" was not injected: check your FXML file 'connect.fxml'.";
        assert parameterTable != null : "fx:id=\"parameterTable\" was not injected: check your FXML file 'connect.fxml'.";
        assert parameterColumn != null : "fx:id=\"parameterColumn\" was not injected: check your FXML file 'connect.fxml'.";
        assert valueColumn != null : "fx:id=\"valueColumn\" was not injected: check your FXML file 'connect.fxml'.";

        pane.lookupButton(ButtonType.NEXT).addEventFilter(ActionEvent.ACTION, event -> {
            model.getTablerows().add(new Pair<String, String>("key", "value"));
            event.consume();
        });

        parameterColumn.setCellFactory(ComboBoxTableCell.forTableColumn(model.getParameters()));
        parameterColumn.setCellValueFactory( new PropertyValueFactory<Pair<String, String>, String>("key"));
        parameterColumn.setOnEditCommit(event -> {
            Pair<String, String> pair = event.getTableView().getItems().get(event.getTablePosition().getRow());
            pair.setKey(event.getNewValue());
        });

        valueColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        valueColumn.setCellValueFactory( cdf -> {
            return new SimpleStringProperty(cdf.getValue().getValue());
        });
        valueColumn.setOnEditCommit(event -> {
            Pair<String, String> entry = event.getTableView().getItems().get(event.getTablePosition().getRow());
            entry.setValue(event.getNewValue());
        });

        parameterTable.setItems(model.getTablerows());
    }

    public Map<String, String> getTableMap() {

        model.getTablerows()
                .stream()
                .map(Pair::toString)
                .forEach(log::info);

        return model.getTablerows().stream().collect(
                Collectors.toMap(Pair::getKey, Pair::getValue, (acc, add) -> add)
        );
    }
}
