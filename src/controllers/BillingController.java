package controllers;

import database.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.sql.*;

public class BillingController {

    @FXML
    private TextField rollNumberField;

    @FXML
    private TextField amountDueField;

    @FXML
    private TextField statusField;

    @FXML
    private Button fetchBillingDetailsButton;

    @FXML
    private Button goToDashboardButton;

    @FXML
    private void fetchBillingDetails() {
        String rollNumber = rollNumberField.getText().trim();

        // Validate input
        if (rollNumber.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Please enter your Roll Number.");
            return;
        }

        // Step 1: Check if the student is "Approved"
        if (!isStudentApproved(rollNumber)) {
            showAlert(Alert.AlertType.ERROR, "Access Denied", "You are not an approved student.");
            return;
        }

        // Step 2: Fetch billing details if approved
        fetchBillingData(rollNumber);
    }

    private boolean isStudentApproved(String rollNumber) {
        String query = "SELECT status FROM admissions WHERE roll_no = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, rollNumber);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("status").equalsIgnoreCase("Approved");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void fetchBillingData(String rollNumber) {
        String query = "SELECT amount_due, status FROM billing WHERE roll_no = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, rollNumber);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                amountDueField.setText(rs.getString("amount_due"));
                statusField.setText(rs.getString("status"));
            } else {
                amountDueField.clear();
                statusField.clear();
                showAlert(Alert.AlertType.INFORMATION, "No Record", "No billing record found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Error fetching billing details.");
        }
    }

    @FXML
    private void goToDashboard() {
        try {
            Stage oldStage = (Stage) goToDashboardButton.getScene().getWindow();
            oldStage.close();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/dashboard.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Student Dashboard");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
