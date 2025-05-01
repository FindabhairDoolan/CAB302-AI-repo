package com.example.quizapp.Controllers;

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

    private IUserDAO userDAO;

    public SignupController() {
        userDAO = new SqliteUserDAO();
    }

    public SignupController(IUserDAO mockUserDAO) {
        userDAO = mockUserDAO;
    }

    public static boolean disableAlertsForTesting = false;

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
            boolean signUpSuccessful = signup(userName, email, password);
            if (!signUpSuccessful) return;

            showAlert(Alert.AlertType.INFORMATION, "Signup successful!");

            // Swap to Login screen
            Stage stage = (Stage) signupButton.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/quizapp/login.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 800, 650);
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Something went wrong during signup. Please try again.");
        }
    }

    public boolean signup(String userName, String email, String password) {

        // Check if any fields are empty
        if (userName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "All fields are required.");
            return false;
        }

        // Email uniqueness check
        if (userDAO.isEmailRegistered(email)) {
            showAlert(Alert.AlertType.WARNING, "Email already registered.");
            return false;
        }
        // Email format validation
        if (!isValidEmail(email)) {
            showAlert(Alert.AlertType.WARNING, "Invalid email format. It should be like user@example.com");
            return false;
        }

        // Password strength check (at least 8 chars, includes letters and digits)
        if (!isValidPassword(password)) {
            showAlert(Alert.AlertType.WARNING, "Password should be at least 8 characters long and contain both letters and digits.");
            return false;
        }

        userDAO.addUser(new User(userName, email, password));
        return true;

    }

    private void showAlert(Alert.AlertType alertType, String message) {
        if(disableAlertsForTesting) return;

        Alert alert = new Alert(alertType);
        alert.setTitle("Signup Validation");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private boolean isValidPassword(String password) {
        if (password.length() < 8) return false;
        boolean hasLetter = false;
        boolean hasDigit = false;

        for (char ch : password.toCharArray()) {
            if (Character.isLetter(ch)) hasLetter = true;
            if (Character.isDigit(ch)) hasDigit = true;
        }

        return hasLetter && hasDigit;
    }
    private boolean isValidEmail(String email) {
        // Regex pattern
        return email.matches("^[^@\\s]+@[^@\\s]+\\.com$");
    }



}
