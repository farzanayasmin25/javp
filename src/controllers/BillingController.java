package controllers;

import database.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class BillingController {
    @FXML
    private TextField studentIdField;
    @FXML
    private Label billAmountLabel;

    public void checkBill() {
        String studentId = studentIdField.getText();

        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT amount FROM bills WHERE student_id=?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, studentId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                billAmountLabel.setText("Bill Amount: " + rs.getDouble("amount") + " BDT");
            } else {
                billAmountLabel.setText("No bill found for this ID.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

