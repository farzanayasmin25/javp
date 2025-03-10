package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.sql.*;
import models.BillingRecord;

public class Abilling {
    @FXML
    private TableView<BillingRecord> billingTable;
    @FXML
    private TableColumn<BillingRecord, String> studentIdColumn;
    @FXML
    private TableColumn<BillingRecord, String> amountDueColumn;
    @FXML
    private TableColumn<BillingRecord, String> statusColumn;
    @FXML
    private ComboBox<String> studentIdComboBox;
    @FXML
    private TextField amountField;

    private Connection conn;

    public void initialize() {
        connectDatabase();
        loadStudentIds();
        loadBillingRecords();
    }

    private void connectDatabase() {
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/osmanyhall", "root", "Farzana");
            System.out.println("Database connected successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadStudentIds() {
        try {
            studentIdComboBox.getItems().clear();
            String query = "SELECT DISTINCT roll_no FROM admissions";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                studentIdComboBox.getItems().add(rs.getString("roll_no"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadBillingRecords() {
        ObservableList<BillingRecord> billingList = FXCollections.observableArrayList();
        try {
            String query = "SELECT * FROM billing";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                billingList.add(new BillingRecord(
                        rs.getString("roll_no"),
                        rs.getString("amount_due"),
                        rs.getString("status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        billingTable.setItems(billingList);
    }

    @FXML
    private void insertBillingRecord() {
        String studentId = studentIdComboBox.getValue();
        String amount = amountField.getText();

        if (studentId == null || amount.isEmpty()) {
            showAlert("Error", "Please fill all fields!");
            return;
        }

        try {
            String query = "INSERT INTO billing (roll_no, amount_due, status) VALUES (?, ?, 'Pending')";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, studentId);
            stmt.setString(2, amount);
            stmt.executeUpdate();
            showAlert("Success", "Billing record added!");
            loadBillingRecords();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void updatePayment() {
        BillingRecord selected = billingTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Error", "Please select a record to update!");
            return;
        }

        try {
            String query = "UPDATE billing SET status='Paid' WHERE roll_no=?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, selected.getStudentId());
            stmt.executeUpdate();
            showAlert("Success", "Payment updated!");
            loadBillingRecords();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
