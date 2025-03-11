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
import database.DatabaseConnection;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

public class AroomAllocation {

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
    private TableColumn<Student, String> roomColumn;
    @FXML
    private TextField roomNumberField;
    @FXML
    private Button allocateRoomButton;
    @FXML
    private Label statusLabel;

    private ObservableList<Student> studentList = FXCollections.observableArrayList();
    private Connection connection;

    public void initialize() {
        connectToDatabase();
        loadApprovedStudents();

        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        departmentColumn.setCellValueFactory(cellData -> cellData.getValue().departmentProperty());
        rollColumn.setCellValueFactory(cellData -> cellData.getValue().rollProperty());
        roomColumn.setCellValueFactory(cellData -> cellData.getValue().roomProperty());

        studentTable.setItems(studentList);
    }

    private void connectToDatabase() {
        connection = DatabaseConnection.getConnection();
        if (connection == null) {
            System.out.println("Error: Could not establish database connection!");
        }
    }

    private void loadApprovedStudents() {
        studentList.clear();
        if (connection == null) {
            System.out.println("Database connection is null.");
            return;
        }

        String query = "SELECT admission_id, full_name, department_name, roll_no, room_no FROM admissions WHERE status = 'Approved'";
        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(query)) {

            while (rs.next()) {
                int id = rs.getInt("admission_id");
                String name = rs.getString("full_name");
                String department = rs.getString("department_name");
                String roll = rs.getString("roll_no");
                String room = rs.getString("room_no") != null ? rs.getString("room_no") : "Not Assigned";

                studentList.add(new Student(id, name, department, roll, room));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void allocateRoom(ActionEvent event) {
        Student selectedStudent = studentTable.getSelectionModel().getSelectedItem();
        String selectedRoom = roomNumberField.getText().trim();

        if (selectedStudent == null || selectedRoom.isEmpty()) {
            statusLabel.setText("Please select a student and enter a room number.");
            return;
        }

        // Check if the room is already occupied
        String checkRoomQuery = "SELECT available FROM rooms WHERE room_no = ?";
        try (PreparedStatement checkRoomPstmt = connection.prepareStatement(checkRoomQuery)) {
            checkRoomPstmt.setString(1, selectedRoom);
            ResultSet rs = checkRoomPstmt.executeQuery();
            if (rs.next() && rs.getInt("available") == 0) {
                statusLabel.setText("Room is already occupied.");
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            statusLabel.setText("Error checking room availability.");
            return;
        }

        // Check if the student already has a room assigned
        if (selectedStudent.getRoom() != null && !selectedStudent.getRoom().equals("Not Assigned")) {
            statusLabel.setText("Room is already assigned to this student.");
            return;
        }

        // Proceed with room allocation
        String updateQuery = "UPDATE admissions SET room_no = ? WHERE admission_id = ?";
        String markRoomOccupied = "UPDATE rooms SET available = 0 WHERE room_no = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(updateQuery);
             PreparedStatement pstmtRoom = connection.prepareStatement(markRoomOccupied)) {

            pstmt.setString(1, selectedRoom);
            pstmt.setInt(2, selectedStudent.getId());
            pstmt.executeUpdate();

            pstmtRoom.setString(1, selectedRoom);
            pstmtRoom.executeUpdate();

            selectedStudent.setRoom(selectedRoom);
            studentTable.refresh();
            statusLabel.setText("Room allocated successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
            statusLabel.setText("Error allocating room.");
        }
    }

    @FXML
    private void gotodashboard() {
        try {
            Stage currentStage = (Stage) allocateRoomButton.getScene().getWindow();
            currentStage.close();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/admin_dashboard.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Admin Dashboard");
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
        private final SimpleStringProperty room;

        public Student(int id, String name, String department, String roll, String room) {
            this.id = new SimpleIntegerProperty(id);
            this.name = new SimpleStringProperty(name);
            this.department = new SimpleStringProperty(department);
            this.roll = new SimpleStringProperty(roll);
            this.room = new SimpleStringProperty(room);
        }

        public int getId() { return id.get(); }
        public String getName() { return name.get(); }
        public String getDepartment() { return department.get(); }
        public String getRoll() { return roll.get(); }
        public String getRoom() { return room.get(); }

        public void setRoom(String newRoom) { room.set(newRoom); }

        public SimpleIntegerProperty idProperty() { return id; }
        public SimpleStringProperty nameProperty() { return name; }
        public SimpleStringProperty departmentProperty() { return department; }
        public SimpleStringProperty rollProperty() { return roll; }
        public SimpleStringProperty roomProperty() { return room; }
    }
}
