package com.example.quizapp.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuBarController {

    @FXML
    protected MenuItem menuItem1; // Account
    @FXML
    protected MenuItem menuItem2; // Logout
    @FXML
    protected MenuItem menuItem3; // My Quizzes
    @FXML
    protected MenuItem menuItem4; // Take Quiz
    @FXML
    protected MenuItem menuItem5; // Create Quiz
    @FXML
    protected MenuItem menuItem6; // Quiz History
    @FXML
    protected Menu home; //Home
    @FXML
    private MenuBar menuBar;





    @FXML
    protected void handleHome(ActionEvent event) {
        //SceneManager.switchScene("/com/example/pages/WelcomePage.fxml", "Welcome");
        // Swap to home
        try {
            System.out.println("Going to home page...");

            // Get current stage from any node (make sure 'home' is defined)
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Load new FXML
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/quizapp/home.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 800, 650);

            // Set new scene on the same stage
            stage.setScene(scene);
            stage.setTitle("Home");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @FXML
    protected void handleLogout(ActionEvent event) {
        System.out.println("Logging out...");
        // add logout logic
        //SceneManager.switchScene("/com/example/pages/WelcomePage.fxml", "Welcome");
        //Swap to Welcome
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("WelcomePage.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 800, 550);
            Stage stage = (Stage) menuBar.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Welcome");

        }
        catch (IOException e) {
            e.printStackTrace();
    }

           
    }

    @FXML
    protected void handleMenuItem1() {
        System.out.println("Navigating to Account page...");
        // add shared navigation logic
    }

    @FXML
    protected void handleMenuItem3() {
        System.out.println("Going to my quizzes");
        // add edit quiz page logic
    }

    @FXML
    protected void handleMenuItem4() {
        System.out.println("Take Quiz");
        //
    }

    @FXML
    protected void handleMenuItem5() {
        System.out.println("Going to Quiz Creator...");
        // add  logic
    }

    @FXML
    protected void handleMenuItem6() {
        System.out.println("Going to Quiz History...");
        // add  logic
    }



}
