package controllers;

import database.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class studentprofile {
    @FXML private TextField rollNoField;
    @FXML private Button searchButton;
    @FXML private Label fullNameLabel, fatherNameLabel, motherNameLabel, genderLabel, religionLabel, nationalityLabel;
    @FXML private Label dobLabel, emailLabel, maritalStatusLabel, presentAddressLabel, permanentAddressLabel;
    @FXML private Label phoneLabel, fatherPhoneLabel, motherPhoneLabel, departmentLabel, rollLabel, statusLabel;

    private Connection connection;

    public void initialize() {
        connection = DatabaseConnection.getConnection();
        if (connection == null) {
            statusLabel.setText("Database connection error!");
        }
    }

    @FXML
    private void searchStudent() {
        String rollNo = rollNoField.getText().trim();

        if (rollNo.isEmpty()) {
            statusLabel.setText("Please enter a Roll Number.");
            return;
        }

        String query = "SELECT * FROM admissions WHERE roll_no = ? AND status = 'Approved'";

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, rollNo);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                // Update Labels with Student Data
                fullNameLabel.setText(rs.getString("full_name"));
                fatherNameLabel.setText(rs.getString("father_name"));
                motherNameLabel.setText(rs.getString("mother_name"));
                genderLabel.setText(rs.getString("gender"));
                religionLabel.setText(rs.getString("religion"));
                nationalityLabel.setText(rs.getString("nationality"));
                dobLabel.setText(rs.getString("date_of_birth"));
                emailLabel.setText(rs.getString("email"));
                maritalStatusLabel.setText(rs.getString("marital_status"));
                presentAddressLabel.setText(rs.getString("present_address"));
                permanentAddressLabel.setText(rs.getString("permanent_address"));
                phoneLabel.setText(rs.getString("phone_no"));
                fatherPhoneLabel.setText(rs.getString("fathers_phone_no"));
                motherPhoneLabel.setText(rs.getString("mothers_phone_no"));
                departmentLabel.setText(rs.getString("department_name"));
                rollLabel.setText(rs.getString("roll_no"));

                statusLabel.setText("Profile found.");
                statusLabel.setStyle("-fx-text-fill: green;");
            } else {
                statusLabel.setText("No approved student found with this Roll No.");
                clearProfileData();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            statusLabel.setText("Error retrieving data.");
        }
    }

    private void clearProfileData() {
        fullNameLabel.setText("-");
        fatherNameLabel.setText("-");
        motherNameLabel.setText("-");
        genderLabel.setText("-");
        religionLabel.setText("-");
        nationalityLabel.setText("-");
        dobLabel.setText("-");
        emailLabel.setText("-");
        maritalStatusLabel.setText("-");
        presentAddressLabel.setText("-");
        permanentAddressLabel.setText("-");
        phoneLabel.setText("-");
        fatherPhoneLabel.setText("-");
        motherPhoneLabel.setText("-");
        departmentLabel.setText("-");
        rollLabel.setText("-");
    }
    @FXML
    private void gotodashboard() {
        try {
            Stage currentStage = (Stage) rollNoField.getScene().getWindow();
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
