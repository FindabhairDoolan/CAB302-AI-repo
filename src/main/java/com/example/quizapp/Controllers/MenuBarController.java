package com.example.quizapp.Controllers;

import com.example.quizapp.utils.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;


/**
 * Controller class responsible for handling interactions with the menu bar.
 * Provides navigation between different views in the Quiz App.
 */
public class MenuBarController {
    @FXML
    public Region spacer;
    @FXML
    protected Button menuItem2; // Logout
    @FXML
    protected Button menuItem3; // My Quizzes
    @FXML
    protected Button menuItem5; // Create Quiz
    @FXML
    protected Button menuItem6; // Quiz History
    @FXML
    protected Button home; //Home
    @FXML
    private ToolBar menuBar;


    /**
     * Initializes the controller after the FXML components are loaded.
     * Ensures the spacer expands correctly within the layout.
     */
    @FXML
    public void initialize() {
        // Ensure the spacer behaves like a flexible expander
        if (spacer != null) {
            spacer.setMaxWidth(Double.MAX_VALUE); // allow the spacer to expand
            HBox.setHgrow(spacer, Priority.ALWAYS); // correctly set grow priority
        }
    }


    /**
     * Handles navigation to the home (dashboard) view.
     */
    @FXML
    protected void handleHome() {
        // Swap to home/dashboard
        SceneManager.switchScene("/com/example/quizapp/home.fxml", "Home");
        System.out.println("Going to home page...");
    }

    /**
     * Handles logout logic and navigation to the welcome screen.
     * Logs out the user and ends the current session.
     */
    @FXML
    protected void handleLogout() {
        //Swap to Welcome
        SceneManager.switchScene("/com/example/quizapp/WelcomePage.fxml", "Welcome");
        //End user session
        AuthManager.getInstance().logOut();
        //Terminal/System Notification
        System.out.println("Logging out...");


    }



    /**
     * Navigates to the user's list of quizzes the current user has created.
     */
    @FXML
    protected void handleMyQuizzes() {
        SceneManager.switchScene("/com/example/quizapp/my-quizzes.fxml", "My Quizzes");
        System.out.println("Going to my quizzes...");
    }

    /**
     * Navigates to the quiz creation page.
     */
    @FXML
    protected void handleCreateQuiz() {
        //Swap to Create Quiz
        SceneManager.switchScene("/com/example/quizapp/create-quiz-view.fxml", "Create Quiz");
        System.out.println("Going to Quiz Creator...");

    }

    /**
     * Navigates to the quiz creation page.
     */
    @FXML
    protected void handleQuizHistory() {
        SceneManager.switchScene("/com/example/quizapp/quiz-history.fxml", "Quiz History");
        System.out.println("Going to Quiz History...");

    }




}
