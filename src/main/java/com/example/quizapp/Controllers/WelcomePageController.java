package com.example.quizapp.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;


public class WelcomePageController{
    @FXML
    private Button loginButton;
    @FXML
    private Button signupButton;

    public void handleLogin() throws IOException {
        System.out.println("Login Button clicked");

        Stage stage = (Stage) loginButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/quizapp/login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 650);
        stage.setScene(scene);

    }
    // move to signup controller
    public void handleSignup(ActionEvent actionEvent) throws IOException {
        System.out.println("Signup Button clicked");
        // Swap to Signup screen
        Stage stage = (Stage) signupButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/quizapp/signup.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 650);
        stage.setScene(scene);


    }


}