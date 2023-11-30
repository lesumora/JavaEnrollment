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
import java.sql.Statement;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author hp
 */
public class LoginController implements Initializable {

    Connection connection = null;
    Statement statement = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    final String DB_URL = "jdbc:mysql://localhost:3306/enrollment?zeroDateTimeBehavior=CONVERT_TO_NULL";
    final String DATABASE_NAME = "enrollment";
    final String USERNAME = "root";
    final String PASSWORD = "";

    double x = 0, y = 0;
    private int loginAttempts = 0;

    @FXML
    private Button btnLogin, btnRegister;
    @FXML
    private Label label;
    @FXML
    private TextField tfUsername;
    @FXML
    private PasswordField pfPassword;
    @FXML
    private Button btnExit;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
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
                Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (event.getSource() == btnLogin) {
            String username = tfUsername.getText();
            String password = pfPassword.getText();

            // Check username and password length
            if (username.length() > 6 || password.length() > 6) {
                showMessage("Character limit reached!", 
                        "Username and password limit is 6 characters only.");
                return;
            }

            // Check login attempts
            while (loginAttempts < 3) {
                try {
                    connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
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
                            dashboardStage.initStyle(StageStyle.UNDECORATED);

                            root.setOnMousePressed(evt -> {
                                x = evt.getSceneX();
                                y = evt.getSceneY();
                            });
                            root.setOnMouseDragged(evt -> {
                                dashboardStage.setX(evt.getScreenX() - x);
                                dashboardStage.setY(evt.getScreenY() - y);
                            });

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
        
        if (event.getSource() == btnExit){
            Platform.exit();
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
