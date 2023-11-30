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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author hp
 */
public class AdvisingController implements Initializable {

    SubjectManager subjectManager = new SubjectManager();
    CheckBox[] checkBoxes;
    Text[] subjectTexts;
    double x = 0, y = 0;

    private static final String[] SECTION_NAMES = {"SECTION - A", "SECTION - B", "SECTION - C", "SECTION - D"};

    @FXML
    private CheckBox cbSubject1, cbSubject2, cbSubject3, cbSubject4, cbSubject5, cbSubject6, cbSubject7, cbSubject8,
            cbSubject9, cbSubject10, cbSubject11, cbSubject12;
    @FXML
    private Text tSubject1, tSubject2, tSubject3, tSubject4, tSubject5, tSubject6, tSubject7, tSubject8,
            tSubject9, tSubject10, tSubject11, tSubject12;
    @FXML
    private Button btnContinue, btnBack;
    @FXML
    private ComboBox<String> cbSection;
    @FXML
    private Button btnExit;
    @FXML
    private Pane advisingPane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cbSection.setItems(FXCollections.observableArrayList(SECTION_NAMES));
        int yearLevel = UserSession.getInstance().getYearLevel();

        checkBoxes = new CheckBox[]{cbSubject1, cbSubject2, cbSubject3, cbSubject4, cbSubject5,
            cbSubject6, cbSubject7, cbSubject8, cbSubject9, cbSubject10, cbSubject11, cbSubject12};

        subjectTexts = new Text[]{tSubject1, tSubject2, tSubject3, tSubject4, tSubject5,
            tSubject6, tSubject7, tSubject8, tSubject9, tSubject10, tSubject11, tSubject12};

        int visibleSubjectCount = 0;

        switch (yearLevel) {
            case 1:
                setSubjects(getYear1Subjects());
                visibleSubjectCount = 6;
                break;
            case 2:
                setSubjects(getYear2Subjects());
                visibleSubjectCount = 10;
                break;
            case 3:
                setSubjects(getYear3Subjects());
                visibleSubjectCount = 12;
                break;
            case 4:
                setSubjects(getYear4Subjects());
                visibleSubjectCount = 4;
                break;
            default:
                System.out.println("Invalid year level");
                break;
        }

        // Hide unused checkboxes and text fields
        for (int i = visibleSubjectCount; i < 12; i++) {
            checkBoxes[i].setVisible(false);
            subjectTexts[i].setVisible(false);
        }
    }

    @FXML
    private void handleButtonAction(ActionEvent event) {
        if (event.getSource() == btnBack) {
            // User clicked on btnBack, go back without checking ComboBox value
            try {
                Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                currentStage.close();

                // Open the previous window
                Stage previousStage = new Stage();
                Parent root = FXMLLoader.load(getClass().getResource("Enrollment.fxml"));
                Scene scene = new Scene(root);
                previousStage.setScene(scene);
                previousStage.initStyle(StageStyle.UNDECORATED);

                root.setOnMousePressed(evt -> {
                    x = evt.getSceneX();
                    y = evt.getSceneY();
                });
                root.setOnMouseDragged(evt -> {
                    previousStage.setX(evt.getScreenX() - x);
                    previousStage.setY(evt.getScreenY() - y);
                });
                previousStage.show();
            } catch (IOException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else if (cbSection.getValue() != null && event.getSource() == btnContinue) {
            // User clicked on btnContinue and ComboBox has a selected value
            try {
                // Close the current confirmation window
                Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                currentStage.close();

                // Open the specified window
                Stage confirmationStage = new Stage();
                Parent root = FXMLLoader.load(getClass().getResource("Confirmation.fxml"));
                Scene scene = new Scene(root);
                confirmationStage.setScene(scene);
                confirmationStage.initStyle(StageStyle.UNDECORATED);

                root.setOnMousePressed(evt -> {
                    x = evt.getSceneX();
                    y = evt.getSceneY();
                });
                root.setOnMouseDragged(evt -> {
                    confirmationStage.setX(evt.getScreenX() - x);
                    confirmationStage.setY(evt.getScreenY() - y);
                });
                confirmationStage.show();
            } catch (IOException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            // User clicked on btnContinue, but ComboBox is null
            showMessage("OOPS!", "Please choose a section.");
        }
        
        if (event.getSource() == btnExit){
            Platform.exit();
        }
    }

    @FXML
    private void checkBoxSave(ActionEvent event) {
        UserSession userSession = UserSession.getInstance();
        int yearLevel = userSession.getYearLevel();

        SubjectManager.Subject[] subjects;

        switch (yearLevel) {
            case 1:
                subjects = getYear1Subjects();
                break;
            case 2:
                subjects = getYear2Subjects();
                break;
            case 3:
                subjects = getYear3Subjects();
                break;
            case 4:
                subjects = getYear4Subjects();
                break;
            default:
                System.out.println("Invalid year level");
                return;
        }

        for (int i = 0; i < checkBoxes.length; i++) {
            CheckBox checkBox = checkBoxes[i];

            if (checkBox.isSelected()) {
                userSession.enrollSubject(subjects[i].getSubjectCode());
            } else {
                userSession.unenrollSubject(subjects[i].getSubjectCode());
            }
        }
    }

    @FXML
    private void sectionSelect(ActionEvent event) {
        String selectedSection = cbSection.getValue();
        UserSession.getInstance().setSection(selectedSection);
    }

    private void setSubjects(SubjectManager.Subject[] subjects) {
        for (int i = 0; i < subjects.length; i++) {
            subjectTexts[i].setText(subjects[i].toString());
        }
    }

    private SubjectManager.Subject[] getYear1Subjects() {
        return new SubjectManager.Subject[]{
            subjectManager.getSubjectCode("IT101"), subjectManager.getSubjectCode("IT102"),
            subjectManager.getSubjectCode("IT103"), subjectManager.getSubjectCode("IT104"),
            subjectManager.getSubjectCode("IT105"), subjectManager.getSubjectCode("IT106")
        };
    }

    private SubjectManager.Subject[] getYear2Subjects() {
        return new SubjectManager.Subject[]{
            subjectManager.getSubjectCode("IT201"), subjectManager.getSubjectCode("IT202"),
            subjectManager.getSubjectCode("IT203"), subjectManager.getSubjectCode("IT204"),
            subjectManager.getSubjectCode("IT205"), subjectManager.getSubjectCode("IT206"),
            subjectManager.getSubjectCode("IT207"), subjectManager.getSubjectCode("IT208"),
            subjectManager.getSubjectCode("IT209"), subjectManager.getSubjectCode("IT210")
        };
    }

    private SubjectManager.Subject[] getYear3Subjects() {
        return new SubjectManager.Subject[]{
            subjectManager.getSubjectCode("IT301"), subjectManager.getSubjectCode("IT302"),
            subjectManager.getSubjectCode("IT303"), subjectManager.getSubjectCode("IT304"),
            subjectManager.getSubjectCode("IT305"), subjectManager.getSubjectCode("IT306"),
            subjectManager.getSubjectCode("IT307"), subjectManager.getSubjectCode("IT308"),
            subjectManager.getSubjectCode("IT309"), subjectManager.getSubjectCode("IT310"),
            subjectManager.getSubjectCode("IT311"), subjectManager.getSubjectCode("IT312")
        };
    }

    private SubjectManager.Subject[] getYear4Subjects() {
        return new SubjectManager.Subject[]{
            subjectManager.getSubjectCode("IT401"), subjectManager.getSubjectCode("IT402"),
            subjectManager.getSubjectCode("IT403"), subjectManager.getSubjectCode("IT404")
        };
    }

    private void showMessage(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
