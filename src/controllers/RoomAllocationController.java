package controllers;

import database.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RoomAllocationController {

    @FXML
    private TextField studentIdField;

    @FXML
    private Label allocatedRoomLabel;
    @FXML
    private Button back;
    @FXML
    private void gotodashboard() {
        try {
            Stage oldstage=(Stage) back.getScene().getWindow();
            oldstage.close();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/dashboard.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("MIST Osmany Hall");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void checkRoom() {
        String studentId = studentIdField.getText().trim();

        // Validate input field
        if (studentId.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Please enter your Student ID.");
            return;
        }

        // Fetch allocated room from database
        String roomNumber = fetchAllocatedRoom(studentId);

        if (roomNumber != null) {
            allocatedRoomLabel.setText(roomNumber);
        } else {
            showAlert(Alert.AlertType.ERROR, "Error", "No room allocated for this Student ID.");
        }
    }

    private String fetchAllocatedRoom(String studentId) {
        String query = "SELECT room_number FROM room_allocations WHERE student_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, studentId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("room_number");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}