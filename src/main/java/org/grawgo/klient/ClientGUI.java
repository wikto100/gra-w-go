package org.grawgo.klient;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ClientGUI extends Application {
    Parent root;
    FXMLLoader loader;

    @Override
    public void start(Stage primaryStage) throws IOException {
        GoClient goClient = new GoClient();
        Platform.runLater(goClient);
        changeScene("/size_select.fxml");


        // Zrob stage (FXML)
        Scene scene = new Scene(root, 600, 400);
        primaryStage.setTitle("Go Client");
        primaryStage.setScene(scene);
        primaryStage.show();

        SizeSelectController sizeSelectController = loader.getController();
        sizeSelectController.confirmSizeButton.setOnAction(actionEvent -> {
            try {
                if (sizeSelectController.wasSelected()) {
                    changeScene("/board_gui.fxml");
                    BoardSetupController boardSetupController = loader.getController();
                    boardSetupController.setBoardSize(sizeSelectController.getSizeFromRadio());
                    Scene scene1 = new Scene(root, 800, 800);
                    primaryStage.setScene(scene1);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });
    }

    public void changeScene(String fxml) throws IOException {
        loader = new FXMLLoader(getClass().getResource(fxml));
        root = loader.load();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
