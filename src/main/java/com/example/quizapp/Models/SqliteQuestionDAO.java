package com.example.quizapp.Models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class SqliteQuestionDAO implements IQuestionDAO {

    private Connection connection;

    public SqliteQuestionDAO() {
        connection = SqliteConnection.getInstance();
        createTable();
        //insertSampleData(); //for testing, to be removed later
    }


    private void createTable() {
        // Create table if not exists
        try {
            Statement statement = connection.createStatement();
            String query = "CREATE TABLE IF NOT EXISTS questions ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "quizID INTEGER NOT NULL,"
                    + "questionText VARCHAR NOT NULL,"
                    + "correctAnswer VARCHAR NOT NULL,"
                    + "incorrectAnswer1 VARCHAR NOT NULL,"
                    + "incorrectAnswer2 VARCHAR NOT NULL,"
                    + "incorrectAnswer3 VARCHAR NOT NULL,"
                    + "FOREIGN KEY (quizID) references quizzes(id) ON DELETE CASCADE"
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
    public void addQuestion(Question question) {
        try {
            String query = "INSERT INTO questions (quizID, questionText, correctAnswer, incorrectAnswer1, incorrectAnswer2, incorrectAnswer3) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, question.getQuizID());
            statement.setString(2, question.getQuestionText());
            statement.setString(3, question.getCorrectAnswer());
            statement.setString(4, question.getIncorrectAnswer1());
            statement.setString(5, question.getIncorrectAnswer2());
            statement.setString(6, question.getIncorrectAnswer3());
            statement.executeUpdate();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateQuestion(Question question) {
        try {
            String query = "UPDATE questions SET quizID = ?, questionText = ?, correctAnswer = ?, incorrectAnswer1 = ?, incorrectAnswer2 = ?, incorrectAnswer3 = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, question.getQuizID());
            statement.setString(2, question.getQuestionText());
            statement.setString(3, question.getCorrectAnswer());
            statement.setString(4, question.getIncorrectAnswer1());
            statement.setString(5, question.getIncorrectAnswer2());
            statement.setString(6, question.getIncorrectAnswer3());
            statement.setInt(7, question.getId());
            statement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteQuestion(Question question) {
        try {
            String query = "DELETE FROM questions WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, question.getId());
            statement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

