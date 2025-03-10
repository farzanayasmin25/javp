package controllers;

import database.DatabaseConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ALoginController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Please enter both username and password.");
        } else {
            try (Connection conn = DatabaseConnection.getConnection()) {

                String query = "SELECT * FROM adminusers WHERE username=? AND password=?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, username);
                stmt.setString(2, password);

                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {

                    Stage oldstage=(Stage) usernameField.getScene().getWindow();
                    oldstage.close();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/admin_dashboard.fxml"));
                    Parent root = loader.load();
                    Stage stage = new Stage();
                    stage.setTitle("Admin Dashboard");
                    stage.setScene(new Scene(root, 548, 500));
                    stage.show();
                    // Navigate to Dashboard
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setContentText("Invalid Username or Password!");
                    alert.show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            // Perform authentication logic here



        }

    }

    @FXML
    private void handleForgotPassword(ActionEvent event) {
        showAlert("Forgot Password", "Redirecting to password recovery...");
    }

    @FXML
    private void handleSignUp() {
        try {
            Stage oldstage=(Stage) usernameField.getScene().getWindow();
            oldstage.close();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Adminsignup.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("SignUp Portal");
            stage.setScene(new Scene(root, 380, 448));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void gotohome() {
        try {
            Stage oldstage=(Stage) usernameField.getScene().getWindow();
            oldstage.close();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/home.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("MIST Osmany Hall");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


}









