package com.example.quizapp.Models;


import javafx.scene.control.ComboBox;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Data Access Object (DAO) for the User entity.
 * Handles all database operations related to the users table in the SQLite database.
 */
public class SqliteUserDAO implements IUserDAO {


    private Connection connection;

    /**
     * The constructor for the Sqlite User DAO
     */
    public SqliteUserDAO() {
        connection = SqliteConnection.getInstance();
        createTable();
        //insertSampleData(); //for testing, to be removed later
    }

    /**
     * Creates the users table in the sqlite database if it does not exist
     */
    private void createTable() {
        // Create table if not exists
        try {
            Statement statement = connection.createStatement();
            String query = "CREATE TABLE IF NOT EXISTS users ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "userName VARCHAR NOT NULL,"
                    + "email VARCHAR NOT NULL UNIQUE,"
                    + "password VARCHAR NOT NULL"
                    + ")";
            statement.execute(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void insertSampleData() {
        try {
            Statement clearStatement = connection.createStatement();
            String clearQuery = "DELETE FROM users";
            clearStatement.execute(clearQuery);
            Statement insertStatement = connection.createStatement();
            String insertQuery = "INSERT INTO users (userName, email, password) VALUES "
                    + "('John Doe', 'johndoe@example.com', 'secret1'),"
                    + "('Jane Doe', 'janedoe@example.com', 'secret1'),"
                    + "('Jay Doe', 'jaydoe@example.com', 'secret1')";
            //insertStatement.execute(insertQuery);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Adds a new user to the database.
     *
     * @param user the User object containing user details to be inserted
     */
    @Override
    public void addUser(User user) {
        try {
            String query = "INSERT INTO users (userName, email, password) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, user.getUserName());
            statement.setString(2, user.getEmail());
            //statement.setString(3, user.getPassword());
            statement.setString(3, user.getPassword()); // Store hashed password
            statement.executeUpdate();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if an email is already registered in the database.
     *
     * @param email the email address to check
     * @return true if the email exists, false otherwise
     */
    @Override
    public boolean isEmailRegistered(String email) {
        try {
            String query = "SELECT 1 FROM users WHERE email = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next(); // returns true if email found
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * Retrieves a User object by email.
     *
     * @param email the email address to search for
     * @return the User object if found, null otherwise
     */
    @Override
    public User getUserByEmail(String email) {
        User user = null;
        try {
            String query = "SELECT * FROM users WHERE email = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, email);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                user = new User(
                        rs.getString("userName"),
                        rs.getString("email"),
                        rs.getString("password")
                );
                int id = rs.getInt("id");
                user.setUserID(id);
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    /**
     * Validates login credentials against stored user data.
     *
     * @param email the email address provided
     * @param password the password provided (should be hashed as stored)
     * @return true if credentials are valid, false otherwise
     */
    @Override
    public boolean validateCredentials(String email, String password) {
        try {
            String query = "SELECT * FROM users WHERE email = ? AND password = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, email);
            statement.setString(2, password);
            var resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Validates the signup details for a new user.
     * Checks for email uniqueness, password length, and password complexity.
     *
     * @param user the User object to validate
     * @return true if the signup information is valid, false otherwise
     */
    public boolean isSignupValid(User user) {
        try {
            if (isEmailRegistered(user.getEmail())) {
                System.out.println("Email already registered");
                return false;
            }

            String password = user.getPassword();

            if (password.length() < 8) {
                System.out.println("Password should be at least 8 characters long");
                return false;
            }
            if (!password.matches(".*[A-Za-z].*") || !password.matches(".*\\d.*")) {
                System.out.println("Password must contain both letters and digits");
                return false;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}

