package controllers;

import database.DatabaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import models.Complaint;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Acomplaint {

    @FXML private TableView<Complaint> complaintTable;
    @FXML private TableColumn<Complaint, Integer> idColumn;  // ✅ Updated name
    @FXML private TableColumn<Complaint, String> studentIdColumn;
    @FXML private TableColumn<Complaint, String> descriptionColumn;
    @FXML private TableColumn<Complaint, String> statusColumn;

    @FXML
    public void initialize() {
        // ✅ Set up table columns correctly
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        studentIdColumn.setCellValueFactory(new PropertyValueFactory<>("studentId"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        loadComplaints(); // Load complaints from the database
    }

    private void loadComplaints() {
        ObservableList<Complaint> complaints = FXCollections.observableArrayList();
        String query = "SELECT * FROM complaints";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                complaints.add(new Complaint(
                        rs.getInt("id"),
                        rs.getString("student_id"),
                        rs.getString("description"),
                        rs.getString("status")
                ));
            }

            complaintTable.setItems(complaints);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleResolveComplaint() {
        Complaint selectedComplaint = complaintTable.getSelectionModel().getSelectedItem();

        if (selectedComplaint == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a complaint to resolve.");
            return;
        }

        String query = "UPDATE complaints SET status = 'Resolved' WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, selectedComplaint.getId());
            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Complaint resolved successfully!");
                loadComplaints(); // Refresh the table
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to resolve complaint.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void gotoadmindashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/home.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) complaintTable.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Home");
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
}
