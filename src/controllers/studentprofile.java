package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import database.DatabaseConnection; // Your MySQL connection class

public class studentprofile {

    @FXML private ImageView profileImage;
    @FXML private Label studentName;
    @FXML private Label studentRoll;
    @FXML private Label studentRegNo;
    @FXML private Label mobileNumber;
    @FXML private Label email;
    @FXML private ImageView advisorImage;
    @FXML private Label advisorName;
    @FXML private Label advisorPhone;
    @FXML private Label batch;
    @FXML private Label classSection;
    @FXML private Label creditGroup;
    @FXML private Label fatherName;
    @FXML private Label motherName;
    @FXML private Label dob;
    @FXML private Label gender;
    @FXML private Label syllabus;

    public void initialize() {
        loadProfileData("202314004"); // Example Roll Number
    }

    private void loadProfileData(String studentRollNumber) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM students WHERE student_roll = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, studentRollNumber);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                studentName.setText(rs.getString("student_name"));
                studentRoll.setText("Roll: " + rs.getString("student_roll"));
                studentRegNo.setText("Reg No: " + rs.getString("registration_no"));
                mobileNumber.setText("Phone: " + rs.getString("mobile_number"));
                email.setText("Email: " + rs.getString("email"));
                batch.setText(rs.getString("batch"));
                classSection.setText(rs.getString("class_section"));
                creditGroup.setText(rs.getString("credit_group"));
                fatherName.setText(rs.getString("father_name"));
                motherName.setText(rs.getString("mother_name"));
                dob.setText(rs.getString("dob"));
                gender.setText(rs.getString("gender"));
                syllabus.setText(rs.getString("syllabus"));

                // Load images (assuming image paths are stored in DB)
                profileImage.setImage(new Image(rs.getString("profile_image")));
                advisorImage.setImage(new Image(rs.getString("advisor_image")));

                // Load advisor info
                advisorName.setText(rs.getString("advisor_name"));
                advisorPhone.setText(rs.getString("advisor_phone"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
