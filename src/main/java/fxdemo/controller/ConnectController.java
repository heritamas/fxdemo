package fxdemo.controller;

import fxdemo.model.AppModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ChoiceBoxTableCell;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ConnectController {


    Logger log = Logger.getLogger("fx");

    private AppModel model = new AppModel();

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private DialogPane pane;

    @FXML
    private TableView<AppModel.Pair<String, String>> parameterTable;

    @FXML
    private TableColumn<AppModel.Pair<String, String>, String> parameterColumn;

    @FXML
    private TableColumn<AppModel.Pair<String, String>, String> valueColumn;

    @FXML
    void initialize() {
        assert pane != null : "fx:id=\"pane\" was not injected: check your FXML file 'connect.fxml'.";
        assert parameterTable != null : "fx:id=\"parameterTable\" was not injected: check your FXML file 'connect.fxml'.";
        assert parameterColumn != null : "fx:id=\"parameterColumn\" was not injected: check your FXML file 'connect.fxml'.";
        assert valueColumn != null : "fx:id=\"valueColumn\" was not injected: check your FXML file 'connect.fxml'.";

        pane.lookupButton(ButtonType.NEXT).addEventFilter(ActionEvent.ACTION, event -> {
            model.getTablerows().add(0, new AppModel.Pair<String, String>("key", "value"));
            parameterTable.refresh();
            event.consume();
        });

        parameterColumn.setCellFactory(ComboBoxTableCell.forTableColumn(model.getParameters()));
        parameterColumn.setOnEditCommit(event -> {
            AppModel.Pair<String, String> entry = event.getTableView().getItems().get(event.getTablePosition().getRow());
            log.info("old: " + event.getOldValue() + " new: " + event.getNewValue());
            entry.setKey(event.getNewValue());
        });

        valueColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        valueColumn.setOnEditCommit(event -> {
            AppModel.Pair<String, String> entry = event.getTableView().getItems().get(event.getTablePosition().getRow());
            log.info("old: " + event.getOldValue() + " new: " + event.getNewValue());
            entry.setValue(event.getNewValue());
        });

        parameterTable.setItems(model.getTablerows());
    }

    public Map<String, String> getTableMap() {
        model.getTablerows()
                .stream()
                .forEach(entr -> log.info("entry: " + entr));

        return model.getTablerows().stream().collect(Collectors.toMap(entr -> entr.getKey(), entr -> entr.getValue()));
    }
}
