package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.sql.*;
import java.io.IOException;

public class Admission1Controller {

    @FXML private TextField fullNameField, fatherNameField, motherNameField, emailField, presentAddressField, permanentAddressField;
    @FXML private CheckBox maleCheckBox, femaleCheckBox, marriedCheckBox, unmarriedCheckBox;
    @FXML private ChoiceBox<String> religionChoiceBox, nationalityChoiceBox;
    @FXML private DatePicker dateOfBirthPicker;
    @FXML private Button nextButton, backButton;

    private static final String DB_URL = "jdbc:mysql://localhost:3306/osmanyhall";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "Farzana";

    @FXML
    public void initialize() {
        religionChoiceBox.getItems().addAll("Christianity", "Islam", "Hinduism", "Buddhism", "Other");
        nationalityChoiceBox.getItems().addAll("Bangladeshi", "Indian", "American", "British", "Other");
        religionChoiceBox.setValue("Islam");
        nationalityChoiceBox.setValue("Bangladeshi");

        maleCheckBox.setOnAction(event -> { if (maleCheckBox.isSelected()) femaleCheckBox.setSelected(false); });
        femaleCheckBox.setOnAction(event -> { if (femaleCheckBox.isSelected()) maleCheckBox.setSelected(false); });

        nextButton.setOnAction(event -> handleNextButton());
        backButton.setOnAction(event -> gotohome()); // Ensure the Back button works correctly
    }

    private void handleNextButton() {
        if (fullNameField.getText().isEmpty() || fatherNameField.getText().isEmpty() || motherNameField.getText().isEmpty() ||
                emailField.getText().isEmpty() || presentAddressField.getText().isEmpty() || permanentAddressField.getText().isEmpty() ||
                dateOfBirthPicker.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all required fields.");
            return;
        }

        if (!maleCheckBox.isSelected() && !femaleCheckBox.isSelected()) {
            showAlert(Alert.AlertType.ERROR, "Error", "Please select your gender.");
            return;
        }

        insertAdmission1Data();
    }

    private void insertAdmission1Data() {
        String query = "INSERT INTO admissions (full_name, father_name, mother_name, gender, religion, nationality, date_of_birth, email, marital_status, present_address, permanent_address) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, fullNameField.getText());
            pstmt.setString(2, fatherNameField.getText());
            pstmt.setString(3, motherNameField.getText());
            pstmt.setString(4, maleCheckBox.isSelected() ? "Male" : "Female");
            pstmt.setString(5, religionChoiceBox.getValue());
            pstmt.setString(6, nationalityChoiceBox.getValue());
            pstmt.setString(7, dateOfBirthPicker.getValue().toString());
            pstmt.setString(8, emailField.getText());
            pstmt.setString(9, marriedCheckBox.isSelected() ? "Married" : "Unmarried");
            pstmt.setString(10, presentAddressField.getText());
            pstmt.setString(11, permanentAddressField.getText());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {


                switchToAdmission2();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to submit admission form.");
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void switchToAdmission2() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/admission2.fxml"));
            Parent root = loader.load();

            // Get the controller instance
            Admission2Controller admission2Controller = loader.getController();
            admission2Controller.setFullName(fullNameField.getText()); // Pass full name

            Stage stage = (Stage) nextButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Admission Form - Step 2");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Failed to load Admission2 portal.");
        }
    }


    @FXML
    private void gotohome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/home.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Home");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Failed to load Home.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


}
