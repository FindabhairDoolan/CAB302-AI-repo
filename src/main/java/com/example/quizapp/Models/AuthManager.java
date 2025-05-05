package com.example.quizapp.Models;
import com.example.quizapp.Models.SqliteUserDAO;
import com.example.quizapp.Models.IUserDAO;

import javafx.scene.control.Alert;

//create a singleton object to keep track of the currently logged in user
public class AuthManager {

    //Create the singleton object
    private static AuthManager instance = null;

    //Initialize userDAO object
    private IUserDAO userDAO;

    //keep track of the current user by storing their email
    private String currentUser = null;

    //To disable alerts while testing since tests don't work with alerts on
    public static boolean disableAlertsForTesting = false;

    //private constructor so no other classes can create objects
    private AuthManager() {
        this.userDAO = new SqliteUserDAO();
    }

    //Mock constructor for testing purposes
    private AuthManager(IUserDAO mockUserDAO) {
        this.userDAO = mockUserDAO;
    }

    //method for other classes to access the object
    public static AuthManager getInstance() {
        if (instance == null) {
            instance = new AuthManager();
        }
        return instance;
    }

    //Switch to use mock userDAO for testing
    public static void setTestInstance(IUserDAO mockUserDAO) {
        instance = new AuthManager(mockUserDAO);
    }

    //login
    public boolean login(String email, String password) {
        boolean isValid = userDAO.validateCredentials(email, password);
        if(isValid){
            currentUser = email;
        };
        return isValid;
    }

    public void logOut() {
        currentUser = null;
    }

    //sign up
    public boolean signup(String userName, String email, String password) {

        // Check if any fields are empty
        if (userName.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "All fields are required.", true);
            return false;
        }

        // Email uniqueness check
        if (userDAO.isEmailRegistered(email)) {
            showAlert(Alert.AlertType.WARNING, "Email already registered.", true);
            return false;
        }
        // Email format validation
        if (!isValidEmail(email)) {
            showAlert(Alert.AlertType.WARNING, "Invalid email format. It should be like user@example.com", true);
            return false;
        }

        // Password strength check (at least 8 chars, includes letters and digits)
        if (!isValidPassword(password)) {
            showAlert(Alert.AlertType.WARNING, "Password should be at least 8 characters long and contain both letters and digits.", true);
            return false;
        }

        userDAO.addUser(new User(userName, email, password));
        return true;

    }

    public void showAlert(Alert.AlertType alertType, String message, boolean signup) {
        if(disableAlertsForTesting) return;

        Alert alert = new Alert(alertType);
        if (signup) { alert.setTitle("Signup Validation"); }
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
