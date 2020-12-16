package fxdemo;

import fxdemo.model.Stoppable;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ResourceBundle;
import java.util.logging.LogManager;
import java.util.logging.Logger;


public class FXDemo extends Application  {

    static {
        final InputStream inputStream = FXDemo.class.getResourceAsStream("/logging.properties");
        try
        {
            LogManager.getLogManager().readConfiguration(inputStream);
        }
        catch (final IOException e)
        {
            Logger.getAnonymousLogger().severe("Could not load default logging.properties file");
            Logger.getAnonymousLogger().severe(e.getMessage());
        }

    }

    Logger log = Logger.getLogger("fx");
    Collection<Stoppable> controllers = new ArrayList<>();

    @Override
    public void start(Stage primaryStage) throws IOException {
        URL location = getClass().getResource("fxdemo.fxml");
        ResourceBundle resources = ResourceBundle.getBundle("i18n.labels");
        FXMLLoader fxmlLoader = new FXMLLoader(location, resources);

        Pane root = (Pane)fxmlLoader.load();
        controllers.add(fxmlLoader.getController());

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm());

        primaryStage.setTitle("JavaFX DEMO");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        controllers.forEach(Stoppable::stop);
        Platform.exit();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
