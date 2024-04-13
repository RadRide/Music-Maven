module main.spotifydownloader {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.win10;
    requires MaterialFX;
    requires com.google.gson;

    opens main to javafx.fxml;
    exports main;
    exports files;
}