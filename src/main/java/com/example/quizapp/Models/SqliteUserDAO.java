package com.example.quizapp.Models;


import javafx.scene.control.ComboBox;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class SqliteUserDAO implements IUserDAO {


    private Connection connection;
    private ComboBox<Integer> questionDropdown;


    public SqliteUserDAO() {
        connection = SqliteConnection.getInstance();
        createTable();
        initializeQuestionDropdown();
        //insertSampleData(); //for testing, to be removed later
    }


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


    private void initializeQuestionDropdown() {
        questionDropdown = new ComboBox<>(); // Initialize the ComboBox
        // Add number of questions options
        questionDropdown.getItems().addAll(5, 10, 15, 20, 25);
        // Set default value to 5 questions
        questionDropdown.setValue(5);
    }

    public ComboBox<Integer> getQuestionDropdown() {
        return questionDropdown; // Return the ComboBox
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
    @Override
    public void addUser(User user) {
        try {
            String hashedPassword = hashPassword(user.getPassword());
            String query = "INSERT INTO users (userName, email, password) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, user.getUserName());
            statement.setString(2, user.getEmail());
            //statement.setString(3, user.getPassword());
            statement.setString(3, hashedPassword); // Store hashed password
            statement.executeUpdate();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

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
                user.setPassword(null); //we don't want to actually host the password here
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public void updateUser(User user) {
    try {
        String hashedPassword = hashPassword(user.getPassword());
        String query = "UPDATE users SET userName = ?, email = ?, password = ? WHERE userID = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, user.getUserName());
        statement.setString(2, user.getEmail());
        //statement.setString(3, user.getPassword());
        statement.setString(3, hashedPassword); // Store the hashed password
        statement.setInt(4, user.getUserID());
        statement.executeUpdate();

    } catch (Exception e) {
        e.printStackTrace();
    }
    }

    @Override
    public void deleteUser(User user) {
        try {
            String query = "DELETE FROM users WHERE userID = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, user.getUserID());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean validateCredentials(String email, String password) {
        try {
            String hashedPassword = hashPassword(password); // Hash the input password
            String query = "SELECT * FROM users WHERE email = ? AND password = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, email);
            statement.setString(2, hashedPassword);
            var resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
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


    private String hashPassword(String password) {
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

