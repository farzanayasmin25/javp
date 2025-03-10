package controllers;

import database.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EmergencyContactController {
    @FXML
    private TextField studentIdField, contactNameField, contactNumberField;

    public void addEmergencyContact() {
        String studentId = studentIdField.getText();
        String contactName = contactNameField.getText();
        String contactNumber = contactNumberField.getText();

        if (studentId.isEmpty() || contactName.isEmpty() || contactNumber.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "All fields are required!");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) { // Using existing database connection
            String query = "INSERT INTO emergency_contacts (student_id, contact_name, contact_number) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, studentId);
            stmt.setString(2, contactName);
            stmt.setString(3, contactNumber);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Emergency Contact Added Successfully!");
                clearFields();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to add contact. Try again.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to connect to the database.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields() {
        studentIdField.clear();
        contactNameField.clear();
        contactNumberField.clear();
    }
}
