package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import database.DatabaseConnection;
import javafx.stage.Stage;
import models.Notice;

import java.io.IOException;
import java.sql.*;

public class AdminNoticeBoardController {

    @FXML
    private TextField titleField;  // New field for title input
    @FXML
    private TextArea noticeTextArea;
    @FXML
    private TableView<Notice> noticeTable;
    @FXML
    private TableColumn<Notice, Integer> idColumn;

    @FXML
    private TableColumn<Notice, String> noticeColumn;

    @FXML
    private TableColumn<Notice, String> titleColumn;
    @FXML
    private TableColumn<Notice, Timestamp> timestampColumn;
    @FXML
    private Button addNoticeButton;
    @FXML
    private Button updateNoticeButton;

    private final ObservableList<Notice> noticeList = FXCollections.observableArrayList();
    private Connection connection;

    public void initialize() {
        connectToDatabase();
        loadNoticesFromDatabase();

        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        titleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        noticeColumn.setCellValueFactory(cellData -> cellData.getValue().noticeProperty());
        timestampColumn.setCellValueFactory(cellData -> cellData.getValue().timestampProperty());

        noticeTable.setItems(noticeList);
    }

    private void connectToDatabase() {
        connection = DatabaseConnection.getConnection();
        if (connection == null) {
            System.out.println("Error: Could not establish database connection!");
        }
    }

    private void loadNoticesFromDatabase() {
        noticeList.clear();
        if (connection == null) {
            System.out.println("Database connection is null.");
            return;
        }

        String query = "SELECT * FROM notices ORDER BY timestamp DESC";
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(query)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String noticeText = rs.getString("notice");
                Timestamp timestamp = rs.getTimestamp("timestamp");

                noticeList.add(new Notice(id, title, noticeText, timestamp));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void addNotice() {
        String title = titleField.getText().trim();
        String noticeText = noticeTextArea.getText().trim();

        if (title.isEmpty() || noticeText.isEmpty()) {
            showAlert("Error", "Title and Notice text cannot be empty!", Alert.AlertType.ERROR);
            return;
        }

        if (connection == null) {
            System.out.println("Database connection is null.");
            return;
        }

        String insertSQL = "INSERT INTO notices (title, notice) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {
            pstmt.setString(1, title);
            pstmt.setString(2, noticeText);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                showAlert("Success", "Notice added successfully!", Alert.AlertType.INFORMATION);
                loadNoticesFromDatabase();
                titleField.clear();
                noticeTextArea.clear();
            } else {
                showAlert("Error", "Failed to add notice.", Alert.AlertType.ERROR);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void updateNotice() {
        Notice selectedNotice = noticeTable.getSelectionModel().getSelectedItem();
        String updatedTitle = titleField.getText().trim();
        String updatedNotice = noticeTextArea.getText().trim();

        if (selectedNotice == null) {
            showAlert("Error", "Please select a notice to update!", Alert.AlertType.ERROR);
            return;
        }

        if (updatedTitle.isEmpty() || updatedNotice.isEmpty()) {
            showAlert("Error", "Title and Notice text cannot be empty!", Alert.AlertType.ERROR);
            return;
        }

        if (connection == null) {
            System.out.println("Database connection is null.");
            return;
        }

        String updateSQL = "UPDATE notices SET title = ?, notice = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(updateSQL)) {
            pstmt.setString(1, updatedTitle);
            pstmt.setString(2, updatedNotice);
            pstmt.setInt(3, selectedNotice.getId());
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                showAlert("Success", "Notice updated successfully!", Alert.AlertType.INFORMATION);
                loadNoticesFromDatabase();
                titleField.clear();
                noticeTextArea.clear();
            } else {
                showAlert("Error", "Failed to update notice.", Alert.AlertType.ERROR);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onNoticeSelected(MouseEvent event) {
        Notice selectedNotice = noticeTable.getSelectionModel().getSelectedItem();
        if (selectedNotice != null) {
            titleField.setText(selectedNotice.getTitle());
            noticeTextArea.setText(selectedNotice.getNotice());
        }
    }
    @FXML
    private void gotodashboard() {
        try {
            Stage currentStage = (Stage) noticeTable.getScene().getWindow(); // Correct stage reference
            currentStage.close();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/home.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Home Page");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.out.println("Error loading home page: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
