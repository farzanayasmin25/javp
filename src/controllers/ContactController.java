package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ContactController {

    @FXML
    public void initialize() {
        System.out.println("Contact Portal Loaded Successfully!");
    }

    @FXML
    private void gotohome() {
        try {
            // Get the current stage and close it
            Stage currentStage = (Stage) Stage.getWindows().filtered(window -> window.isShowing()).get(0);
            currentStage.close();

            // Load the home page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/home.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Home Page");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading home portal.");
        }
    }
}

