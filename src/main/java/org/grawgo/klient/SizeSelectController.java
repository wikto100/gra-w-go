package org.grawgo.klient;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;

public class SizeSelectController {
    final ToggleGroup sizeToggleGroup = new ToggleGroup();
    @FXML
    public RadioButton size19button;
    @FXML
    public RadioButton size13button;
    @FXML
    public RadioButton size9button;
    @FXML
    public Button confirmSizeButton;

    @FXML
    public void initialize() {
        size9button.setToggleGroup(sizeToggleGroup);
        size13button.setToggleGroup(sizeToggleGroup);
        size19button.setToggleGroup(sizeToggleGroup);
    }
    public int getSizeFromRadio(){
        if(sizeToggleGroup.getSelectedToggle().equals(size19button)){
            return 19;
        }
        else if (sizeToggleGroup.getSelectedToggle().equals(size13button)){
            return 13;
        } else if (sizeToggleGroup.getSelectedToggle().equals(size9button)) {
            return 9;
        }else {
            return 0;
        }
    }
    public boolean wasSelected() {
        return sizeToggleGroup.getSelectedToggle() != null;
    }
}
