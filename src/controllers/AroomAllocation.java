package controllers;

import database.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AroomAllocation {

    @FXML
    private TextField studentIdField;

    @FXML
    private TextField roomNumberField;

    @FXML
    private void allocateRoom() {
        String studentId = studentIdField.getText().trim();
        String roomNumber = roomNumberField.getText().trim();

        // Validate input fields
        if (studentId.isEmpty() || roomNumber.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Please fill in all fields.");
            return;
        }

        // Process room allocation (e.g., save to database)
        boolean allocationSuccessful = allocateRoomToStudent(studentId, roomNumber);

        if (allocationSuccessful) {
            showAlert(Alert.AlertType.INFORMATION, "Success", "Room allocated successfully!");
            clearFields();
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to allocate room. Please try again.");
        }
    }

    private boolean allocateRoomToStudent(String studentId, String roomNumber) {
        String query = "INSERT INTO room_allocations (student_id, room_number) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, studentId);
            stmt.setString(2, roomNumber);
            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void clearFields() {
        studentIdField.clear();
        roomNumberField.clear();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}