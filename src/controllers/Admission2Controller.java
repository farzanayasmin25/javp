package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class Admission2Controller {

    @FXML
    private TextField phoneNoField, fathersPhoneNoField, mothersPhoneNoField, departmentNameField, rollNoField;
    @FXML
    private CheckBox guardianYesCheckBox, guardianNoCheckBox;
    @FXML
    private TextField guardianNameField, guardianAddressField, guardianPhoneNoField;
    @FXML
    private TextArea reasonForHallSeatField;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/osmanyhall";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Farzana";

    private String fullName; // Store the full name received from Admission1

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @FXML
    private void handlesubmit() {
        if (fullName == null || fullName.trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Full name is missing. Cannot update record.");
            return;
        }

        String query = "UPDATE admissions SET phone_no = ?, fathers_phone_no = ?, mothers_phone_no = ?, " +
                "department_name = ?, roll_no = ?, has_guardian = ?, guardian_name = ?, guardian_address = ?, " +
                "guardian_phone_no = ?, reason_for_hall_seat = ? WHERE full_name = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, phoneNoField.getText());
            pstmt.setString(2, fathersPhoneNoField.getText());
            pstmt.setString(3, mothersPhoneNoField.getText());
            pstmt.setString(4, departmentNameField.getText());
            pstmt.setString(5, rollNoField.getText());
            pstmt.setString(6, guardianYesCheckBox.isSelected() ? "Yes" : "No");
            pstmt.setString(7, guardianYesCheckBox.isSelected() ? guardianNameField.getText() : null);
            pstmt.setString(8, guardianYesCheckBox.isSelected() ? guardianAddressField.getText() : null);
            pstmt.setString(9, guardianYesCheckBox.isSelected() ? guardianPhoneNoField.getText() : null);
            pstmt.setString(10, reasonForHallSeatField.getText());
            pstmt.setString(11, fullName);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Data submitted successfully.");
                clearForm();
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/home.fxml"));
                    Parent root = loader.load();
                    Stage stage = (Stage) phoneNoField.getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.setTitle("Home page");
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Navigation Error", "Failed to load Home.");
                }

            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "No record found with the given full name.");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    @FXML
    private void goToadmission1() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/admission1.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) phoneNoField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Admission Form");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Failed to load Home.");
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearForm() {
        phoneNoField.clear();
        fathersPhoneNoField.clear();
        mothersPhoneNoField.clear();
        departmentNameField.clear();
        rollNoField.clear();
        guardianYesCheckBox.setSelected(false);
        guardianNoCheckBox.setSelected(false);
        guardianNameField.clear();
        guardianAddressField.clear();
        guardianPhoneNoField.clear();
        reasonForHallSeatField.clear();
    }
}
