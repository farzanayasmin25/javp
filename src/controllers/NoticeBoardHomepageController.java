package controllers;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import database.DatabaseConnection;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class NoticeBoardHomepageController {

    @FXML
    private TableView<Notice> noticeTable;
    @FXML
    private TableColumn<Notice, Integer> idColumn;
    @FXML
    private TableColumn<Notice, String> noticeColumn;
    @FXML
    private TableColumn<Notice, String> dateColumn;

    private ObservableList<Notice> noticeList = FXCollections.observableArrayList();
    private Connection connection;

    // Initialize controller and load data from database
    public void initialize() {
        connectToDatabase();
        loadNoticesFromDatabase();

        // Set up table columns
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        noticeColumn.setCellValueFactory(cellData -> cellData.getValue().noticeProperty());
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());

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
    @FXML
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

    // Handle mouse click on a notice
    @FXML
    private void onNoticeSelected(MouseEvent event) {
        Notice selectedNotice = noticeTable.getSelectionModel().getSelectedItem();
        if (selectedNotice != null) {
            showAlert("Notice", selectedNotice.getNotice(), Alert.AlertType.INFORMATION);
        }
    }
    @FXML
    private void gotohome() {
        try {
            Stage currentStage = (Stage) noticeTable.getScene().getWindow();
            currentStage.close();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/home.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Home page");
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
