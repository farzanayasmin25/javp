package controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import javafx.event.ActionEvent;

public class AdminController {
    @FXML
    private AnchorPane leftPane; // Sidebar Pane

    public void initialize() {
        animateBackground();
    }

    private void animateBackground() {
        TranslateTransition transition = new TranslateTransition(Duration.seconds(5), leftPane);
        transition.setFromX(-50); // Move left
        transition.setToX(50); // Move right
        transition.setAutoReverse(true); // Move back and forth
        transition.setCycleCount(TranslateTransition.INDEFINITE); // Infinite loop
        transition.play();
    }

    public void goToManageStudents(ActionEvent event) throws IOException {
        openNewPage(event, "/views/manage_students.fxml", "Manage Students");
    }

    public void goToRoomAllocation(ActionEvent event) throws IOException {
        openNewPage(event, "/views/adminroomallocation.fxml", "Room Allocation");
    }

    public void goToBilling(ActionEvent event) throws IOException {
        openNewPage(event, "/views/adminbilling.fxml", "Billing System");
    }

    public void goToComplaints(ActionEvent event) throws IOException {
        openNewPage(event, "/views/admincomplaint.fxml", "Complaint Management");
    }

    public void goToReports(ActionEvent event) throws IOException {
        openNewPage(event, "/views/reports.fxml", "Reports");
    }

    public void gotonoticeboard(ActionEvent event) throws IOException {
        openNewPage(event, "/views/adminnotice.fxml", "Notice Management");
    }

    public void handleLogout(ActionEvent event) throws IOException {
        openNewPage(event, "/views/adminlogin.fxml", "Admin Login Portal");
    }

    private void openNewPage(ActionEvent event, String fxmlPath, String title) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlPath));
        Stage newStage = new Stage();
        newStage.setScene(new Scene(fxmlLoader.load()));
        newStage.setTitle(title);
        newStage.show();

        // Close current window
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }
}
