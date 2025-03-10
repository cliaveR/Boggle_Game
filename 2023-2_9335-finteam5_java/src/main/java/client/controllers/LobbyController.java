package client.controllers;

import BoggleApp.Player;
import BoggleApp.PlayerServant;
import client.utility.InterfaceLoader;
import databaseClass.PlayerQueries;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.List;

public class LobbyController {
    private static PlayerServant playerServant;
    private int playerID;
    public TextArea LeaderboardTextArea;
    public Button startGameButton;
    int waitingTime;
    static int gameId;
    boolean waitingLoop = true;

    @FXML
    public void initialize(PlayerServant stub, int pID) {
        playerServant = stub;
        playerID = pID;
        displayLeaderboard();
    }
    @FXML
    void statGameButton(ActionEvent event) {
        Stage stage = (Stage) startGameButton.getScene().getWindow();

        // Start server communication
        Thread gameStartThread = new Thread(() -> {
            gameId = playerServant.startGame(playerID);
            System.out.println("Game ID: " + gameId);
        });
        gameStartThread.start();

        // Display loading screen
        new Thread(() -> {
            while (waitingLoop) {
                // Wait for some time before fetching the waiting time again
                try {
                    Thread.sleep(1000); // Fetch waiting time every second (adjust as needed)
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                waitingTime = playerServant.getWaitingTime();
                System.out.println("Waiting time: T-" + waitingTime);

                // Update UI with the latest waiting time
                Platform.runLater(() -> {
                    InterfaceLoader.loadInterface("/WaitingScreen.fxml", stage, controller -> ((WaitingScreen) controller).setWaitingTime(waitingTime));
                });
                if (waitingTime<1){
                    waitingLoop = false;
                }
            }
        }).start();

        // Check game start result
        Thread gameCheckThread = new Thread(() -> {
            try {
                gameStartThread.join(); // Wait for server communication to finish
                Platform.runLater(() -> {
                    if (gameId > 0) {
                        System.out.println("Game received: " + gameId);
                        InterfaceLoader.loadInterface("/InGame.fxml", stage, controller -> ((InGameController)controller).initialize(playerServant,playerID,gameId));
                    } else {

                        System.out.println("Game received: " + gameId);
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Lobby.fxml"));
                        try {
                            Parent root = fxmlLoader.load();
                            LobbyController controller = fxmlLoader.getController();
                            controller.initialize(playerServant, playerID);
                            Scene scene = new Scene(root);
                            stage.setScene(scene);
                            stage.setTitle("Lobby");
                            stage.show();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                        startGameButton.setStyle("-fx-background-color: BLACK; -fx-background-radius: 10;");
                        // Reverts color after clicking
                        PauseTransition pause = new PauseTransition(Duration.seconds(1));
                        pause.setOnFinished(e -> {
                            startGameButton.setStyle("-fx-background-color: #2D222F; -fx-background-radius: 10;");
                        });
                        pause.play();
                    }
                });
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        gameCheckThread.start();

    }
    private void displayLeaderboard() {
        BoggleApp.Player[] leaderboardDetails = playerServant.getLeaderBoard();
        for (Player p: leaderboardDetails){
            LeaderboardTextArea.appendText(String.format("%-10s\t\t\t\t\t%5d\n", p.username, p.points));
        }
    }


    }

