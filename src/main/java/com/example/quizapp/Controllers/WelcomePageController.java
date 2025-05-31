package com.example.quizapp.Controllers;

import com.example.quizapp.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * Controller for the Welcome page.
 * Handles navigation to the Login and Signup screens.
 */
public class WelcomePageController{
    @FXML
    private Button loginButton;
    @FXML
    private Button signupButton;
    @FXML
    public Button tempButton;

    /**
     * Handles the login button click.
     * Navigates the user to the login screen.
     *
     * @throws IOException if the login FXML file cannot be loaded
     */
    public void handleLogin() throws IOException {
        System.out.println("Login Button clicked");

        Stage stage = (Stage) loginButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/quizapp/login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 650);
        stage.setScene(scene);

    }

    /**
     * Handles the signup button click.
     * Navigates the user to the signup screen.
     *
     * @param actionEvent the event triggered by clicking the signup button
     * @throws IOException if the signup FXML file cannot be loaded
     */
    public void handleSignup(ActionEvent actionEvent) throws IOException {
        System.out.println("Signup Button clicked");
        // Swap to Signup screen
        Stage stage = (Stage) signupButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/quizapp/signup.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 650);
        stage.setScene(scene);


    }


}