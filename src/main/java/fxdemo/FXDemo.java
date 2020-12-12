package fxdemo;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class FXDemo extends Application  {


    @Override
    public void start(Stage primaryStage) throws Exception {

        URL location = getClass().getResource("fxdemo.fxml");
        ResourceBundle resources = ResourceBundle.getBundle("i18n.labels");
        FXMLLoader fxmlLoader = new FXMLLoader(location, resources);

        Pane root = (Pane)fxmlLoader.load();

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

        primaryStage.setTitle("JavaFX DEMO");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    @Override
    public void stop() throws Exception {
        Platform.exit();
    }

    public static void main(String[] args) {
        Application myApp = new FXDemo();
        myApp.launch(args);
    }
}
