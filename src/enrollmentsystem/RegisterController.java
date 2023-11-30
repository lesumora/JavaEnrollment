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
import java.sql.Statement;
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
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author hp
 */
public class RegisterController implements Initializable {

    Connection connection = null;
    Statement statement = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    
    final String DB_URL = "jdbc:mysql://localhost:3306/enrollment?zeroDateTimeBehavior=CONVERT_TO_NULL";
    final String DATABASE_NAME = "enrollment";
    final String USERNAME = "root";
    final String PASSWORD = "";

    double x = 0, y = 0;
    
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
    @FXML
    private Button btnExit;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cbYearLevel.setItems(FXCollections.observableArrayList("1", "2", "3", "4"));
        cbCourse.setItems(FXCollections.observableArrayList("BSIT"));
        
        try {
            // Create database 'enrollment' if not created
            connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            statement = connection.createStatement();
            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS enrollment");
            
            // Connect to the database 'enrollment'
            connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);

            // Create table 'user_info' if not created
            String sql = "CREATE TABLE IF NOT EXISTS user_info ("
                    + "id int(11) NOT NULL AUTO_INCREMENT,"
                    + "last_name TEXT NOT NULL,"
                    + "first_name TEXT NOT NULL,"
                    + "username TEXT NOT NULL,"
                    + "password TEXT NOT NULL,"
                    + "year_level INT NOT NULL,"
                    + "course TEXT NOT NULL,"
                    + "bulsu_scholar TEXT NOT NULL,"
                    + "PRIMARY KEY (id)"
                    + ")";
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            showError("Error", "An error occurred bro", e);
        } finally {
            // Close resources properly in the finally block
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException ex) {
                showError("Error", "An error occurred while closing resources", ex);
            }
        }
    }

    @FXML
    private void handleButtonAction(ActionEvent event) {
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
                registerStage.initStyle(StageStyle.UNDECORATED);

                root.setOnMousePressed(evt -> {
                    x = evt.getSceneX();
                    y = evt.getSceneY();
                });
                root.setOnMouseDragged(evt -> {
                    registerStage.setX(evt.getScreenX() - x);
                    registerStage.setY(evt.getScreenY() - y);
                });
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
            if (username.length() > 6 || password.length() > 6 || confirmPassword.length() > 6) {
                showMessage("Character limit reached!", 
                        "Username and password limit is 6 characters only.");
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
                registerStage.initStyle(StageStyle.UNDECORATED);

                root.setOnMousePressed(evt -> {
                    x = evt.getSceneX();
                    y = evt.getSceneY();
                });
                root.setOnMouseDragged(evt -> {
                    registerStage.setX(evt.getScreenX() - x);
                    registerStage.setY(evt.getScreenY() - y);
                });
                registerStage.setScene(scene);
                registerStage.show();
            } catch (IOException ex) {
                Logger.getLogger(RegisterController.class.getName()).log(Level.SEVERE, null, ex);
            }
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
}
