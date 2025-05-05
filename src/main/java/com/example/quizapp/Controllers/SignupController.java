package com.example.quizapp.Controllers;

import com.example.quizapp.Models.AuthManager;
import com.example.quizapp.Models.IUserDAO;
import com.example.quizapp.Models.SqliteUserDAO;
import com.example.quizapp.Models.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;



public class SignupController {

    private AuthManager authManager = AuthManager.getInstance();

    public SignupController() {}



    @FXML
    private Button backButton;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button signupButton;

    @FXML
    private void handleBackButton() {
        System.out.println("Back clicked");
        // Swap to Welcome screen
        Stage stage = (Stage) backButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/quizapp/WelcomePage.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 800, 650);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.setScene(scene);
    }


    @FXML
    public void handleSignup(ActionEvent actionEvent) {
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String userName = usernameField.getText().trim();

        try {
            // All validations passed – register the user
            boolean signUpSuccessful = authManager.signup(userName, email, password);
            if (!signUpSuccessful) return;

            authManager.showAlert(Alert.AlertType.INFORMATION, "Signup successful!", true );

            // Swap to Login screen
            Stage stage = (Stage) signupButton.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/quizapp/login.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 800, 650);
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
            authManager.showAlert( Alert.AlertType.ERROR, "Something went wrong during signup. Please try again.", true);
        }
    }



}
