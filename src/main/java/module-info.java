module fxdemo {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.logging;
    requires kafka.clients;

    opens fxdemo to javafx.fxml, javafx.graphics;
    opens fxdemo.controller to javafx.fxml;
    opens fxdemo.model to javafx.base;

    exports fxdemo to javafx.fxml;
    exports fxdemo.model to javafx.fxml;
    exports fxdemo.controller to javafx.fxml;
}