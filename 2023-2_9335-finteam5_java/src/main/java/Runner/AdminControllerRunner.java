package Runner;

import client.utility.InterfaceLoader;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import server.controllers.AdminController;

public class AdminControllerRunner extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the FXML file and set the controller
        InterfaceLoader.loadInterface("/Admin.fxml", primaryStage);
        primaryStage.setTitle("Admin");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}