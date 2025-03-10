package controllers;

import database.DatabaseConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
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

public class LoginController {
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
                String query = "SELECT * FROM users WHERE username=? AND password=?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, username);
                stmt.setString(2, password);

                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    // Close the current login window
                    Stage currentStage = (Stage) usernameField.getScene().getWindow();
                    currentStage.close();

                    // Load the dashboard
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/dashboard.fxml"));
                    Parent root = loader.load();
                    Stage stage = new Stage();
                    stage.setTitle("Student Dashboard");
                    stage.setScene(new Scene(root, 598, 444));
                    stage.show();
               }
        else {
                    showAlert("Error", "Invalid Username or Password!");

                        usernameField.clear();
                        passwordField.clear();

                }
            } catch (Exception e) {
                e.printStackTrace();
                showAlert("Error", "An error occurred while trying to login.");
            }


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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/SignUp.fxml"));
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









