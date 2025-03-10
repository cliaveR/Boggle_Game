package client.controllers;

import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class TextTile {
    @FXML
    private Text randomLetter;

    public void setText(String letter) {
        randomLetter.setText(letter);
    }
}
