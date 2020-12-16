package fxdemo.model;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import org.apache.kafka.clients.admin.Admin;
import org.apache.kafka.clients.admin.TopicListing;

import java.util.Map;
import java.util.Optional;
import java.util.Properties;

public class AppModel {

    private Map<String, String> clusterProps = null;
    private Optional<Admin> adminClient = Optional.empty();

    private final ObservableList<TopicListing> topicListing = FXCollections.observableArrayList();


    private final ObservableList<TopicListing> sortedTopicListing = new SortedList<TopicListing>(topicListing);

    private final ObservableList<Pair<String, String>> tablerows = FXCollections.observableArrayList();


    public void setClusterProps(Map<String, String> clusterProps) {
        this.clusterProps = clusterProps;

        Properties cp = new Properties();
        cp.putAll(clusterProps);
        adminClient = Optional.of(Admin.create(cp));
    }

    public Admin getAdminClient() {
        return adminClient.orElseThrow();
    }

    public ObservableList<TopicListing> getTopicListing() {
        return topicListing;
    }
    public ObservableList<TopicListing> getSortedTopicListing() {
        return sortedTopicListing;
    }

    public ObservableList<Pair<String, String>> getTablerows() {
        return tablerows;
    }

}
