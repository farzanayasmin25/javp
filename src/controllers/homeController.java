package controllers;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class homeController implements Initializable {

    @FXML
    private VBox leftPane;

    @FXML
    private Button menuButton;

    // Buttons in the left pane
    @FXML
    private Button home, about, admission, notice, contact, admin, studentlogin;

    // Buttons in the right pane
    @FXML
    private Button abouthostel, admissionprocess, noticeboard, contactus, moreinfo;

    private boolean isPaneVisible = true;
    private double leftPaneWidth;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Store the original width of the left pane
        leftPaneWidth = leftPane.getPrefWidth();

        // Set up menu button action
        menuButton.setOnAction(_ -> toggleLeftPane());

        // Set up event handlers for left pane buttons
        home.setOnAction(_ -> handleButtonAction("Home"));
        about.setOnAction(_ -> handleButtonAction("About"));
        admission.setOnAction(_ -> loadAdmissionForm());
        notice.setOnAction(_ -> gotonoticeboard());
        contact.setOnAction(_ -> contactus());
        admin.setOnAction(_ -> gotoadminlogin());
        studentlogin.setOnAction(_ -> gotostudentlogin());

        // Set up event handlers for right pane buttons
        abouthostel.setOnAction(_ -> handleButtonAction("About Our Hostel"));
        admissionprocess.setOnAction(_ -> loadAdmissionForm());
        noticeboard.setOnAction(_ -> gotonoticeboard());
        contactus.setOnAction(_ -> contactus());
        moreinfo.setOnAction(_ -> handleButtonAction("More Info"));
    }

    private void toggleLeftPane() {
        TranslateTransition transition = new TranslateTransition(Duration.seconds(0.5), leftPane);
        if (isPaneVisible) {
            // Slide out to the left
            transition.setToX(-leftPaneWidth);
            isPaneVisible = false;
        } else {
            // Slide in from the left
            transition.setToX(0);
            isPaneVisible = true;
        }
        transition.play();
    }

    private void handleButtonAction(String buttonName) {
        System.out.println(buttonName + " button clicked!");

        try {
            // Load the corresponding FXML file for the button
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/" + buttonName.toLowerCase().replace(" ", "") + ".fxml"));
            Parent root = loader.load();

            // Get current stage and set the new scene
            Stage currentStage = (Stage) home.getScene().getWindow();
            currentStage.setTitle(buttonName);

            // Create a responsive scene that maintains the window size
            Scene scene = new Scene(root);
            currentStage.setScene(scene);

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading " + buttonName + " page.");
        }
    }

    @FXML
    private void contactus() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Contact.fxml"));
            Parent root = loader.load();

            // Get current stage
            Stage currentStage = (Stage) home.getScene().getWindow();
            currentStage.setTitle("Contact Us");

            // Set the new scene on the existing stage
            Scene scene = new Scene(root);
            currentStage.setScene(scene);

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading contact portal.");
        }
    }

    private void gotoadminlogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/adminlogin.fxml"));
            Parent root = loader.load();

            // Get current stage
            Stage currentStage = (Stage) home.getScene().getWindow();
            currentStage.setTitle("Admin Login Portal");

            // Set the new scene on the existing stage
            Scene scene = new Scene(root);
            currentStage.setScene(scene);

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading login portal.");
        }
    }

    @FXML
    private void gotostudentlogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/login.fxml"));
            Parent root = loader.load();

            // Get current stage
            Stage currentStage = (Stage) home.getScene().getWindow();
            currentStage.setTitle("Student Login Portal");

            // Set the new scene on the existing stage
            Scene scene = new Scene(root);
            currentStage.setScene(scene);

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading login portal.");
        }
    }

    @FXML
    private void gotonoticeboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/HomepageNoticeBoard.fxml"));
            Parent root = loader.load();

            // Get current stage
            Stage currentStage = (Stage) home.getScene().getWindow();
            currentStage.setTitle("Notice Board");

            // Set the new scene on the existing stage
            Scene scene = new Scene(root);
            currentStage.setScene(scene);

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading notice board.");
        }
    }

    private void loadAdmissionForm() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/admission1.fxml"));
            Parent root = loader.load();

            // Get current stage
            Stage currentStage = (Stage) home.getScene().getWindow();
            currentStage.setTitle("Admission Form");

            // Set the new scene on the existing stage
            Scene scene = new Scene(root);
            currentStage.setScene(scene);

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading admission form.");
        }
    }
}