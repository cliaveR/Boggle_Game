package client.utility;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.function.Consumer;

public class InterfaceLoader {

    // Load the interface from the FXML file and show it on the stage
    public static void loadInterface(String fxmlPath, Stage stage, Consumer<Object> controller) {
        try {
            FXMLLoader loader = new FXMLLoader(InterfaceLoader.class.getResource(fxmlPath));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setResizable(false);
            stage.setScene(scene);

            Object controllerObj = loader.getController();
            if (controllerObj instanceof Initializable) {
                ((Initializable) controllerObj).initialize(null,null);
            }

            controller.accept(controllerObj);

            if(!stage.isShowing()) {
                stage.show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void loadInterface(String fxmlPath, Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(InterfaceLoader.class.getResource(fxmlPath));
            Parent root = loader.load();
            stage.setTitle("Boggle Application");
            stage.setResizable(false);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void loadInterface(String fxmlPath, Consumer<Object> controller){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(InterfaceLoader.class.getResource(fxmlPath));
        Object controllerObj = loader.getController();
        controller.accept(controllerObj);
    }
}