package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Student;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class managestudent {

    @FXML
    private TableView<Student> studentTable;

    private ObservableList<Student> studentList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Set up the table columns
        studentTable.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("admissionId"));
        studentTable.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("fullName"));
        studentTable.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("departmentName"));
        studentTable.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("rollNo"));
        studentTable.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("reasonForHallSeat"));

        // Load data from the database
        loadStudents();
    }

    private void loadStudents() {
        String query = "SELECT admission_id, full_name, department_name, roll_no, reason_for_hall_seat FROM admissions WHERE reason_for_hall_seat IS NOT NULL";
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/peakage", "username", "password");
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            studentList.clear();
            while (rs.next()) {
                studentList.add(new Student(
                        rs.getInt("admission_id"),
                        rs.getString("full_name"),
                        rs.getString("department_name"),
                        rs.getString("roll_no"),
                        rs.getString("reason_for_hall_seat")
                ));
            }
            studentTable.setItems(studentList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void approveRequest() {
        Student selectedStudent = studentTable.getSelectionModel().getSelectedItem();
        if (selectedStudent != null) {
            updateStatus(selectedStudent.getAdmissionId(), "Approved");
        }
    }

    @FXML
    private void rejectRequest() {
        Student selectedStudent = studentTable.getSelectionModel().getSelectedItem();
        if (selectedStudent != null) {
            updateStatus(selectedStudent.getAdmissionId(), "Rejected");
        }
    }

    private void updateStatus(int admissionId, String status) {
        String query = "UPDATE admissions SET hall_seat_status = ? WHERE admission_id = ?";
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/peakage", "username", "password");
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, status);
            stmt.setInt(2, admissionId);
            stmt.executeUpdate();
            loadStudents(); // Refresh the table
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}