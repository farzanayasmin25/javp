package controllers;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import database.DatabaseConnection;

import java.io.IOException;
import java.sql.*;

public class AdminNoticeBoardController {

    @FXML
    private TableView<Notice> noticeTable;
    @FXML
    private TableColumn<Notice, Integer> idColumn;
    @FXML
    private TableColumn<Notice, String> noticeColumn;
    @FXML
    private TableColumn<Notice, String> datecolumn;
    @FXML
    private Button addNoticeButton;

    private ObservableList<Notice> noticeList = FXCollections.observableArrayList();
    private Connection connection;

    // Initialize controller and load data from database
    public void initialize() {
        connectToDatabase();
        loadNoticesFromDatabase();

        // Set up table columns
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        noticeColumn.setCellValueFactory(cellData -> cellData.getValue().noticeProperty());
        datecolumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());

        // Set table data
        noticeTable.setItems(noticeList);
    }

    // Connects to MySQL database
    private void connectToDatabase() {
        connection = DatabaseConnection.getConnection();
        if (connection == null) {
            System.out.println("Error: Could not establish database connection!");
        }
    }

    // Load all notices from the database
    private void loadNoticesFromDatabase() {
        noticeList.clear();
        if (connection == null) {
            System.out.println("Database connection is null.");
            return;
        }

        String query = "SELECT * FROM notices ORDER BY notice_date DESC";
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(query)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String noticeText = rs.getString("notice");
                String noticeDate = rs.getString("notice_date");  // Store as string

                noticeList.add(new Notice(id, noticeText, noticeDate));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Add a new notice to the database
    @FXML
    private void addNotice(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("New Notice");
        dialog.setHeaderText("Enter the notice text:");
        dialog.setContentText("Notice:");

        dialog.showAndWait().ifPresent(noticeText -> {
            if (!noticeText.trim().isEmpty()) {
                try {
                    String insertSQL = "INSERT INTO notices (notice) VALUES (?)";
                    PreparedStatement pstmt = connection.prepareStatement(insertSQL);
                    pstmt.setString(1, noticeText);
                    pstmt.executeUpdate();

                    showAlert("Success", "Notice added successfully!", Alert.AlertType.INFORMATION);
                    loadNoticesFromDatabase(); // Refresh table
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                showAlert("Error", "Notice text cannot be empty!", Alert.AlertType.ERROR);
            }
        });
    }

    // Handle mouse click on a notice
    @FXML
    private void onNoticeSelected(MouseEvent event) {
        Notice selectedNotice = noticeTable.getSelectionModel().getSelectedItem();
        if (selectedNotice != null) {
            showAlert("Notice", selectedNotice.getNotice(), Alert.AlertType.INFORMATION);
        }
    }

    // Navigate back to the admin dashboard
    @FXML
    private void gotodashboard() {
        try {
            Stage currentStage = (Stage) noticeTable.getScene().getWindow();
            currentStage.close();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/admin_dashboard.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Admin Dashboard");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load Admin Dashboard.", Alert.AlertType.ERROR);
        }
    }

    // Utility method to show alerts
    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Notice class for TableView
    public static class Notice {
        private final SimpleIntegerProperty id;
        private final SimpleStringProperty notice;
        private final SimpleStringProperty date;

        public Notice(int id, String notice, String date) {
            this.id = new SimpleIntegerProperty(id);
            this.notice = new SimpleStringProperty(notice);
            this.date = new SimpleStringProperty(date);
        }

        public int getId() { return id.get(); }
        public String getNotice() { return notice.get(); }
        public String getDate() { return date.get(); }

        public SimpleIntegerProperty idProperty() { return id; }
        public SimpleStringProperty noticeProperty() { return notice; }
        public SimpleStringProperty dateProperty() { return date; }
    }
}
