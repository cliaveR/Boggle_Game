package client.controllers;

import BoggleApp.PlayerServant;
import javafx.application.Platform;
import javafx.scene.control.Label;

public class WaitingScreen {
    public Label time;
    public void setWaitingTime(int wTime) {
        time.setText(String.valueOf(wTime));
    }
}
