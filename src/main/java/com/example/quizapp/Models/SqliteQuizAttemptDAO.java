package com.example.quizapp.Models;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

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
                //might be some problems with the question id and quizID?
                QuizAttempt quizAttempt = new QuizAttempt(
                        quizID,
                        userID,
                        rs.getInt("score")
                );
                quizAttempt.setId(rs.getInt("id"));
                String timeStr = rs.getString("attemptTime");
                if (timeStr != null) {
                    quizAttempt.setAttemptTime(LocalDateTime.parse(timeStr));
                }
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
            String query = " SELECT q.*, qa.score FROM quizAttempts qa JOIN quizzes q ON qa.quizID = q.id WHERE qa.userID = ? ORDER BY qa.attemptTime DESC ";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, userID);
            ResultSet rs = statement.executeQuery();
            Quiz quiz = null;
            while (rs.next()) {
                quiz = new Quiz(
                        rs.getString("name"),
                        rs.getString("subject"),
                        rs.getString("topic"),
                        rs.getString("mode"),
                        rs.getString("difficulty"),
                        rs.getString("yearLevel"),
                        rs.getString("country"),
                        rs.getString("visibility"),
                        rs.getInt("creatorID")
                );
                quiz.setQuizID(rs.getInt("id"));

                int quizID = rs.getInt("id");

                // Store quiz object once
                if (!quizMap.containsKey(quizID)) {
                    Quiz quiz = new Quiz(
                            rs.getString("name"),
                            rs.getString("subject"),
                            rs.getString("topic"),
                            rs.getString("mode"),
                            rs.getString("difficulty"),
                            rs.getString("yearLevel"),
                            rs.getString("country"),
                            rs.getString("visibility"),
                            rs.getInt("creatorID")
                    );
                    quiz.setQuizID(quizID);
                    quizMap.put(quizID, quiz);
                }

                //Timestamp time = rs.getTimestamp("attemptTime"); <- not working, maybe try later again
                int score = rs.getInt("score");
                attempts.add(new QuizWithScore(quiz, score));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        //return groupedQuizzes;
        return attempts;
    }


}

