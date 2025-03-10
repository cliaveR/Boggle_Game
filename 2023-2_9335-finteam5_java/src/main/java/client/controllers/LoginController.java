package client.controllers;

import BoggleApp.Player;
import BoggleApp.PlayerServant;
import client.utility.InterfaceLoader;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController {
    static PlayerServant playerServant;
    public PasswordField UserPassword;
    public TextField Username;
    public Button LoginButton;
    public Label invalidPassword;
    public Label invalidUsername;
    public static Text usernameLabel;
    static Player player = new Player();
    private static String data;

    @FXML
    void loginButtonAction() {

        String enteredUsername = Username.getText();
        String enteredPassword = UserPassword.getText();


        invalidUsername.setText("");
        invalidPassword.setText("");

        if (enteredUsername.isEmpty() && enteredPassword.isEmpty()) {
            invalidUsername.setText("No username entered");
            invalidPassword.setText("No password entered");
        } else if (enteredUsername.isEmpty()) {
            invalidUsername.setText("No username entered");
        } else if (enteredPassword.isEmpty()) {
            invalidPassword.setText("No password entered");
        }
        try {
            int playerID = playerServant.logInToGame(enteredUsername, enteredPassword);
            System.out.println(playerID);
            if (playerID > 0) {
                Stage stage = (Stage) LoginButton.getScene().getWindow();
                InterfaceLoader.loadInterface("/Lobby.fxml", stage, controller -> ((LobbyController)controller).initialize(playerServant, playerID));
            } else {
                invalidUsername.setText("Wrong Username or Password");
            }
        } catch (Exception e) {
            // Handle exceptions appropriately
            e.printStackTrace(); // For example, you can log the exception
        }

    }
    public void setStub(PlayerServant stub){
        playerServant = stub;
    }

}
