package controllers;

import javafx.application.Platform;
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
import database.DatabaseConnection;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class managestudent {

    @FXML
    private TableView<Student> studentTable;
    @FXML
    private TableColumn<Student, Integer> idColumn;
    @FXML
    private TableColumn<Student, String> nameColumn;
    @FXML
    private TableColumn<Student, String> departmentColumn;
    @FXML
    private TableColumn<Student, String> rollColumn;
    @FXML
    private TableColumn<Student, String> reasonColumn; // New column for reason for hall seat
    @FXML
    private TableColumn<Student, String> statusColumn;
    @FXML
    private Button approveButton;
    @FXML
    private Button rejectButton;

    private ObservableList<Student> studentList = FXCollections.observableArrayList();
    private Connection connection;

    public void initialize() {
        connectToDatabase();
        loadStudentsFromDatabase();

        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        departmentColumn.setCellValueFactory(cellData -> cellData.getValue().departmentProperty());
        rollColumn.setCellValueFactory(cellData -> cellData.getValue().rollProperty());
        reasonColumn.setCellValueFactory(cellData -> cellData.getValue().reasonProperty()); // Set cell value factory for the new column
        statusColumn.setCellValueFactory(cellData -> cellData.getValue().statusProperty());

        studentTable.setItems(studentList);
    }

    private void connectToDatabase() {
        connection = DatabaseConnection.getConnection();
        if (connection == null) {
            System.out.println("Error: Could not establish database connection!");
        }
    }

    private void loadStudentsFromDatabase() {
        studentList.clear();
        if (connection == null) {
            System.out.println("Database connection is null.");
            return;
        }

        String query = "SELECT admission_id, full_name, department_name, roll_no, reason_for_hall_seat, status FROM admissions";
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(query)) {

            while (rs.next()) {
                int id = rs.getInt("admission_id");
                String name = rs.getString("full_name");
                String department = rs.getString("department_name");
                String roll = rs.getString("roll_no");
                String reason = rs.getString("reason_for_hall_seat"); // Get reason for hall seat
                String status = rs.getString("status");

                studentList.add(new Student(id, name, department, roll, reason, status));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void approveStudent(ActionEvent event) {
        Student selectedStudent = studentTable.getSelectionModel().getSelectedItem();
        if (selectedStudent != null) {
            updateStudentStatus(selectedStudent, "Approved");
        }
    }

    @FXML
    private void rejectStudent(ActionEvent event) {
        Student selectedStudent = studentTable.getSelectionModel().getSelectedItem();
        if (selectedStudent != null) {
            updateStudentStatus(selectedStudent, "Rejected");
        }
    }

    private void updateStudentStatus(Student student, String newStatus) {
        if (connection == null) {
            System.out.println("Database connection is null. Cannot update status.");
            return;
        }

        String updateQuery = "UPDATE admissions SET status = ? WHERE admission_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(updateQuery)) {
            pstmt.setString(1, newStatus);
            pstmt.setInt(2, student.getId());
            int updatedRows = pstmt.executeUpdate();

            if (updatedRows > 0) {
                student.setStatus(newStatus);
                Platform.runLater(() -> studentTable.refresh());
                System.out.println("Student status updated successfully.");
            } else {
                System.out.println("Error: No rows updated.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void gotoadmindashboard() {
        try {
            Stage currentStage = (Stage) approveButton.getScene().getWindow();
            currentStage.close();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/admin_dashboard.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Home page");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class Student {
        private final SimpleIntegerProperty id;
        private final SimpleStringProperty name;
        private final SimpleStringProperty department;
        private final SimpleStringProperty roll;
        private final SimpleStringProperty reason; // New property for reason
        private final SimpleStringProperty status;

        public Student(int id, String name, String department, String roll, String reason, String status) {
            this.id = new SimpleIntegerProperty(id);
            this.name = new SimpleStringProperty(name);
            this.department = new SimpleStringProperty(department);
            this.roll = new SimpleStringProperty(roll);
            this.reason = new SimpleStringProperty(reason); // Initialize reason property
            this.status = new SimpleStringProperty(status);
        }

        public int getId() { return id.get(); }
        public String getName() { return name.get(); }
        public String getDepartment() { return department.get(); }
        public String getRoll() { return roll.get(); }
        public String getReason() { return reason.get(); } // Getter for reason
        public String getStatus() { return status.get(); }

        public void setStatus(String newStatus) { status.set(newStatus); }

        public SimpleIntegerProperty idProperty() { return id; }
        public SimpleStringProperty nameProperty() { return name; }
        public SimpleStringProperty departmentProperty() { return department; }
        public SimpleStringProperty rollProperty() { return roll; }
        public SimpleStringProperty reasonProperty() { return reason; } // Property method for reason
        public SimpleStringProperty statusProperty() { return status; }
    }
}