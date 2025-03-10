package Runner;

import BoggleApp.PlayerServant;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import client.controllers.LoginController;

public class LoginControllerRunner extends Application {
    private static PlayerServant stub;
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the FXML file and set the controller

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Login.fxml"));
        Parent root = loader.load();

        // Set the controller for the loaded FXML
        LoginController controller = loader.getController();
        controller.setStub(stub);

        // Set up the scene
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Login");
        primaryStage.show();
    }

    public static void setStub(PlayerServant playerServant){
        stub = playerServant;
    }
    public static void main(String[] args) {
        launch(args);
    }
}