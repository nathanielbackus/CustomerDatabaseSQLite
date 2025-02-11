module C868 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base;
    requires javafx.graphics;
    requires java.sql;
    requires java.desktop;

    opens controller to javafx.fxml;
    opens dao to javafx.base;
    opens helper to javafx.base;
    opens model to javafx.base;

    exports controller;
    exports dao;
    exports helper;
    exports model;
}
