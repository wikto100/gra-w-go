package org.grawgo.klient;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ClientGUI extends Application {
    Parent root;
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/client_gui.fxml"));
    @Override
    public void start(Stage primaryStage) throws IOException {
        root = loader.load();
        // Set up the stage
        Scene scene = new Scene(root, 300, 200);
        primaryStage.setTitle("Hello, World!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
