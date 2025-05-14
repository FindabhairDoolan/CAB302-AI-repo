package com.example.quizapp.Models;


import javafx.scene.control.ComboBox;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class SqliteUserDAO implements IUserDAO {


    private Connection connection;


    public SqliteUserDAO() {
        connection = SqliteConnection.getInstance();
        createTable();
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
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    @Override
    public void updateUser(User user) {
    try {
        String hashedPassword = user.getPassword(); //hash the password in the previous function if needed
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

