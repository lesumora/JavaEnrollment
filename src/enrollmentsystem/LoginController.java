/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML2.java to edit this template
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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 *
 * @author hp
 */
public class LoginController implements Initializable {

    Connection connection = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    private int loginAttempts = 0;

    @FXML
    private Button btnLogin, btnRegister;
    @FXML
    private Label label;
    @FXML
    private TextField tfUsername;
    @FXML
    private PasswordField pfPassword;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        final String DB_URL = "jdbc:mysql://localhost:3306/enrollment?zeroDateTimeBehavior=CONVERT_TO_NULL";
        final String USERNAME = "root";
        final String PASSWORD = "";
        

        try {
            connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            showError("Error", "An error occured bro", e);
        }

        if (event.getSource() == btnRegister) {
            try {
                // Close the current login window
                Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                currentStage.close();

                // Open the registration window
                Stage registerStage = new Stage();
                Parent root = FXMLLoader.load(getClass().getResource("Register.fxml"));
                Scene scene = new Scene(root);
                registerStage.setScene(scene);
                registerStage.show();
            } catch (IOException ex) {
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (event.getSource() == btnLogin) {
            String username = tfUsername.getText();
            String password = pfPassword.getText();

            // Check username and password length
            if (username.length() != 6 || password.length() != 6) {
                showMessage("Invalid Input", "Username and password must be 6 characters long.");
                return;
            }

            // Check login attempts
            
            while (loginAttempts < 3) {
                try {
                    String SQLQuery = "SELECT * FROM user_info WHERE username = ? AND password = ?";
                    preparedStatement = connection.prepareStatement(SQLQuery);
                    preparedStatement.setString(1, username);
                    preparedStatement.setString(2, password);
                    resultSet = preparedStatement.executeQuery();

                    if (!resultSet.next()) {
                        loginAttempts++;
                        showMessage("Invalid Credentials", "Invalid username or password. Attempt: "
                                + loginAttempts + " of 3.");
                        return;
                    } else {
                        // Credentials are correct, proceed to dashboard.
                        int userId = resultSet.getInt("id"); // Assuming 'id' is the column name for user ID
                        String lastName = resultSet.getString("last_name");
                        String firstName = resultSet.getString("first_name");
                        String course = resultSet.getString("course");
                        String scholar = resultSet.getString("bulsu_scholar");
                        int yearLevel = resultSet.getInt("year_level");
                        UserSession.getInstance().setUser(username, userId, lastName, firstName,
                                course, scholar, yearLevel);

                        try {
                            // Close the current login window
                            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                            currentStage.close();

                            // Open the dashboard window
                            Stage dashboardStage = new Stage();
                            Parent root = FXMLLoader.load(getClass().getResource("Dashboard.fxml"));
                            Scene scene = new Scene(root);
                            dashboardStage.setScene(scene);
                            dashboardStage.show();
                        } catch (Exception ex) {
                            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
                            System.out.println(ex);
                        }
                        showMessage("Correct Credentials", "Login Successful");
                        return;
                    }
                } catch (SQLException e) {
                    showError("Error", "An error occurred bro.", e);
                }
            }

            // Reset text fields after 3 unsuccessful attempts
            tfUsername.clear();
            pfPassword.clear();
            showMessage("Login Attempts Exceeded", "You have reached the maximum number of login attempts (3)."
                    + "\nUsername and Password field are cleared.");
            loginAttempts = 0;
        }
    }

    private void showMessage(String title, String content) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showError(String title, String header, Exception exception) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(exception.getMessage());

        // You can also print the stack trace to the console or log it.
        exception.printStackTrace();

        alert.showAndWait();
    }

}
