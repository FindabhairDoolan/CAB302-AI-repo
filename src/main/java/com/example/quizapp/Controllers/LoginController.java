package com.example.quizapp.Controllers;

import com.example.quizapp.utils.AuthManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;


/**
 * Controller for the login screen.
 * Handles user authentication and navigation between login, welcome, and dashboard screens.
 */
public class LoginController {

    private AuthManager authManager = AuthManager.getInstance();

    public LoginController() {}

    @FXML
    private Button backButton;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    /**
     * Handles the back button click by navigating back to the welcome screen.
     */
    @FXML
    private void handleBackButton() {
        System.out.println("Back clicked");
        // Swap to Welcome screen
        Stage stage = (Stage) loginButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/quizapp/WelcomePage.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 800, 550);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.setScene(scene);
    }

    /**
     * Attempts to log in the user using the entered email and password.
     * On success, navigates to the dashboard; otherwise, shows an error alert.
     */
    public void handleLogin(ActionEvent actionEvent) {
        String email = emailField.getText();
        String password = passwordField.getText();
        boolean loginSucceeded = authManager.login(email, password);
        if (loginSucceeded) {
            authManager.showAlert(Alert.AlertType.INFORMATION, "Login successful!", false);
            System.out.println("Successful login -> Dashboard");
            // Swap to dashboard
            Stage stage = (Stage) loginButton.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/quizapp/home.fxml"));
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load(), 800, 550);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            stage.setScene(scene);
        }
        else {
            authManager.showAlert(Alert.AlertType.ERROR, "Incorrect email or password", false);
            System.out.println("Incorrect credentials!");
        }
    }




}
