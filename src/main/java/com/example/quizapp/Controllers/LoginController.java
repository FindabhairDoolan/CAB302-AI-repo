package com.example.quizapp.Controllers;

import com.example.quizapp.Models.SqliteUserDAO;
import com.example.quizapp.Models.IUserDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;



public class LoginController {

    private IUserDAO userDAO;

    public LoginController() {
        userDAO = new SqliteUserDAO();
    }

    public LoginController(IUserDAO mockUserDAO) {
        userDAO = mockUserDAO;
    }

    @FXML
    private Button backButton;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

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


    public void handleLogin(ActionEvent actionEvent) {
        String email = emailField.getText();
        String password = passwordField.getText();
        boolean loginSucceeded = login(email, password);
        if (loginSucceeded) {
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
            System.out.println("Incorrect credentials!");
        }
    }

    public boolean login(String email, String password) {
        boolean isValid = userDAO.validateCredentials(email, password);
        return isValid;
    }
}
