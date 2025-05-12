package com.example.quizapp.Models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SqliteQuizDAO implements IQuizDAO {

    private Connection connection;

    public SqliteQuizDAO() {
        connection = SqliteConnection.getInstance();
        createTable();
        //insertSampleData(); //for testing, to be removed later
    }


    private void createTable() {
        // Create table if not exists
        try {
            Statement statement = connection.createStatement();
            String query = "CREATE TABLE IF NOT EXISTS quizzes ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "quizName VARCHAR NOT NULL,"
                    + "quizTopic VARCHAR NOT NULL,"
                    + "quizMode VARCHAR NOT NULL,"
                    + "difficulty VARCHAR NOT NULL," //is "not null" necessary?
                    + "yearLevel VARCHAR NOT NULL," //is "not null" necessary?
                    + "country VARCHAR NOT NULL," //is "not null" necessary?
                    + "creatorID INTEGER NOT NULL,"
                    + "FOREIGN KEY (creatorID) REFERENCES users(id) ON DELETE SET NULL" //or on delete cascade?
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
            Need to be modified if wanted to use for testing purposes:
            String insertQuery = "INSERT INTO quizzes (userName, email, password) VALUES "
                    + "('John Doe', 'johndoe@example.com', 'secret1'),"
                    + "('Jane Doe', 'janedoe@example.com', 'secret1'),"
                   + "('Jay Doe', 'jaydoe@example.com', 'secret1')";
            insertStatement.execute(insertQuery);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 */

    @Override
    public void addQuiz(Quiz quiz) {
        try {
            String query = "INSERT INTO quizzes (quizName, quizTopic, quizMode, difficulty, yearLevel, country, creatorID) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, quiz.getQuizName());
            statement.setString(2, quiz.getQuizTopic());
            statement.setString(3, quiz.getQuizMode());
            statement.setString(4, quiz.getDifficulty());
            statement.setString(5, quiz.getYearLevel());
            statement.setString(6, quiz.getCountry());
            statement.setInt(7, quiz.getCreatorID());
            statement.executeUpdate();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateQuizInfo(Quiz quiz) {
        try {
            String query = "UPDATE quizzes SET quizName = ?, quizTopic = ?, quizMode = ?, difficulty = ?, yearLevel = ?, country = ?, creatorID = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, quiz.getQuizName());
            statement.setString(2, quiz.getQuizTopic());
            statement.setString(3, quiz.getQuizMode());
            statement.setString(4, quiz.getDifficulty());
            statement.setString(5, quiz.getYearLevel());
            statement.setString(6, quiz.getCountry());
            statement.setInt(7, quiz.getCreatorID());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteQuiz(Quiz quiz) {
        try {
            String query = "DELETE FROM quizzes WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, quiz.getQuizID());
            statement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Quiz> searchQuizByTopic(String topic) {
        List<Quiz> quizzes = new ArrayList<>();
        try {
            //query and database are changed to lowercase and, and search is
            // for both topic and name to ensure more reliable and better matching
            String query = "SELECT * FROM quizzes WHERE LOWER(quizTopic) LIKE ? OR LOWER(quizName) LIKE ?";

            PreparedStatement statement = connection.prepareStatement(query);
            var phrase = "%" + topic.toLowerCase().trim() + "%";
            statement.setString(1, phrase);
            statement.setString(2, phrase);

            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                //might be some problems with the question id and quizID?
                Quiz quiz = new Quiz(
                        rs.getString("quizName"),
                        rs.getString("quizTopic"),
                        rs.getString("quizMode"),
                        rs.getString("difficulty"),
                        rs.getString("yearLevel"),
                        rs.getString("country"),
                        rs.getInt("creatorID")
                );
                quiz.setQuizID(rs.getInt("id"));
                quizzes.add(quiz);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return quizzes;
    }

    @Override
    public List<Quiz> getAllQuizzes() {
        List<Quiz> quizzes = new ArrayList<>();
        try {
            String query = "SELECT * FROM quizzes";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Quiz quiz = new Quiz(
                        rs.getString("quizName"),
                        rs.getString("quizTopic"),
                        rs.getString("quizMode"),
                        rs.getString("difficulty"),
                        rs.getString("yearLevel"),
                        rs.getString("country"),
                        rs.getInt("creatorID")
                );
                quiz.setQuizID(rs.getInt("id"));
                quizzes.add(quiz);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return quizzes;
    }

    @Override
    public Quiz getQuizByName(String name) {
        Quiz quiz = null;
        try {
            String query = "SELECT * FROM quizzes where quizName = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, name);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                //might be some problems with the question id and quizID?
                quiz = new Quiz(
                        rs.getString("quizName"),
                        rs.getString("quizTopic"),
                        rs.getString("quizMode"),
                        rs.getString("difficulty"),
                        rs.getString("yearLevel"),
                        rs.getString("country"),
                        rs.getInt("creatorID")
                );
                quiz.setQuizID(rs.getInt("id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return quiz;
    }

    @Override
    public List<Quiz> getQuizzesByCreator(int creatorID) {
        List<Quiz> quizzes = new ArrayList<>();
        try {
            String query = "SELECT * FROM quizzes where creatorID = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, creatorID);
            ResultSet rs = statement.executeQuery(query);
            while (rs.next()) {
                //might be some problems with the question id and quizID?
                Quiz quiz = new Quiz(
                        rs.getString("quizName"),
                        rs.getString("quizTopic"),
                        rs.getString("quizMode"),
                        rs.getString("difficulty"),
                        rs.getString("yearLevel"),
                        rs.getString("country"),
                        creatorID
                );
                quiz.setQuizID(rs.getInt("id"));
                quizzes.add(quiz);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return quizzes;
    }


    @Override
    public void addQuestionToQuiz(String username) {

    }

    @Override
    public void removeQuestionFromQuiz(String emailaddress) {

    }


    @Override
    public List<Question> getQuestionsForQuiz(Quiz quiz) {
        List<Question> questions = new ArrayList<>();
        int quizID = quiz.getQuizID();
        try {
            String query = "SELECT * FROM questions where quizID = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, quizID);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Question question = new Question(
                        rs.getInt("quizID"),
                        rs.getString("questionText"),
                        rs.getString("correctAnswer"),
                        rs.getString("incorrectAnswer1"),
                        rs.getString("incorrectAnswer1"),
                        rs.getString("incorrectAnswer1")
                );
                question.setQuestionID(rs.getInt("questionID"));
                questions.add(question);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return questions;
    }

    public int getNumberOfQuestions(Quiz quiz) {
        List<Question> questions = getQuestionsForQuiz(quiz);
        long numOfQuestions = questions.size();
        return ((int) numOfQuestions);

    }

}

