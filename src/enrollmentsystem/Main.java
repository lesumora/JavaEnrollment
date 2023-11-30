/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXML.java to edit this template
 */
package enrollmentsystem;

import java.sql.Connection;
import java.sql.DriverManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author hp
 */
public class Main extends Application {
    double x = 0, y = 0;
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));       
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        
        root.setOnMousePressed(evt -> {
            x = evt.getSceneX();
            y = evt.getSceneY();
        });
        root.setOnMouseDragged(evt ->{
            stage.setX(evt.getScreenX() - x);
            stage.setY(evt.getScreenY() - y);
        });
        stage.show();
    }
    
    public static Connection connect() {
        final String DB_URL = "jdbc:mysql://localhost:3306/enrollment?zeroDateTimeBehavior=CONVERT_TO_NULL";
        final String USERNAME = "root";
        final String PASSWORD = "";
        
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            return conn;
        }catch(Exception e){
            System.out.println(e);
        }
        return null;
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
}
