package client.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FXMLNavigator {
    public static void navigateToFXML(String fxmlPath) {
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(FXMLNavigator.class.getResource(fxmlPath));
            Parent root = loader.load();

            // Create a new scene with the loaded content
            Scene scene = new Scene(root);

            // Get the primary stage
            Stage primaryStage = new Stage();

            // Set the new scene in the primary stage
            primaryStage.setScene(scene);

            // Show the primary stage
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
