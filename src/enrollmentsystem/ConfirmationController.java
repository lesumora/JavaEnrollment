/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package enrollmentsystem;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author hp
 */
public class ConfirmationController implements Initializable {

    @FXML
    private Text tSubject1, tSubject2, tSubject3, tSubject4, tSubject5, tSubject6, tSubject7, tSubject8,
            tSubject9, tSubject10, tSubject11, tSubject12;

    private Text[] subjectTexts;
    double x = 0, y = 0;

    HashMap<String, String> bsuCourse = new HashMap<>();
    SubjectManager subjectManager = new SubjectManager();

    @FXML
    private Text tAcadYear, tCampus, tAcademicProgram, tCurriculum, tYearLevel, tSection;
    @FXML
    private VBox paneSubject;

    @FXML
    private Button btnContinue;
    @FXML
    private Button btnBack;
    @FXML
    private Button btnExit;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        subjectTexts = new Text[]{tSubject1, tSubject2, tSubject3, tSubject4, tSubject5,
            tSubject6, tSubject7, tSubject8, tSubject9, tSubject10, tSubject11, tSubject12};
        bsuCourse.put("BSIT", "BACHELOR OF SCIENCE IN INFORMATION TECHNOLOGY");
        //bsuCourse.put("BSA", "BACHELOR OF SCIENCE IN ACCOUNTANCY");
        paneSubject.setSpacing(6);

        tCampus.setText(UserSession.getInstance().getUserCampus());

        String userCourse = UserSession.getInstance().getCourse();
        String value = bsuCourse.get(userCourse);
        if (bsuCourse.containsKey(userCourse)) {
            tAcademicProgram.setText(value);
        } else {
            System.out.println("Error");
        }

        tCurriculum.setText(UserSession.getInstance().getCurriculum());

        tYearLevel.setText("" + UserSession.getInstance().getYearLevel());

        tSection.setText(UserSession.getInstance().getSection());

        HashMap<String, String> enrolledSubjects = UserSession.getInstance().getEnrolledSubjects();
        int yearLevel = UserSession.getInstance().getYearLevel();
        int subjectCount = 0;
        switch (yearLevel) {
            case 1:
                subjectCount = 6;
                for (int i = 1; i <= subjectCount; i++) {
                    Text currentSubject = subjectTexts[i - 1];
                    String subjectCode = "IT10" + i;

                    if (enrolledSubjects.containsKey(subjectCode)) {
                        currentSubject.setText("" + subjectManager.getSubjectCode(subjectCode));
                    } else {
                        paneSubject.getChildren().remove(currentSubject);
                    }
                }
                break;
            case 2:
                subjectCount = 10;
                for (int i = 1; i <= subjectCount; i++) {
                    Text currentSubject = subjectTexts[i - 1];
                    String subjectCode = "IT20" + i;

                    if (enrolledSubjects.containsKey(subjectCode)) {
                        currentSubject.setText("" + subjectManager.getSubjectCode(subjectCode));
                    } else {
                        paneSubject.getChildren().remove(currentSubject);
                    }
                }
                break;
            case 3:
                subjectCount = 12;
                for (int i = 1; i <= subjectCount; i++) {
                    Text currentSubject = subjectTexts[i - 1];
                    String subjectCode = "IT3" + (i < 10 ? "0" + i : i);

                    if (enrolledSubjects.containsKey(subjectCode)) {
                        currentSubject.setText("" + subjectManager.getSubjectCode(subjectCode));
                    } else {
                        paneSubject.getChildren().remove(currentSubject);
                    }
                }
                break;
            case 4:
                subjectCount = 4;
                for (int i = 1; i <= subjectCount; i++) {
                    Text currentSubject = subjectTexts[i - 1];
                    String subjectCode = "IT40" + i;

                    if (enrolledSubjects.containsKey(subjectCode)) {
                        currentSubject.setText("" + subjectManager.getSubjectCode(subjectCode));
                    } else {
                        paneSubject.getChildren().remove(currentSubject);
                    }
                }
                break;
            default:
                System.out.println("Error");
        }
        for (int i = subjectCount; i < 12; i++) {
            paneSubject.getChildren().remove(subjectTexts[i]);
        }
    }

    @FXML
    private void handleButtonAction(ActionEvent event) {
        if (event.getSource() == btnContinue || event.getSource() == btnBack) {
            String fxmlFile = (event.getSource() == btnContinue) ? "Dashboard.fxml" : "Advising.fxml";

            try {
                // Close the current confirmation window
                Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                currentStage.close();

                // Open the specified window
                Stage stage = new Stage();
                Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.initStyle(StageStyle.UNDECORATED);

                root.setOnMousePressed(evt -> {
                    x = evt.getSceneX();
                    y = evt.getSceneY();
                });
                root.setOnMouseDragged(evt -> {
                    stage.setX(evt.getScreenX() - x);
                    stage.setY(evt.getScreenY() - y);
                });
                stage.show();
                UserSession.getInstance().setIsEnrolled(true);
            } catch (IOException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (event.getSource() == btnExit){
            Platform.exit();
        }
    }

}
