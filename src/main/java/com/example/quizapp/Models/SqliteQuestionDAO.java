package com.example.quizapp.Models;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SqliteQuestionDAO implements IQuestionDAO {

    private Connection connection;

    public SqliteQuestionDAO() {
        connection = SqliteConnection.getInstance();
        createTable();
        insertSampleData(); //for testing, to be removed later
    }


    private void createTable() {
        // Create table if not exists
        try {
            Statement statement = connection.createStatement();
            String query = "CREATE TABLE IF NOT EXISTS questions ("
                    + "questionID INTEGER PRIMARY KEY AUTOINCREMENT,"
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


    private void insertSampleData() {
        try {
            Statement clearStatement = connection.createStatement();
            String clearQuery = "DELETE FROM questions";
            clearStatement.execute(clearQuery);
            Statement insertStatement = connection.createStatement();
            String insertQuery = "INSERT INTO questions (quizID, questionText, correctAnswer, incorrectAnswer1, incorrectAnswer2, incorrectAnswer3) VALUES "
                    + "('1', 'What is 1+1?', '2', '1', '0', '4'),"
                    + "('1', 'What symbol represents subtraction?', '-', '+', 'x', '%'),"
                    + "('1', 'How many sides does a square have?', '4', '1', '5', '3'),"
                    + "('1', 'What is 10x1?', '10', '1', '20', '11'),"
                    + "('1', 'How many sides does a triangle have?', '3', '4', '2', '1')";
            insertStatement.execute(insertQuery);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Question> getQuestionsForQuiz(int quizId) {

        List<Question> questions = new ArrayList<>();

        try {
            Statement getAll = connection.createStatement();
            ResultSet rs = getAll.executeQuery("SELECT quizID, questionText, correctAnswer, incorrectAnswer1, incorrectAnswer2, incorrectAnswer3 FROM questions");
            while (rs.next()) {
                questions.add(
                        new Question(
                                rs.getInt("quizID"),
                                rs.getString("questionText"),
                                rs.getString("correctAnswer"),
                                rs.getString("incorrectAnswer1"),
                                rs.getString("incorrectAnswer2"),
                                rs.getString("incorrectAnswer3")
                        )
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return questions;
    }


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
            statement.setInt(7, question.getQuestionID());
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
            statement.setInt(1, question.getQuestionID());
            statement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

