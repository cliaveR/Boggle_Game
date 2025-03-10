package client;

import BoggleApp.PlayerServant;
import client.controllers.InGameController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApp extends Application {

    private static PlayerServant playerServantStub;
    private static int playerID = 1; // Example player ID
    private static int gameID = 1; // Example game ID

    public static void main(String[] args) {
        // Initialize the playerServantStub with your specific implementation
        // playerServantStub = ... (initialize your PlayerServant stub)

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/InGame.fxml"));
            AnchorPane root = loader.load();

            InGameController controller = loader.getController();
            controller.initialize(playerServantStub,playerID, gameID);

            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle("Boggle Game");
            primaryStage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setStub(PlayerServant stub){
        playerServantStub = stub;
    }
}
