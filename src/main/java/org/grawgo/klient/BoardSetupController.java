package org.grawgo.klient;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;

public class BoardSetupController {
    public Button exitButton;
    public Button skipButton;
    int boardSize;

    public BoardSetupController() {
        this.boardSize = 0;
    }

    public void setBoardSize(int boardSize) {
        this.boardSize = boardSize;
        initialize();
    }

    @FXML
    private GridPane board;

    @FXML
    public void initialize() {
        board.getColumnConstraints().clear();
        board.getRowConstraints().clear();
        for (int i = 1; i <= boardSize; i++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setMinWidth(10.0);
            column.setPrefWidth(30.0);
            RowConstraints row = new RowConstraints();

            row.setMinHeight(10.0);
            row.setPrefHeight(30.0);

            if (i == boardSize) {
                column.setPrefWidth(0);
                column.setMinWidth(0);
                column.setMaxWidth(0);
                column.setFillWidth(false);
                column.setHgrow(Priority.NEVER);
                row.setPrefHeight(0);
                row.setMinHeight(0);
                row.setMaxHeight(0);
                row.setFillHeight(false);
                row.setVgrow(Priority.NEVER);
            }
            board.getColumnConstraints().add(column);
            board.getRowConstraints().add(row);
        }
        CircleButton stoneHolder;
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                stoneHolder = new CircleButton(5,i,j);
                if(stoneHolder.myY == boardSize - 1){
                    stoneHolder.selfCorrect();
                }
                board.add(stoneHolder, i, j);
            }
        }

    }
}
