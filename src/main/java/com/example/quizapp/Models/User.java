package com.example.quizapp.Models;

/**
 * Represents a user in the quiz application.
 */
public class User {
    /** Unique identifier for the user. */
    private int id;
    /** Username, email and password of teh user.*/
    private String userName, email, password;
    /**
     * Constructs a new User with the given username, email, and password.
     *
     * @param userName The username of the user.
     * @param email The email address of the user.
     * @param password The user's password.
     */
    public User(String userName, String email, String password) {
        this.userName = userName;
        this.email = email;
        this.password = password;

    }
    /**
     * Gets the unique ID of the user.
     *
     * @return The user ID.
     */
    public int getUserID() {
        return id;
    }

    /**
     * Gets the username of the user.
     *
     * @return The username.
     */
    public String getUserName() {
        return userName;
    }
    /**
     * Gets the email of the user.
     *
     * @return The email.
     */
    public String getEmail() {
        return email;
    }
    /**
     * Gets the password of the user.
     *
     * @return The password.
     */
    public String getPassword() {
        return password;
    }
    /**
     * Sets the unique ID of the user.
     *
     * @param userID The user ID to set.
     */
    public void setUserID(int userID) {
        this.id = userID;
    }
    /**
     * Sets the username of the user.
     *
     * @param userName The username to set.
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }
    /**
     * Sets the email address of the user.
     *
     * @param email The email to set.
     */
    public void setEmail(String email) {
        this.email = email;
    }
    /**
     * Sets the password of the user.
     *
     * @param password The password to set.
     */
    public void setPassword(String password) {
        this.password = password;
    }
    /**
     * Returns a string representation of the user.
     *
     * @return A formatted string representing the user.
     */
    @Override
    public String toString() {
        return String.format("User{id=%d, username='%s', email='%s', password='%s'}",
               id, userName, email, password);
    }


}
