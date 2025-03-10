package server.controllers;

import databaseClass.PlayerQueries;
import databaseClass.SettingsQueries;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import universalModels.Player;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AdminController {
    public AnchorPane AdminAnchorPane;
    public Pane AdminPane;
    public Button deletingButton;
    public Label editUsernameError;
    public Label editPasswordError;
    public Button refreshButton;
    private Player player;
    @FXML
    public TextField roundTimer;


    @FXML
    public TextField waitingTimer;

    @FXML
    public Button applySettingsButton;

    @FXML
    public TextField newPlayerUsername;

    @FXML
    public TextField newPlayerPassword;

    @FXML
    public Button createPlayerButton;

    @FXML
    public TextField searchBar;

    @FXML
    public Button searchButton;


    @FXML
    public ListView<String> playerList;
    public Label roundTimerError;
    public Label usernameError;
    public Label passwordError;
    public Label searchPlayerError;
    public Label successfulSettingsChange;
    public Label successfulPlayerCreation;
    public Label waitingTimerError;
    public TextField searchedPoints;
    public TextField searchedRound;
    public TextField searchedPassword;
    public TextField searchedUsername;
    public Button saveButton;
    public Button editButton;


    @FXML
    public void initialize() {
        searchedUsername.setEditable(false);
        searchedPassword.setEditable(false);
        deletingButton.setVisible(false);
        searchedRound.setEditable(false);
        searchedPoints.setEditable(false);
        saveButton.setVisible(false);
        editButton.setVisible(false);

    }

    @FXML
    /**
     * This is a button action to enable edit mode.
     * */
    public void editPlayerInformation() {
        // Enable editing of username and password fields
        searchedUsername.setEditable(true);
        searchedPassword.setEditable(true);
        saveButton.setVisible(true);
        deletingButton.setVisible(true);
        searchBar.setEditable(false);
    }

    @FXML
    public void refreshContent() {
        // Clear text fields
        newPlayerUsername.setText("");
        newPlayerPassword.setText("");
        searchBar.setText("");
        searchedUsername.setText("");
        searchedPassword.setText("");
        searchedPoints.setText("");
        searchedRound.setText("");

        // Clear error labels
        usernameError.setText("");
        passwordError.setText("");
        searchPlayerError.setText("");
        roundTimerError.setText("");
        waitingTimerError.setText("");
        editUsernameError.setText("");
        editPasswordError.setText("");
        successfulSettingsChange.setText("");
        successfulPlayerCreation.setText("");

        // Hide buttons
        saveButton.setVisible(false);
        editButton.setVisible(false);
        deletingButton.setVisible(false);

        // Reset editable properties
        searchedUsername.setEditable(false);
        searchedPassword.setEditable(false);
        searchBar.setEditable(true);
    }


    @FXML
    public void deletePlayer() {
        // Import logic to delete player
        PlayerQueries.deletePlayer(searchBar.getText());
        searchPlayerError.setText("Player successfully deleted.");
        searchedUsername.setText("");
        searchedPassword.setText("");
        searchedPoints.setText("");
        searchedRound.setText("");
        searchBar.setText("");
        searchBar.setEditable(true);
        editButton.setVisible(false);
        saveButton.setVisible(false);
        deletingButton.setVisible(false);

        //Add logic that resizes the panel again to version that does not show.
    }


    @FXML
    public void savePlayerInformation() {
        // Get the modified username and password
        String newUsername = searchedUsername.getText();
        String newPassword = searchedPassword.getText();

        // Check if the fields are empty
        if (newUsername.isEmpty() && newPassword.isEmpty()) {
            editUsernameError.setText("Enter a New Username");
            editPasswordError.setText("Enter a New Password");
        } else if (newUsername.isEmpty()) {
            editUsernameError.setText("Enter a New Username");
            editPasswordError.setText(newPassword.isEmpty() ? "Enter a New Password" : "");
        } else if (newPassword.isEmpty()) {
            editPasswordError.setText("Enter a New Password");
            editUsernameError.setText("");
        } else {

            PlayerQueries.editUsername(player.getUsername(), newUsername);
            PlayerQueries.editPassword(newUsername, newPassword);


            // Hide save and delete buttons after saving
            saveButton.setVisible(false);
            deletingButton.setVisible(false);
            searchedUsername.setEditable(false);
            searchedPassword.setEditable(false);
            searchPlayerError.setText("Player information saved successfully.");
            searchPlayerError.setStyle("-fx-text-fill: white;");


            // Clear error messages
            editUsernameError.setText("");
            editPasswordError.setText("");
        }
    }


    public void createPlayerButton() throws SQLException {
        String username = newPlayerUsername.getText();
        String password = newPlayerPassword.getText();
        successfulPlayerCreation.setText("");
        if (username.isEmpty() && password.isEmpty()) {
            usernameError.setText("Enter a Username!");
            passwordError.setText("Enter a Password!");

        } else if (username.isEmpty()) {
            usernameError.setText("Enter a Username!");
        } else if (PlayerQueries.doesUsernameExist(username)) {
            // Username already exists, display an error message or take appropriate action
            usernameError.setText("Username already exists");
            newPlayerUsername.setText("");
            newPlayerPassword.setText("");
            passwordError.setText(""); // Clear any previous password error message
        } else {
            if (password.isEmpty()) {
                passwordError.setText("Enter a Password!");
            } else if (password.length() < 8)
                passwordError.setText("Enter a minimum of 8 characters.");
            else {
                Player player = new Player(username, password);
                usernameError.setText("");
                passwordError.setText("");
                newPlayerUsername.setText("");
                newPlayerPassword.setText("");
                successfulPlayerCreation.setText("Successfully created player " + username);
                PlayerQueries.addNewAccount(player);
            }
        }

        createPlayerButton.setStyle("-fx-background-color: BLACK; -fx-background-radius: 10;");

        // Reverts color after clicking
        PauseTransition pause = new PauseTransition(Duration.millis(500));
        pause.setOnFinished(e -> {
            createPlayerButton.setStyle("-fx-background-color: #2D222F; -fx-background-radius: 10;");
        });
        pause.play();
    }

    public void applyNewSettings() {
        String selectedRoundTimer = roundTimer.getText();
        String selectedWaitingTimer = waitingTimer.getText();

        if (selectedRoundTimer.isEmpty()) {
            roundTimerError.setText("Cannot be empty");
        } else if (!selectedRoundTimer.matches("\\d+")) {
            roundTimerError.setText("Please enter a number only");
        } else {
            int roundTime = Integer.parseInt(selectedRoundTimer);
            if (roundTime > 60) {
                roundTimerError.setText("Round Time too long");
            } else if (roundTime < 15) {
                roundTimerError.setText("Round time too short");
            } else {
                SettingsQueries.updateWaitingTime(roundTime);
                roundTimer.setPromptText(roundTime + " seconds");
                roundTimer.setText("");
            }
        }

        if (selectedWaitingTimer.isEmpty()) {
            waitingTimerError.setText("Cannot be empty");
        } else if (!selectedWaitingTimer.matches("\\d+")) {
            waitingTimerError.setText("Please enter a number only");
        } else {
            int waitingTime = Integer.parseInt(selectedWaitingTimer);
            if (waitingTime < 10) {
                waitingTimerError.setText("Wait time too short");
            } else {
                SettingsQueries.updateRoundTime(waitingTime);
                waitingTimer.setPromptText(waitingTime + " seconds");
                waitingTimer.setText("");
            }
        }
        successfulSettingsChange.setText("Successfully changed the settings.");

        /**
         * Button reaction
         **/

        applySettingsButton.setStyle("-fx-background-color: BLACK; -fx-background-radius: 10;");
        // Reverts color after clicking
        PauseTransition pause = new PauseTransition(Duration.millis(500));
        pause.setOnFinished(e -> {
            applySettingsButton.setStyle("-fx-background-color: #2D222F; -fx-background-radius: 10;");
        });
        pause.play();
    }

    public void searchPlayer() throws SQLException, IOException {
        String searchUsername = searchBar.getText();

        if (searchUsername.isEmpty()) {
            searchPlayerError.setText("No name entered");
        } else {
            // Retrieve the player information from the database
            Player player = PlayerQueries.getUserInfoByUsername(searchUsername);
            if (player != null) {
                // Set the player object
                this.player = player;
                editButton.setVisible(true);
                searchedUsername.setText(player.getUsername());
                searchedPassword.setText(player.getPassword());
                searchedPoints.setText(String.valueOf(player.getPoints()));
                System.out.println("Player found: " + searchUsername);
                searchButton.setStyle("-fx-background-color: BLACK; -fx-background-radius: 10;");
                PauseTransition pause = new PauseTransition(Duration.seconds(1));
                pause.setOnFinished(e -> {
                    searchButton.setStyle("-fx-background-color: #2D222F; -fx-background-radius: 10;");
                });
                pause.play();
            } else {
                // Player not found, display an appropriate message
                searchPlayerError.setText("No player found");
                System.out.println("Player not found!");
            }
        }
    }



    public void playerList() {
        List<String> playerNames = PlayerQueries.getAllPlayerNames();
        if (playerNames != null) {
            // Clear the ListView
            playerList.getItems().clear();
            // Add all player names to the ListView
            playerList.getItems().addAll(playerNames);
        }
    }
}




