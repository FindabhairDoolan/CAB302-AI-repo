package com.example.quizapp.Models;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SqliteQuizAttemptDAO implements IQuizAttemptDAO {

    private Connection connection;

    public SqliteQuizAttemptDAO() {
        connection = SqliteConnection.getInstance();
        createTable();
    }


    private void createTable() {
        // Create table if not exists
        try {
            Statement statement = connection.createStatement();
            String query = "CREATE TABLE IF NOT EXISTS quizAttempts ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "quizID INTEGER NOT NULL,"
                    + "userID INTEGER NOT NULL,"
                    + "score DOUBLE,"
                    + "attemptTime INTEGER NOT NULL,"
                    + "answers TEXT,"
                    + "FOREIGN KEY (quizID) REFERENCES quizzes(id) ON DELETE CASCADE,"
                    + "FOREIGN KEY (userID) REFERENCES users(id) ON DELETE CASCADE"
                    + ")";
            statement.execute(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void addQuizAttempt(QuizAttempt quizAttempt) {

        try {
            String answersJson = new ObjectMapper().writeValueAsString(quizAttempt.getAnswers());
            String query = "INSERT INTO quizAttempts (quizID, userID, score, attemptTime, answers) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, quizAttempt.getQuizID());
            statement.setInt(2, quizAttempt.getUserID());
            statement.setDouble(3, quizAttempt.getScore());
            statement.setInt(4, quizAttempt.getAttemptTime());
            statement.setString(5, answersJson);
            statement.executeUpdate();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateQuizAttempt(QuizAttempt quizAttempt) {
        try {
            String query = "UPDATE quizAttempts SET quizID = ?, userID = ?, score = ?, attemptTime = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, quizAttempt.getQuizID());
            statement.setInt(2, quizAttempt.getUserID());
            statement.setDouble(3, quizAttempt.getScore());
            statement.setInt(4, quizAttempt.getAttemptTime());
            statement.setInt(5, quizAttempt.getId());

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

    @Override
    public List<QuizAttempt> getAttemptsByUserAndQuiz(int userID, int quizID) {
        List<QuizAttempt> quizAttempts = new ArrayList<>();
        try {
            String query = "SELECT * FROM quizAttempts where userID = ? AND quizID = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, userID);
            statement.setInt(2, quizID);
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                List<String> answers = new ObjectMapper().readValue(rs.getString("answers"), new TypeReference<List<String>>() {});
                QuizAttempt quizAttempt = new QuizAttempt(
                        quizID,
                        userID,
                        rs.getInt("score"),
                        rs.getInt("attemptTime"),
                        answers
                );
                quizAttempt.setId(rs.getInt("id"));
                quizAttempts.add(quizAttempt);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return quizAttempts;
    }

    //work in progress
    @Override
    public List<QuizWithScore> getQuizzesAttemptedByUser(int userID) {
        List<QuizWithScore> attempts = new ArrayList<>();

        try {
            String query = " SELECT q.*, qa.* FROM quizAttempts qa JOIN quizzes q ON qa.quizID = q.id WHERE qa.userID = ? ORDER BY qa.attemptTime DESC ";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, userID);
            ResultSet rs = statement.executeQuery();
            Quiz quiz = null;
            QuizAttempt attempt = null;
            while (rs.next()) {

                quiz = new Quiz(
                        rs.getString("name"),
                        rs.getString("subject"),
                        rs.getString("topic"),
                        rs.getInt("timer"),
                        rs.getString("difficulty"),
                        rs.getString("yearLevel"),
                        rs.getString("country"),
                        rs.getString("visibility"),
                        rs.getInt("creatorID")
                );

                List<String> answers = new ObjectMapper().readValue(rs.getString("answers"), new TypeReference<List<String>>() {});
                attempt = new QuizAttempt(
                        rs.getInt("quizID"),
                        rs.getInt("userID"),
                        rs.getDouble("score"),
                        rs.getInt("attemptTime"),
                        answers
                );

                quiz.setQuizID(rs.getInt("id"));
                attempts.add(new QuizWithScore(quiz, attempt));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return attempts;
    }


}

