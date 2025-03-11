package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;

import java.io.IOException;

public class DashboardController {

    // Method to close the current window and open a new one
    private void openNewWindow(String fxmlFile, String title, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/" + fxmlFile));
            Parent root = loader.load();

            // Create a new stage for the requested page
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();

            // Close the current stage (Dashboard)
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Unable to load " + title + " page.");
        }
    }

    @FXML
    private void goToProfile(ActionEvent event) {
        openNewWindow("studentprofile.fxml", "Student Profile", event);
    }

    @FXML
    private void goToRoomAllocation(ActionEvent event) {
        openNewWindow("room_allocation.fxml", "Room Allocation", event);
    }

    @FXML
    private void goToComplaints(ActionEvent event) {
        openNewWindow("complaint.fxml", "Complaint Page", event);
    }

    @FXML
    private void goToBilling(ActionEvent event) {
        openNewWindow("billing.fxml", "Billing Page", event);
    }

    @FXML
    private void goToReports(ActionEvent event) {
        openNewWindow("student_report.fxml", "Report Page",event);
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        openNewWindow("login.fxml", "Login", event);
    }

    // Helper method to show alerts
    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
