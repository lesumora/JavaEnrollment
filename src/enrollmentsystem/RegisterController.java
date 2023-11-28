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
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author hp
 */
public class RegisterController implements Initializable {

    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    @FXML
    private Label label;
    @FXML
    private TextField tfUsername, tfID, tfLastName, tfFirstName;
    @FXML
    private PasswordField pfPassword, pfConfirmPassword;
    @FXML
    private Button btnRegister, btnBack;
    @FXML
    private ComboBox<String> cbYearLevel, cbCourse;
    @FXML
    private CheckBox cbScholar;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cbYearLevel.setItems(FXCollections.observableArrayList("1", "2", "3", "4"));
        cbCourse.setItems(FXCollections.observableArrayList("BSIT"));
    }

    @FXML
    private void handleButtonAction(ActionEvent event) {
        final String DB_URL = "jdbc:mysql://localhost:3306/enrollment?zeroDateTimeBehavior=CONVERT_TO_NULL";
        final String USERNAME = "root";
        final String PASSWORD = "";

        try {
            connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            showError("Error", "An error occured LIL bro", e);
        }

        //Back Button
        if (event.getSource() == btnBack) {
            try {
                Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                currentStage.close();

                // Open the registration window
                Stage registerStage = new Stage();
                Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
                Scene scene = new Scene(root);
                registerStage.setScene(scene);
                registerStage.show();
            } catch (IOException ex) {
                Logger.getLogger(RegisterController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        //Register Button
        if (event.getSource() == btnRegister) {
            String ID = tfID.getText();
            String lastName = tfLastName.getText();
            String firstName = tfFirstName.getText();
            String username = tfUsername.getText();
            String password = pfPassword.getText();
            String confirmPassword = pfConfirmPassword.getText();
            String yearLevel = cbYearLevel.getValue();
            String course = cbCourse.getValue();

            if (ID.isEmpty() || lastName.isEmpty() || firstName.isEmpty() || username.isEmpty()
                    || password.isEmpty() || confirmPassword.isEmpty() || yearLevel == null || course == null) {
                showMessage("Try again", "Please fill in all fields.");
                return;
            }
            
            // Check username and password length
            if (username.length() != 6 || password.length() != 6) {
                showMessage("Invalid Input", "Username and password must be 6 characters long.");
                return;
            }

            if (!password.equals(confirmPassword)) {
                showMessage("Try again", "Password do not match.");
                return;
            }

            String SQLQuery = "INSERT INTO user_info (id, last_name, first_name, username, password, "
                    + "year_level, course, bulsu_scholar) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try {
                preparedStatement = connection.prepareStatement(SQLQuery);

                // Set values for common fields
                preparedStatement.setString(1, ID);
                preparedStatement.setString(2, lastName);
                preparedStatement.setString(3, firstName);
                preparedStatement.setString(4, username);
                preparedStatement.setString(5, password);
                preparedStatement.setString(6, yearLevel);
                preparedStatement.setString(7, course);

                // Set value for bulsu_scholar based on checkbox selection
                if (cbScholar.isSelected()) {
                    preparedStatement.setString(8, "YES");
                } else {
                    preparedStatement.setString(8, "NO");
                }

                preparedStatement.execute();
            } catch (Exception e) {
                showError("Oops!", "An error occurred lil brodie.", e);
            }

            showMessage("Success!", "New student registered.");
            try {
                Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                currentStage.close();

                // Open the registration window
                Stage registerStage = new Stage();
                Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
                Scene scene = new Scene(root);
                registerStage.setScene(scene);
                registerStage.show();
            } catch (IOException ex) {
                Logger.getLogger(RegisterController.class.getName()).log(Level.SEVERE, null, ex);
            }
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
}
