package fxdemo.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ConnectModel {


    private final ObservableList<String> parameters =
            FXCollections.unmodifiableObservableList(
                    FXCollections.observableArrayList(
                            "key",
                            "client.id",
                            "bootstrap.servers",
                            "ssl.keystore.type",
                            "ssl.key.password",
                            "ssl.keystore.location",
                            "ssl.keystore.password",
                            "ssl.truststore.location",
                            "ssl.truststore.password",
                            "sasl.jaas.config",
                            "sasl.mechanism",
                            "security.protocol")
            );

    private final ObservableList<Pair<String, String>> tablerows = FXCollections.observableArrayList();

    public ObservableList<String> getParameters() {
        return parameters;
    }

    public ObservableList<Pair<String, String>> getTablerows() {
        return tablerows;
    }
}
