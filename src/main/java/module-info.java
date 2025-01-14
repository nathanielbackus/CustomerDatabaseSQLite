module backus.firstscreen {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.base;
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