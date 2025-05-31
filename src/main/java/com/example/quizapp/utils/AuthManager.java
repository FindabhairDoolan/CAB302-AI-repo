package com.example.quizapp.utils;
import com.example.quizapp.Models.SqliteUserDAO;
import com.example.quizapp.Models.IUserDAO;

import com.example.quizapp.Models.User;
import javafx.scene.control.Alert;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Singleton class responsible for user authentication and session management.
 * Handles login, logout, and signup operations using a user data access object (IUserDAO).
 * Provides password hashing and validation utilities.
 */
public class AuthManager {

    /**
     * The singleton instance of AuthManager.
     */
    private static AuthManager instance = null;

    /**
     * Data access object interface to interact with user data storage.
     */
    private IUserDAO userDAO;

    /**
     * Keeps track of the currently logged-in user.
     */
    private User currentUser = null;

    /**
     * Flag to disable JavaFX alerts during testing.
     */
    public static boolean disableAlertsForTesting = false;

    /**
     * Private constructor to prevent external instantiation.
     * Initializes the userDAO with the real SqliteUserDAO implementation.
     */
    private AuthManager() {
        this.userDAO = new SqliteUserDAO();
    }

    /**
     * Private constructor used for testing purposes with a mock userDAO.
     *
     * @param mockUserDAO the mock implementation of IUserDAO
     */
    private AuthManager(IUserDAO mockUserDAO) {
        this.userDAO = mockUserDAO;
    }

    /**
     * Retrieves the singleton instance of AuthManager.
     * Creates the instance if it does not exist.
     *
     * @return the singleton AuthManager instance
     */
    public static AuthManager getInstance() {
        if (instance == null) {
            instance = new AuthManager();
        }
        return instance;
    }

    /**
     * Sets the singleton instance to a test instance with a mock IUserDAO.
     * Useful for unit testing without affecting the real database.
     *
     * @param mockUserDAO mock implementation of IUserDAO to use in tests
     */
    public static void setTestInstance(IUserDAO mockUserDAO) {
        instance = new AuthManager(mockUserDAO);
    }

    /**
     * Returns the currently logged-in user.
     *
     * @return the current User, or null if no user is logged in
     */
    public User getCurrentUser() {
        return this.currentUser;
    }

    /**
     * Attempts to log in a user with the specified email and password.
     * Password is hashed before validation.
     *
     * @param email the email of the user trying to log in
     * @param password the plaintext password entered by the user
     * @return true if login is successful; false otherwise
     */
    public boolean login(String email, String password) {
        boolean isValid = userDAO.validateCredentials(email, hashPassword(password));
        if(isValid){
            currentUser = userDAO.getUserByEmail(email);
        };

        return isValid;
    }
    /**
     * Logs out the current user by clearing the currentUser reference.
     */
    public void logOut() {
        currentUser = null;
    }

    /**
     * Registers a new user with the provided username, email, and password.
     * Validates input fields, email format, email uniqueness, and password strength.
     * Shows alerts for validation errors unless alerts are disabled for testing.
     *
     * @param userName the desired username
     * @param email the user's email address
     * @param password the plaintext password
     * @return true if signup is successful; false otherwise
     */
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

        userDAO.addUser(new User(userName, email, hashPassword(password)));
        return true;

    }
    /**
     * Shows a JavaFX alert with the given type and message.
     * Alerts can be disabled during testing.
     *
     * @param alertType the type of alert (e.g., ERROR, WARNING)
     * @param message the message to display in the alert
     * @param signup true if this alert is related to signup validation (sets alert title)
     */
    public void showAlert(Alert.AlertType alertType, String message, boolean signup) {
        if(disableAlertsForTesting) return;

        Alert alert = new Alert(alertType);
        if (signup) { alert.setTitle("Signup Validation"); }
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    /**
     * Checks if a password is valid by ensuring it is at least 8 characters long
     * and contains at least one letter and one digit.
     *
     * @param password the password to validate
     * @return true if password meets criteria; false otherwise
     */
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
    /**
     * Validates an email address format using a regular expression.
     * Only accepts emails ending with ".com".
     *
     * @param email the email string to validate
     * @return true if email matches the pattern; false otherwise
     */
    private boolean isValidEmail(String email) {
        // Regex pattern
        return email.matches("^[^@\\s]+@[^@\\s]+\\.com$");
    }
    /**
     * Hashes the given password string using SHA-256 algorithm.
     * Converts the hash to a hexadecimal string.
     *
     * @param password the plaintext password to hash
     * @return the hashed password as a hex string
     * @throws RuntimeException if SHA-256 algorithm is unavailable
     */
    public String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashed = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashed) {
                sb.append(String.format("%02x", b)); // Convert to hex
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 hashing not available", e);
        }
    }
}
