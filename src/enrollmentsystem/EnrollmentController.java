/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package enrollmentsystem;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author hp
 */
public class EnrollmentController implements Initializable {

    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    
    HashMap<String, String> bsuCourse = new HashMap<>();
    double x = 0, y = 0;

    @FXML
    private ComboBox<String> cbCampus;
    @FXML
    private TextField tfProgram;
    @FXML
    private TextField tfCurriculum;
    @FXML
    private TextField tfIncomingLevel;
    @FXML
    private TextField tfBalance;
    @FXML
    private Button btnContinue;
    @FXML
    private Button btnBack;
    @FXML
    private Button btnExit;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        final String DB_URL = "jdbc:mysql://localhost:3306/enrollment?zeroDateTimeBehavior=CONVERT_TO_NULL";
        final String USERNAME = "root";
        final String PASSWORD = "";

        try {
            connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            showError("Error", "An error occured LIL bro", e);
        }

        cbCampus.setItems(FXCollections.observableArrayList(
                "BulSU - Main", "Sarmiento", "San Rafael", "Bustos"));

        bsuCourse.put("BSIT", "BACHELOR OF SCIENCE IN INFORMATION TECHNOLOGY");
        bsuCourse.put("BSA", "BACHELOR OF SCIENCE IN ACCOUNTANCY");

        String userCourse = UserSession.getInstance().getCourse();
        String value = bsuCourse.get(userCourse);
        if (bsuCourse.containsKey(userCourse)) {
            tfProgram.setText(value);
        } else {
            tfProgram.setText("error");
        }

        tfCurriculum.setText(userCourse + " (2024-2025)");
        UserSession.getInstance().setCurriculum(userCourse + " (2024-2025)");

        switch (UserSession.getInstance().getYearLevel()) {
            case 1:
                tfIncomingLevel.setText("1st Year");
                break;
            case 2:
                tfIncomingLevel.setText("2nd Year");
                break;
            case 3:
                tfIncomingLevel.setText("3rd Year");
                break;
            case 4:
                tfIncomingLevel.setText("4th Year");
                break;
            default:
                tfIncomingLevel.setText("Error");
        }

        if (UserSession.getInstance().getScholar().equals("YES")) {
            tfBalance.setText("0.00");
        } else {
            tfBalance.setText("5,145");
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
                Parent root = FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
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
        } else if (cbCampus.getValue() != null && event.getSource() == btnContinue) {
            // User clicked on btnContinue and ComboBox has a selected value
            try {
                // Close the current confirmation window
                Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                currentStage.close();

                // Open the specified window
                Stage advisingStage = new Stage();
                Parent root = FXMLLoader.load(getClass().getResource("Advising.fxml"));
                Scene scene = new Scene(root);
                advisingStage.setScene(scene);
                advisingStage.initStyle(StageStyle.UNDECORATED);

                root.setOnMousePressed(evt -> {
                    x = evt.getSceneX();
                    y = evt.getSceneY();
                });
                root.setOnMouseDragged(evt -> {
                    advisingStage.setX(evt.getScreenX() - x);
                    advisingStage.setY(evt.getScreenY() - y);
                });
                advisingStage.show();
            } catch (IOException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            // User clicked on btnContinue, but ComboBox is null
            showMessage("OOPS!", "Please choose a campus.");
        }
        
        if (event.getSource() == btnExit){
            Platform.exit();
        }
    }

    private void showMessage(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showError(String title, String header, Exception exception) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(exception.getMessage());

        // You can also print the stack trace to the console or log it.
        exception.printStackTrace();

        alert.showAndWait();
    }

    @FXML
    private void comboBoxClicked(ActionEvent event) {
        String selectedCampus = cbCampus.getValue();
        System.out.println("Selected Campus: " + selectedCampus);
        UserSession.getInstance().setUserCampus(selectedCampus);
    }
}
