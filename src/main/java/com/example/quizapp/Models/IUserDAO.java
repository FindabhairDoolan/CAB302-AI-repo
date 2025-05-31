package com.example.quizapp.Models;

import java.util.List;
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

    public boolean isEmailRegistered(String email);

    public User getUserByEmail(String email);
}


