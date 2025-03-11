package controllers;

import database.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentReportController {

    @FXML
    private TextField rollNumberField;
    @FXML
    private Button checkStatusButton;
    @FXML
    private Label statusLabel;

    private Connection connection;

    public void initialize() {
        connection = DatabaseConnection.getConnection();
    }

    @FXML
    private void checkStatus() {
        String rollNumber = rollNumberField.getText().trim();

        if (rollNumber.isEmpty()) {
            statusLabel.setText("Please enter your Roll Number.");
            return;
        }

        String query = "SELECT status FROM admissions WHERE roll_no = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, rollNumber);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String status = rs.getString("status");
                if ("Approved".equalsIgnoreCase(status)) {
                    statusLabel.setText("Congratulations! Your admission is Approved.");
                    statusLabel.setStyle("-fx-text-fill: white;");
                } else {
                    statusLabel.setText("Sorry! Your admission was Rejected.");
                    statusLabel.setStyle("-fx-text-fill: red;");
                }
            } else {
                statusLabel.setText("No record found for this Roll Number.");
                statusLabel.setStyle("-fx-text-fill: red;");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            statusLabel.setText("Database error. Please try again.");
        }
    }
    @FXML
    private void gotodashboard() {
        try {
            Stage currentStage = (Stage) rollNumberField.getScene().getWindow();
            currentStage.close();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/dashboard.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Student Dashboard");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();

        }
    }
}
