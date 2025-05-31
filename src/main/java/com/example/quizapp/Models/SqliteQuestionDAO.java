package com.example.quizapp.Models;

import io.github.ollama4j.exceptions.OllamaBaseException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.*;
import java.util.*;

/**
 * The Sqlite Question DAO, communicates with the question table in the database and modifies data
 */
public class SqliteQuestionDAO implements IQuestionDAO {

    private Connection connection;

    /**
     * The constructor for the Sqlite Question DAO
     */
    public SqliteQuestionDAO() {
        connection = SqliteConnection.getInstance();
        createTable();
        //insertSampleData(); //for testing
    }


    /**
     * Creates the questions table in the sqlite database if it does not exist
     */
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


    /**
     * Inserts sample data in the questions table in the sqlite database (for testing)
     */
    private void insertSampleData() {
        try {
            Statement clearStatement = connection.createStatement();
            //String clearQuery = "DELETE FROM questions";
            //clearStatement.execute(clearQuery);
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

    /**
     * Gets the questions that are from a specified quiz
     * @param quizId the quiz ID of the quiz the questions are from
     * @return A list of questions
     */
    @Override
    public List<Question> getQuestionsForQuiz(int quizId) {
        List<Question> questions = new ArrayList<>();

        try {
            String query = "SELECT quizID, questionText, correctAnswer, incorrectAnswer1, incorrectAnswer2, incorrectAnswer3 FROM questions WHERE quizID = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, quizId);
            ResultSet rs = statement.executeQuery();
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

    /**
     * Gets the number of questions in a quiz
     * @param quiz the quiz the questions are from
     * @return the number of questions
     */
    public int getNumberOfQuestions(Quiz quiz) {
        List<Question> questions = getQuestionsForQuiz(quiz.getQuizID());
        long numOfQuestions = questions.size();
        return ((int) numOfQuestions);

    }

    /**
     * Adds all AI generated questions for a quiz to the database
     * @param JSONResponse The Ollama AI JSON response with the questions for the quiz
     * @param quizID the ID of the quiz the questions are for
     */
    public void addAIQuestions(String JSONResponse, int quizID) {

        JSONObject json = new JSONObject(JSONResponse);
        JSONArray quizArray = json.getJSONArray("Quiz");
        for (int i = 0; i < quizArray.length(); i++) {
            JSONObject questionObj = quizArray.getJSONObject(i);

            String questionText = questionObj.getString("question");
            String correctAnswer = questionObj.getString("correctAnswer");
            String incorrectAnswer1 = questionObj.getString("incorrectAnswer1");
            String incorrectAnswer2 = questionObj.getString("incorrectAnswer2");
            String incorrectAnswer3 = questionObj.getString("incorrectAnswer3");
            Question question = new Question(quizID, questionText, correctAnswer, incorrectAnswer1, incorrectAnswer2, incorrectAnswer3);
            addQuestion(question);
        }

    }

    /**
     * Adds a question to the sqlite database questions table
     * @param question the question to be added to the database
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


    /**
     * Deletes a question from the sqlite database questions table
     * @param question the question to be deleted from the database
     */
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

    public void setQuestions(int quizId, List<Question> questions) throws SQLException {
        connection.setAutoCommit(false);

        try {
            // Delete the old questions for the quiz
            String deleteQuery = "DELETE FROM questions WHERE quizID = ?";
            try (PreparedStatement deleteStmt = connection.prepareStatement(deleteQuery)) {
                deleteStmt.setInt(1, quizId);
                deleteStmt.executeUpdate();
            }

            // Insert the updated list of questions
            String insertQuery = "INSERT INTO questions (quizID, questionText, correctAnswer, incorrectAnswer1, incorrectAnswer2, incorrectAnswer3) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {
                for (Question question : questions) {
                    insertStmt.setInt(1, quizId);
                    insertStmt.setString(2, question.getQuestionText());
                    insertStmt.setString(3, question.getCorrectAnswer());
                    insertStmt.setString(4, question.getIncorrectAnswer1());
                    insertStmt.setString(5, question.getIncorrectAnswer2());
                    insertStmt.setString(6, question.getIncorrectAnswer3());
                    insertStmt.addBatch();
                }
                insertStmt.executeBatch();
            }

            connection.commit();
        } catch (SQLException e) {
            connection.rollback();
            throw e;
        } finally {
            connection.setAutoCommit(true);
        }
    }

}

