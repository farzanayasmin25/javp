package controllers;

import database.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import models.Notice;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.IOException;
import java.sql.*;

public class NoticeBoardHomepageController {

    @FXML
    private TableView<Notice> noticeTable;

    @FXML
    private TableColumn<Notice, Integer> idColumn;

    @FXML
    private TableColumn<Notice, String> titleColumn; // Added missing title column

    @FXML
    private TableColumn<Notice, String> noticeColumn;

    @FXML
    private TableColumn<Notice, Timestamp> timestampColumn;

    private ObservableList<Notice> noticeList;
    private Connection conn;

    public NoticeBoardHomepageController() {
        conn = DatabaseConnection.getConnection(); // Ensure database connection is established
    }

    @FXML
    private void initialize() {
        noticeList = FXCollections.observableArrayList();

        // Binding table columns
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        titleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        noticeColumn.setCellValueFactory(cellData -> cellData.getValue().noticeProperty());
        timestampColumn.setCellValueFactory(cellData -> cellData.getValue().timestampProperty());

        noticeTable.setItems(noticeList);
        loadNotices();
    }

    @FXML
    private void loadNotices() {
        String query = "SELECT * FROM notices ORDER BY timestamp DESC";

        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            noticeList.clear();
            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title"); // Fetching title from DB
                String notice = rs.getString("notice");
                Timestamp timestamp = rs.getTimestamp("timestamp");

                noticeList.add(new Notice(id, title, notice, timestamp)); // Passing correct parameters
            }
        } catch (SQLException e) {
            System.out.println("Error loading notices: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void refreshNotices() {
        loadNotices();
    }

    @FXML
    private void gotohome() {
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
}
