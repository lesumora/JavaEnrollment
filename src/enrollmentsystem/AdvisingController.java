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
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author hp
 */
public class AdvisingController implements Initializable {

    SubjectManager subjectManager = new SubjectManager();
    SubjectManager.Subject year2_1 = subjectManager.getSubjectCode("IT201");
    SubjectManager.Subject year2_2 = subjectManager.getSubjectCode("IT202");
    SubjectManager.Subject year2_3 = subjectManager.getSubjectCode("IT203");
    SubjectManager.Subject year2_4 = subjectManager.getSubjectCode("IT204");
    SubjectManager.Subject year2_5 = subjectManager.getSubjectCode("IT205");
    SubjectManager.Subject year2_6 = subjectManager.getSubjectCode("IT206");
    SubjectManager.Subject year2_7 = subjectManager.getSubjectCode("IT207");
    SubjectManager.Subject year2_8 = subjectManager.getSubjectCode("IT208");
    SubjectManager.Subject year2_9 = subjectManager.getSubjectCode("IT209");
    SubjectManager.Subject year2_10 = subjectManager.getSubjectCode("IT210");

    SubjectManager.Subject year4_1 = subjectManager.getSubjectCode("IT401");
    SubjectManager.Subject year4_2 = subjectManager.getSubjectCode("IT402");
    SubjectManager.Subject year4_3 = subjectManager.getSubjectCode("IT403");
    SubjectManager.Subject year4_4 = subjectManager.getSubjectCode("IT404");

    CheckBox[] checkBoxes;

    @FXML
    private CheckBox cbSubject1, cbSubject2, cbSubject3, cbSubject4, cbSubject5, cbSubject6, cbSubject7, cbSubject8,
            cbSubject9, cbSubject10, cbSubject11, cbSubject12;
    @FXML
    private Text tSubject1, tSubject2, tSubject3, tSubject4, tSubject5, tSubject6, tSubject7, tSubject8,
            tSubject9, tSubject10, tSubject11, tSubject12;
    @FXML
    private Button btnContinue;
    @FXML
    private ComboBox<String> cbSection;
    @FXML
    private Button btnBack;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //String course = UserSession.getInstance().getCourse();
        cbSection.setItems(FXCollections.observableArrayList(
                "SECTION A", "SECTION B", "SECTION C", "SECTION D"));
        int yearLevel = UserSession.getInstance().getYearLevel();
        switch (yearLevel) {
            case 1:
                cbSubject7.setVisible(false);
                cbSubject8.setVisible(false);
                cbSubject9.setVisible(false);
                cbSubject10.setVisible(false);
                cbSubject11.setVisible(false);
                cbSubject12.setVisible(false);
                tSubject7.setVisible(false);
                tSubject8.setVisible(false);
                tSubject9.setVisible(false);
                tSubject10.setVisible(false);
                tSubject11.setVisible(false);
                tSubject12.setVisible(false);
                break;
            case 2:
                tSubject1.setText(year2_1.toString());
                tSubject2.setText(year2_2.toString());
                tSubject3.setText(year2_3.toString());
                tSubject4.setText(year2_4.toString());
                tSubject5.setText(year2_5.toString());
                tSubject6.setText(year2_6.toString());
                tSubject7.setText(year2_7.toString());
                tSubject8.setText(year2_8.toString());
                tSubject9.setText(year2_9.toString());
                tSubject10.setText(year2_10.toString());

                cbSubject11.setVisible(false);
                cbSubject12.setVisible(false);
                tSubject11.setVisible(false);
                tSubject12.setVisible(false);
                break;
            case 3:
                break;
            case 4:
                tSubject1.setText(year4_1.toString());
                tSubject2.setText(year4_2.toString());
                tSubject3.setText(year4_3.toString());
                tSubject4.setText(year4_4.toString());

                if (cbSubject1.isSelected()) {
                    UserSession.getInstance().enrollSubject(year4_1.toString());
                } else if (cbSubject2.isSelected()) {
                    UserSession.getInstance().enrollSubject(year4_2.toString());
                } else if (cbSubject3.isSelected()) {
                    UserSession.getInstance().enrollSubject(year4_3.toString());
                } else if (cbSubject4.isSelected()) {
                    UserSession.getInstance().enrollSubject(year4_4.toString());
                }

                cbSubject5.setVisible(false);
                cbSubject6.setVisible(false);
                cbSubject7.setVisible(false);
                cbSubject8.setVisible(false);
                cbSubject9.setVisible(false);
                cbSubject10.setVisible(false);
                cbSubject11.setVisible(false);
                cbSubject12.setVisible(false);
                tSubject5.setVisible(false);
                tSubject6.setVisible(false);
                tSubject7.setVisible(false);
                tSubject8.setVisible(false);
                tSubject9.setVisible(false);
                tSubject10.setVisible(false);
                tSubject11.setVisible(false);
                tSubject12.setVisible(false);
                break;
            default:
                System.out.println("Error");
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
                Stage registerStage = new Stage();
                Parent root = FXMLLoader.load(getClass().getResource("Confirmation.fxml"));
                Scene scene = new Scene(root);
                registerStage.setScene(scene);
                registerStage.show();
            } catch (IOException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            // User clicked on btnContinue, but ComboBox is null
            showMessage("OOPS!", "Please choose a section.");
        }
    }

    @FXML
    private void checkBoxSave(ActionEvent event) {
        UserSession userSession = UserSession.getInstance();
        checkBoxes = new CheckBox[]{
            cbSubject1, cbSubject2, cbSubject3, cbSubject4, cbSubject5,
            cbSubject6, cbSubject7, cbSubject8, cbSubject9, cbSubject10
        };

        SubjectManager.Subject[] subjects = {
            year2_1, year2_2, year2_3, year2_4, year2_5,
            year2_6, year2_7, year2_8, year2_9, year2_10
        };

        for (int i = 0; i < checkBoxes.length; i++) {
            CheckBox checkBox = checkBoxes[i];
            SubjectManager.Subject subject = subjects[i];

            if (checkBox.isSelected()) {
                userSession.enrollSubject(subject.getSubjectCode());
            } else {
                userSession.unenrollSubject(subject.getSubjectCode());
            }
        }
    }

    @FXML
    private void sectionSelect(ActionEvent event) {
        String selectedSection = cbSection.getValue();
        UserSession.getInstance().setSection(selectedSection);
    }

    private void showMessage(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
