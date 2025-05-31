package com.example.quizapp.Models;

import java.util.List;

/**
 * Interface for the Quiz DAO
 */

public interface IUserDAO {
    /**
     *
     * Adds a new user to the database.
     * @param user The user to add.
     */
    public void addUser(User user);

    /**
     * Validates user credentials.
     * @param username The username to validate.
     * @param password The password to validate.
     * @return true if credentials are valid, false otherwise.
     */
    boolean validateCredentials(String username, String password);

    /**
     * Checks if the email is registered.
     * @param email The email to check.
     * @return true if the email is registered, false otherwise.
     */
    public boolean isEmailRegistered(String email);

    /**
     * Retrieves a User object that is associated with the email address.
     * @param email The email of the user to retrieve.
     * @return The user associated with the email.
     */
    public User getUserByEmail(String email);
}


