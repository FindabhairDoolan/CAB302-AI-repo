package com.example.quizapp.Controllers;

import com.example.quizapp.Models.AuthManager;
import com.example.quizapp.utils.SceneManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.stage.*;


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
    protected void handleHome() {
        // Swap to home
        Stage stage = (Stage) menuBar.getScene().getWindow();
        SceneManager.switchScene("/com/example/quizapp/home.fxml", "Home", stage);
        System.out.println("Going to home page...");
    }

    @FXML
    protected void handleLogout() {
        //Swap to Welcome
        Stage stage = (Stage) menuBar.getScene().getWindow();
        SceneManager.switchScene("/com/example/quizapp/WelcomePage.fxml", "Welcome", stage);
        //End user session
        AuthManager.getInstance().logOut();
        //Terminal/System Notification
        System.out.println("Logging out...");


    }

    @FXML
    protected void handleAccount() {
        System.out.println("Pulling up account details...");
    }

    @FXML
    protected void handleMyQuizzes() {
        Stage stage = (Stage) menuBar.getScene().getWindow();
        SceneManager.switchScene("/com/example/quizapp/my-quizzes.fxml", "My Quizzes", stage);
        System.out.println("Going to my quizzes...");
    }

    @FXML
    protected void handleMenuItem4() {
        System.out.println("Take Quiz");
        //rid
    }

    @FXML
    protected void handleCreateQuiz() {
        //Swap to Create Quiz
        Stage stage = (Stage) menuBar.getScene().getWindow();
        SceneManager.switchScene("/com/example/quizapp/create-quiz-view.fxml", "Create Quiz", stage);
        System.out.println("Going to Quiz Creator...");

    }

    @FXML
    protected void handleQuizHistory() {
        Stage stage = (Stage) menuBar.getScene().getWindow();
        SceneManager.switchScene("/com/example/quizapp/quiz-history.fxml", "Quiz History", stage);
        System.out.println("Going to Quiz History...");

    }



}
