package com.example.quizapp.Models;


import javafx.scene.control.ComboBox;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

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
                    + "email VARCHAR NOT NULL,"
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

    // Method to get the ComboBox for number of questions
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
            insertStatement.execute(insertQuery);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addUser(User user) {
        try {
            String query = "INSERT INTO users (userName, email, password) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, user.getUserName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.executeUpdate();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateUser(User user) {
    try {
        String query = "UPDATE users SET userName = ?, email = ?, password = ? WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, user.getUserName());
        statement.setString(2, user.getEmail());
        statement.setString(3, user.getPassword());
        statement.setInt(4, user.getUserID());
        statement.executeUpdate();

    } catch (Exception e) {
        e.printStackTrace();
    }
    }

    @Override
    public void deleteUser(User user) {
        try {
            String query = "DELETE FROM users WHERE id = ?";
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


}

