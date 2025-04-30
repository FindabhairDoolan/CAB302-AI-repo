package com.example.quizapp.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;

public abstract class MenuBarController {

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
    protected void handleMenuItem1() {
        System.out.println("Navigating to Account page...");
        // add shared navigation logic
    }

    @FXML
    protected void handleLogout() {
        System.out.println("Logging out...");
        // add logout logic
        //SceneManager.switchScene("/com/example/pages/WelcomePage.fxml", "Welcome");
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
