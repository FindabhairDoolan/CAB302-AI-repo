package com.example.quizapp.Models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class SqliteQuizAttemptDAO implements IQuizAttemptDAO {

    private Connection connection;

    public SqliteQuizAttemptDAO() {
        connection = SqliteConnection.getInstance();
        createTable();
        //insertSampleData(); //for testing, to be removed later
    }


    private void createTable() {
        // Create table if not exists
        try {
            Statement statement = connection.createStatement();
            String query = "CREATE TABLE IF NOT EXISTS quizAttempts ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "quizID INTEGER NOT NULL,"
                    + "userID INTEGER NOT NULL,"
                    + "score,"
                    + "attemptTime DATETIME DEFAULT CURRENT_TIMESTAMP,"
                    + "FOREIGN KEY (quizID) REFERENCES quizzes(id) ON DELETE CASCADE,"
                    + "FOREIGN KEY (userID) REFERENCES users(id) ON DELETE CASCADE"
                    + ")";
            statement.execute(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

/**
    private void insertSampleData() {
        try {
            Statement clearStatement = connection.createStatement();
            String clearQuery = "DELETE FROM quizzes";
            clearStatement.execute(clearQuery);
            Statement insertStatement = connection.createStatement();
            //Need to be modified if wanted to use for testing purposes:
            //String insertQuery = "INSERT INTO quizzes (userName, email, password) VALUES "
            //        + "('John Doe', 'johndoe@example.com', 'secret1'),"
            //        + "('Jane Doe', 'janedoe@example.com', 'secret1'),"
            //       + "('Jay Doe', 'jaydoe@example.com', 'secret1')";
            //insertStatement.execute(insertQuery);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 */

    @Override
    public void addQuizAttempt(QuizAttempt quizAttempt) {
        try {
            String query = "INSERT INTO quizAttempts (quizID, userID, score) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, quizAttempt.getQuizID());
            statement.setInt(2, quizAttempt.getUserID());
            statement.setInt(3, quizAttempt.getScore());
            statement.executeUpdate();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateQuizAttempt(QuizAttempt quizAttempt) {
        try {
            String query = "UPDATE quizAttempts SET quizID = ?, userID = ?, score = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, quizAttempt.getQuizID());
            statement.setInt(2, quizAttempt.getUserID());
            statement.setInt(3, quizAttempt.getScore());
            statement.setInt(4, quizAttempt.getId());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteQuizAttempt(QuizAttempt quizAttempt) {
        try {
            String query = "DELETE FROM quizAttempts WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, quizAttempt.getId());
            statement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

