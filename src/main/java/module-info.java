module fxdemo {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    opens fxdemo to javafx.fxml, javafx.graphics;

    exports fxdemo to javafx.fxml;
    exports fxdemo.model to javafx.fxml;
    exports fxdemo.controller to javafx.fxml;
}