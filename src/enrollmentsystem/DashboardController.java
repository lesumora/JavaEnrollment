/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package enrollmentsystem;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author hp
 */
public class DashboardController implements Initializable {

    @FXML
    private Text tSubject1, tSubject2, tSubject3, tSubject4, tSubject5, tSubject6, tSubject7, tSubject8,
            tSubject9, tSubject10, tSubject11, tSubject12;

    private Text[] subjectText;

    @FXML
    private ComboBox<String> cbStudent;
    @FXML
    private Button btnEnrollment;
    @FXML
    private Label lbWelcome;
    @FXML
    private VBox paneSubject;
    @FXML
    private Pane showPane;
    @FXML
    private Label lbPaneTitle;
    @FXML
    private Button btnLogout;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cbStudent.setItems(FXCollections.observableArrayList("Student Info", "Subject Enrolled"));
        lbWelcome.setText("Welcome, \n" + UserSession.getInstance().getLastName());

        subjectText = new Text[]{tSubject1, tSubject2, tSubject3, tSubject4, tSubject5,
            tSubject6, tSubject7, tSubject8, tSubject9, tSubject10, tSubject11, tSubject12};
        
    }

    @FXML
    private void handleButtonAction(ActionEvent event) {

        if (event.getSource() == btnEnrollment) {
            if (UserSession.getInstance().getIsEnrolled()) {
                showMessage("OOPS!", "User is enrolled.");
            } else {
                try {
                    // Close the current Dashboard window
                    Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    currentStage.close();

                    // Open the Enrollment window
                    Stage dashboardStage = new Stage();
                    Parent root = FXMLLoader.load(getClass().getResource("Enrollment.fxml"));
                    Scene scene = new Scene(root);
                    dashboardStage.setScene(scene);
                    dashboardStage.show();
                } catch (Exception ex) {
                    Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
                    System.out.println(ex);
                }
            }
        }
        
        if (event.getSource() == btnLogout) {
           
                try {
                    //Set UserSession to null
                    UserSession.getInstance().clearSession();
                    // Close the current Dashboard window
                    Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    currentStage.close();

                    // Open the Login window
                    Stage dashboardStage = new Stage();
                    Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
                    Scene scene = new Scene(root);
                    dashboardStage.setScene(scene);
                    dashboardStage.show();
                } catch (Exception ex) {
                    Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
                    System.out.println(ex);
                }
        }
    }

    @FXML
    private void comboBoxSelected(ActionEvent event) {
        lbPaneTitle.setText(cbStudent.getValue());
        VBox vbox = new VBox();
        vbox.setSpacing(13);
        
        if (cbStudent.getValue().equals("Subject Enrolled")) {
            HashMap<String, String> enrolledSubjects = UserSession.getInstance().getEnrolledSubjects();
            SubjectManager subjectManager = new SubjectManager();

            for (int i = 1; i <= 12; i++) {
                String subjectCode = "IT2" + (i < 10 ? "0" + i : i);

                if (enrolledSubjects.containsKey(subjectCode)) {
                    Text currentSubject = new Text("Enrolled Subject - \n" + subjectManager.getSubjectCode(subjectCode));
                    vbox.getChildren().add(currentSubject);
                }
            }
        }

        if (cbStudent.getValue().equals("Student Info")) {
            vbox.getChildren().add(new Text("Student ID : " + UserSession.getInstance().getUserId()));
            vbox.getChildren().add(new Text("Course : " + UserSession.getInstance().getCourse()));
            vbox.getChildren().add(new Text("Last name : " + UserSession.getInstance().getLastName()));
            vbox.getChildren().add(new Text("First Name : " + UserSession.getInstance().getFirstName()));
            vbox.getChildren().add(new Text("Year Level : " + UserSession.getInstance().getYearLevel()));
            vbox.getChildren().add(new Text("Scholarship Status : " + UserSession.getInstance().getScholar()));
        }

        paneSubject.getChildren().setAll(vbox);
        showPane.setVisible(true);
    }

    private void showMessage(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
