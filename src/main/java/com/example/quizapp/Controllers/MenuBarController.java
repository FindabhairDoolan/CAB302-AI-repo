package com.example.quizapp.Controllers;

import com.example.quizapp.utils.SceneManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.stage.Stage;


public class MenuBarController {
    @FXML
    public Region spacer;
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
        // Swap to home
        Stage stage = (Stage) menuBar.getScene().getWindow();
        SceneManager.switchScene("/com/example/quizapp/home.fxml", "Home", stage);
        System.out.println("Going to home page...");
    }

    @FXML
    protected void handleLogout(ActionEvent event) {
        //Swap to Welcome
        Stage stage = (Stage) menuBar.getScene().getWindow();
        SceneManager.switchScene("/com/example/quizapp/WelcomePage.fxml", "Welcome", stage);
        System.out.println("Logging out...");
        // add logout logic

    }

    @FXML
    protected void handleMenuItem1() {
        System.out.println("Navigating to Account page...");
    }

    @FXML
    protected void handleMenuItem3() {
        System.out.println("Going to my quizzes");
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
