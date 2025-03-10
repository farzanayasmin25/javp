package controllers;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

    public class homeController implements Initializable {

        @FXML
        private AnchorPane leftPane;

        @FXML
        private Button menuButton;

        // Buttons in the left pane
        @FXML
        private Button home, about, admission, notice, contact,admin,studentlogin;

        // Buttons in the right pane
        @FXML
        private Button abouthostel, admissionprocess, noticeboard, contactus, moreinfo;

        private boolean isPaneVisible = true;

        @Override
        public void initialize(URL location, ResourceBundle resources) {
            // Set up menu button action
            menuButton.setOnAction(_ -> toggleLeftPane());

            // Set up event handlers for left pane buttons
            home.setOnAction(_ -> handleButtonAction("Home"));
            about.setOnAction(_ -> handleButtonAction("About"));
            admission.setOnAction(_ -> loadAdmissionForm()); // Load admission form
            notice.setOnAction(_ -> gotonoticeboard());
            contact.setOnAction(_ -> contactus());
            admin.setOnAction(_ -> gotoadminlogin());
            studentlogin.setOnAction(_ -> gotostudentlogin());
            // Set up event handlers for right pane buttons
            abouthostel.setOnAction(_ -> handleButtonAction("About Our Hostel"));
            admissionprocess.setOnAction(_ -> loadAdmissionForm()); // Load admission form
            noticeboard.setOnAction(_ -> gotonoticeboard());
            contactus.setOnAction(_ -> contactus());
            moreinfo.setOnAction(_ -> handleButtonAction("More Info"));
        }

        private void toggleLeftPane() {
            TranslateTransition transition = new TranslateTransition(Duration.seconds(0.5), leftPane);
            if (isPaneVisible) {
                // Slide out to the left
                transition.setToX(-leftPane.getWidth());
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

            // Example: Load new FXML pages for each section (optional)
            try {
                // Load the corresponding FXML file for the button
                AnchorPane newPage = FXMLLoader.load(getClass().getResource("/views/" + buttonName.toLowerCase().replace(" ", "") + ".fxml"));
                Stage stage = new Stage();
                stage.setScene(new Scene(newPage));
                stage.setTitle(buttonName);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error loading " + buttonName + " page.");
            }
        }
        @FXML
        private void contactus() {
            try {

                Stage oldstage=(Stage) home.getScene().getWindow();
                oldstage.close();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Contact.fxml"));
                Parent root = loader.load();
                Stage stage = new Stage();
                stage.setTitle("Contact Us");
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error loading contact portal.");
            }
        }

        private void gotoadminlogin() {
            try {

                Stage oldstage=(Stage) home.getScene().getWindow();
                oldstage.close();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/adminlogin.fxml"));
                Parent root = loader.load();
                Stage stage = new Stage();
                stage.setTitle("Admin Login Portal");
                stage.setScene(new Scene(root, 600, 400));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error loading login portal.");
            }
        }
        @FXML
        private void gotostudentlogin() {
            try {

                Stage oldstage=(Stage) home.getScene().getWindow();
                oldstage.close();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/login.fxml"));
                Parent root = loader.load();
                Stage stage = new Stage();
                stage.setTitle("Student Login Portal");
                stage.setScene(new Scene(root, 600, 400));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error loading login portal.");
            }
        }
        @FXML
        private void gotonoticeboard() {
            try {

                Stage oldstage=(Stage) home.getScene().getWindow();
                oldstage.close();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/HomepageNoticeBoard.fxml"));
                Parent root = loader.load();
                Stage stage = new Stage();
                stage.setTitle("Notice Board");
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error loading login portal.");
            }
        }
        private void loadAdmissionForm() {
            try {

                Stage oldstage=(Stage) home.getScene().getWindow();
                oldstage.close();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/admission1.fxml"));
                Parent root = loader.load();
                Stage stage = new Stage();
                stage.setTitle("Admission Form");
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error loading admission form.");
            }
        }
    }

