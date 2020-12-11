package fxdemo;

import javafx.application.Application;
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
        //AppController controller = (AppController)fxmlLoader.getController();

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

        primaryStage.setTitle("JavaFX DEMO");
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
