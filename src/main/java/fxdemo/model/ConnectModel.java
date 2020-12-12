package fxdemo.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.kafka.clients.admin.Admin;

import java.util.StringJoiner;

public class ConnectModel {
    public static class Pair<K, V> {
        private K key;
        private V value;

        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public void setKey(K key) {
            this.key = key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", Pair.class.getSimpleName() + "[", "]")
                    .add("key=" + key)
                    .add("value=" + value)
                    .toString();
        }
    }



    final ObservableList<String> parameters =
            FXCollections.unmodifiableObservableList(
                    FXCollections.observableArrayList(
                            "key",
                            "bootstrap.servers",
                            "security.protocol")
            );

    final ObservableList<Pair<String, String>> tablerows = FXCollections.observableArrayList();

    Admin admin;

    public ObservableList<String> getParameters() {
        return parameters;
    }

    public ObservableList<Pair<String, String>> getTablerows() {
        return tablerows;
    }
}
