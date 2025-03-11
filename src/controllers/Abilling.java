package controllers;

import database.DatabaseConnection;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.*;

public class Abilling {

    @FXML
    private ComboBox<String> rollNumberComboBox;

    @FXML
    private TextField amountDueField;

    @FXML
    private TextField statusField;

    @FXML
    private TableView<Billing> billingTable;

    @FXML
    private TableColumn<Billing, String> rollColumn;

    @FXML
    private TableColumn<Billing, String> amountColumn;

    @FXML
    private TableColumn<Billing, String> statusColumn;

    @FXML
    private Button goToDashboardButton;

    private ObservableList<Billing> billingList = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        rollColumn.setCellValueFactory(cellData -> cellData.getValue().rollProperty());
        amountColumn.setCellValueFactory(cellData -> cellData.getValue().amountDueProperty());
        statusColumn.setCellValueFactory(cellData -> cellData.getValue().statusProperty());

        billingTable.setItems(billingList);

        loadRollNumbers();
        loadBillingData();
    }

    private void loadRollNumbers() {
        String query = "SELECT roll_no FROM admissions WHERE status = 'Approved'";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                rollNumberComboBox.getItems().add(rs.getString("roll_no"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Error loading roll numbers.");
        }
    }

    private void loadBillingData() {
        billingList.clear();
        String query = "SELECT * FROM billing";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                billingList.add(new Billing(
                        rs.getString("roll_no"),
                        rs.getString("amount_due"),
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Error loading billing data.");
        }
    }

    @FXML
    private void checkDueAmount() {
        String rollNumber = rollNumberComboBox.getValue();
        if (rollNumber == null || rollNumber.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Please select a roll number.");
            return;
        }

        String query = "SELECT amount_due, status FROM billing WHERE roll_no = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, rollNumber);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                amountDueField.setText(rs.getString("amount_due"));
                statusField.setText(rs.getString("status"));
            } else {
                amountDueField.clear();
                statusField.setText("Pending");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Error fetching billing record.");
        }
    }

    @FXML
    private void addOrUpdateBilling() {
        String rollNumber = rollNumberComboBox.getValue();
        String amountDue = amountDueField.getText();

        if (rollNumber == null || rollNumber.isEmpty() || amountDue.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Please fill in all fields.");
            return;
        }

        String query = "INSERT INTO billing (roll_no, amount_due, status) VALUES (?, ?, 'Pending') " +
                "ON DUPLICATE KEY UPDATE amount_due = ?, status = 'Pending'";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, rollNumber);
            stmt.setString(2, amountDue);
            stmt.setString(3, amountDue);
            stmt.executeUpdate();

            loadBillingData();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Billing record added/updated.");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Error updating billing record.");
        }
    }

    @FXML
    private void markAsPaid() {
        Billing selectedBilling = billingTable.getSelectionModel().getSelectedItem();
        if (selectedBilling == null) {
            showAlert(Alert.AlertType.ERROR, "Selection Error", "Please select a billing record.");
            return;
        }

        String updateQuery = "UPDATE billing SET status = 'Paid' WHERE roll_no = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
            stmt.setString(1, selectedBilling.getRoll());
            stmt.executeUpdate();

            selectedBilling.setStatus("Paid");
            billingTable.refresh();
            showAlert(Alert.AlertType.INFORMATION, "Success", "Billing marked as paid.");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Error updating payment status.");
        }
    }

    @FXML
    private void goToDashboard() {
        try {
            Stage oldStage = (Stage) goToDashboardButton.getScene().getWindow();
            oldStage.close();

            // Load the dashboard view for the student (modify path as needed)
            // For example, load the Student Dashboard view.
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/admin_dashboard.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Admin Dashboard");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static class Billing {
        private final StringProperty roll;
        private final StringProperty amountDue;
        private final StringProperty status;

        public Billing(String roll, String amountDue, String status) {
            this.roll = new SimpleStringProperty(roll);
            this.amountDue = new SimpleStringProperty(amountDue);
            this.status = new SimpleStringProperty(status);
        }

        // Correct getter methods
        public String getRoll() {
            return roll.get();
        }

        public String getAmountDue() {
            return amountDue.get();
        }

        public String getStatus() {
            return status.get();
        }

        public StringProperty rollProperty() {
            return roll;
        }

        public StringProperty amountDueProperty() {
            return amountDue;
        }

        public StringProperty statusProperty() {
            return status;
        }

        public void setStatus(String status) {
            this.status.set(status);
        }
    }
}
