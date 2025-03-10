package client.controllers;

import BoggleApp.PlayerServant;
import BoggleApp.WordNotFoundException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InGameController {
    public Text roundTime;
    public TableView tableView;
    @FXML
    private TextField wordField;
    @FXML
    private Button enterButton;
    @FXML
    private TextArea inGameConsole;
    @FXML
    private GridPane gridPane;

    private static PlayerServant playerServant;
    private int playerID;
    private int gameID;
    private static int playerScore;
    private static ArrayList<String> wordListUser = new ArrayList<>();
    private static char[] generatedLetters;
    private int runningRoundTime;
    private boolean roundOnGoing = true;
    int roundID;
    String roundWinner;
    int gameWinner = 0;
    String gameWinnerName = "none";
    int currentRound;
    private Map<String, Integer> nameWins = new ConcurrentHashMap<>();

    Thread countDownThread;
    Thread endRoundThread;
    Thread endGameThread;

    public void initialize(PlayerServant playerServant, int pID, int gameId) {
        this.playerServant = playerServant;
        this.gameID = gameId;
        this.playerID = pID;
        currentRound = 1;

        runningRoundTime = playerServant.getRoundTime(gameId);
        System.out.println("Running round time: " + runningRoundTime);

        if (gameWinnerName.equalsIgnoreCase("none")) {
            countDownThread = createCountDownThread(startRound(gameId, currentRound));
            endRoundThread = createEndRoundThread(countDownThread);
            endGameThread = createEndGameThread(endRoundThread, gameId, (Stage) enterButton.getScene().getWindow());
            endGameThread.start();
        }
    }

    Thread startRound(int gameId, int currentRound) {
        return new Thread(() -> {
            try {
                if (endGameThread != null && endGameThread.isAlive()) {
                    Thread.sleep(runningRoundTime);
                    System.out.println("round starts in " + runningRoundTime);
                }
                roundID = playerServant.startRound(gameId, currentRound);
                System.out.println("Round ID: " + roundID);
                char[] letters = playerServant.getLetterSet(gameId, roundID);
                for (char c : letters) {
                    System.out.print(c);
                }
                if (letters == null || letters.length < 20) {
                    throw new IOException("Failed to retrieve letters for the grid.");
                }

                Platform.runLater(() -> {
                    gridPane.getChildren().clear();
                    for (int row = 0; row < 5; row++) {
                        for (int col = 0; col < 4; col++) {
                            try {
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/TextTile.fxml"));
                                Node node = loader.load();
                                TextTile controller = loader.getController();
                                controller.setText(Character.toString(letters[row * 4 + col]));
                                gridPane.add(node, col, row);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    Thread createCountDownThread(Thread startRoundThread) {
        startRoundThread.start();
        return new Thread(() -> {
            try {
                startRoundThread.join();
                while (roundOnGoing) {
                    Thread.sleep(1000);
                    Platform.runLater(() -> {
                        roundTime.setText(String.valueOf(runningRoundTime));
                        if (runningRoundTime <= 7) {
                            System.out.println("Running round time: T-" + runningRoundTime);
                        }
                    });
                    runningRoundTime--;
                    if (runningRoundTime < 1) {
                        roundOnGoing = false;
                        System.out.println("Finished the round time");
                    }
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    Thread createEndRoundThread(Thread countDownThread) {
        countDownThread.start();
        return new Thread(() -> {
            try {
                countDownThread.join();
                Platform.runLater(() -> {
                    System.out.println("Fetching game results");
                    roundWinner = playerServant.getRoundWinner(roundID);
                    if (roundWinner.equalsIgnoreCase("none")) {
                        inGameConsole.appendText("There are no winners in the previous round\n");

                    } else {
                        inGameConsole.appendText("Winner of the previous round is " + roundWinner + "!\n");
                    }
                });
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    Thread createEndGameThread(Thread endRoundThreadHere, int gameId, Stage stage) {
        endRoundThreadHere.start();
        return new Thread(() -> {
            try {
                endRoundThreadHere.join();
                Platform.runLater(() -> {
                    gameWinnerName = playerServant.getGameWinner(gameId);
                    System.out.println("Game winner " + gameWinnerName);
                    if (gameWinnerName.equalsIgnoreCase("none")) {
                        inGameConsole.appendText("There are still no game winners. \n");

                        roundOnGoing = true;
                        runningRoundTime = playerServant.getRoundTime(gameId);
                        currentRound++;

                        countDownThread = createCountDownThread(startRound(gameId, currentRound));
                        endRoundThread = createEndRoundThread(countDownThread);
                        endGameThread = createEndGameThread(endRoundThread, gameId, (Stage) enterButton.getScene().getWindow());
                        endGameThread.start();
                    } else {
                        inGameConsole.appendText(gameWinnerName + " has won the game. Returning to lobby...");
                        try {
                            Thread.sleep(5000); // Wait before returning to the lobby
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
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
                    }
                });
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @FXML
    void wordButtonPressed() throws WordNotFoundException {
        String userInputWord = wordField.getText().toLowerCase();
        boolean success = playerServant.checkWord(userInputWord, playerID, roundID);
        inGameConsole.appendText(userInputWord + "\n");
        System.out.println(success);
    }
}
