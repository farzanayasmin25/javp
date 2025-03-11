package controllers;

import database.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ComplaintController {
    @FXML private TextField studentIdField;
    @FXML private TextField roomNumberField; // Added Room Number Field
    @FXML private TextArea complaintField;

    @FXML
    public void submitComplaint() {
        String studentId = studentIdField.getText().trim();
        String roomNumber = roomNumberField.getText().trim();
        String complaint = complaintField.getText().trim();

        // ✅ Input validation
        if (studentId.isEmpty() || roomNumber.isEmpty() || complaint.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Input Error", "All fields must be filled!");
            return;
        }

        // ✅ Insert complaint into database
        String query = "INSERT INTO complaints (student_id, room_number, description, status) VALUES (?, ?, ?, 'Pending')";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, studentId);
            stmt.setString(2, roomNumber);
            stmt.setString(3, complaint);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Complaint submitted successfully!");
                clearFields();
            } else {
                showAlert(Alert.AlertType.ERROR, "Submission Failed", "Could not submit your complaint.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "An error occurred while saving your complaint.");
        }
    }

    @FXML
    private void gotodashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/dashboard.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) studentIdField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Dashboard");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Failed to load Dashboard.");
        }
    }

    // ✅ Method to show alerts
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // ✅ Method to clear text fields
    private void clearFields() {
        studentIdField.clear();
        roomNumberField.clear();
        complaintField.clear();
    }
}
