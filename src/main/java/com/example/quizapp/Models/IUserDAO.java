package com.example.quizapp.Models;

import java.util.List;
public interface IUserDAO {
    /**
     * Adds a new user to the database.
     * @param user The user to add.
     */
    public void addUser(User user);

    /**
     * Updates an existing user in the database.
     * @param user The user to update.
     */
    public void updateUser(User user);

    /**
     * Deletes a user from the database.
     * @param user The user to delete.
     */
    public void deleteUser(User user);
    /**
     * Sets a new userID to the database.
     * @param userID The userID to set.
     */
    /**
     * Validates user credentials.
     * @param username The username to validate.
     * @param password The password to validate.
     * @return true if credentials are valid, false otherwise.
     */
    boolean validateCredentials(String username, String password);
    public boolean isEmailRegistered(String email);
}


