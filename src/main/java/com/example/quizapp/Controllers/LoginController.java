package com.example.quizapp.Controllers;

import com.example.quizapp.Models.SqliteUserDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import javafx.scene.control.Alert;



public class LoginController {

    private SqliteUserDAO userDAO = new SqliteUserDAO();

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
            scene = new Scene(fxmlLoader.load(), 800, 650);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.setScene(scene);
    }


    public void handleLogin(ActionEvent actionEvent) {
        String email = emailField.getText();
        String password = passwordField.getText();
        boolean isValid = userDAO.validateCredentials(email, password);
        if (isValid) {
            showAlert("Login successful!", Alert.AlertType.INFORMATION);
            loadDashboard(); // Open next window
            System.out.println("Successful login -> Dashboard");
            // Swap to dashboard
        }
        else {
            showAlert("Incorrect email or password", Alert.AlertType.ERROR);
            System.out.println("Incorrect credentials!");
        }

    }
    private void showAlert(String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setContentText(message);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
    private void loadDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/quizapp/home.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            showAlert("Failed to load dashboard: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
}
