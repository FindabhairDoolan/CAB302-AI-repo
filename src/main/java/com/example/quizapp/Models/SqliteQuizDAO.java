package com.example.quizapp.Models;

import javafx.scene.control.ComboBox;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SqliteQuizDAO implements IQuizDAO {

    private Connection connection;
    private ComboBox<Integer> questionDropdown;

    public SqliteQuizDAO() {
        connection = SqliteConnection.getInstance();
        createTable();
        initializeQuestionDropdown();
        //insertSampleData(); //for testing, to be removed later
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



    private void createTable() {
        // Create table if not exists
        try {
            Statement statement = connection.createStatement();
            String query = "CREATE TABLE IF NOT EXISTS quizzes ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "quizName VARCHAR NOT NULL,"
                    + "subject    VARCHAR NOT NULL,"
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
            String query = "INSERT INTO quizzes (quizName, subject, quizTopic, quizMode, difficulty, yearLevel, country, creatorID) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, quiz.getName());
            statement.setString(2, quiz.getSubject());
            statement.setString(3, quiz.getTopic());
            statement.setString(4, quiz.getMode());
            statement.setString(5, quiz.getDifficulty());
            statement.setString(6, quiz.getYearLevel());
            statement.setString(7, quiz.getCountry());
            statement.setInt(8, quiz.getCreatorID());
            statement.executeUpdate();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateQuiz(Quiz quiz) {
        try {
            String query = "UPDATE quizzes SET quizName = ?, subject = ?, quizTopic = ?, quizMode = ?, difficulty = ?, yearLevel = ?, country = ?, creatorID = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, quiz.getName());
            statement.setString(2, quiz.getSubject());
            statement.setString(3, quiz.getTopic());
            statement.setString(4, quiz.getMode());
            statement.setString(5, quiz.getDifficulty());
            statement.setString(6, quiz.getYearLevel());
            statement.setString(7, quiz.getCountry());
            statement.setInt(8, quiz.getCreatorID());
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
    public List<Quiz> searchQuiz(String topic) {
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
                        rs.getString("Name"),
                        rs.getString("subject"),
                        rs.getString("Topic"),
                        rs.getString("Mode"),
                        rs.getString("difficulty"),
                        rs.getString("yearLevel"),
                        rs.getString("country"),
                        rs.getString("visibility"),
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
                        rs.getString("subject"),
                        rs.getString("quizTopic"),
                        rs.getString("quizMode"),
                        rs.getString("difficulty"),
                        rs.getString("yearLevel"),
                        rs.getString("country"),
                        rs.getString("visibility"),
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
                        rs.getString("subject"),
                        rs.getString("quizTopic"),
                        rs.getString("quizMode"),
                        rs.getString("difficulty"),
                        rs.getString("yearLevel"),
                        rs.getString("country"),
                        rs.getString("visibility"),
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
        String sql = "SELECT * FROM quizzes WHERE creatorID = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            // bind the creatorID
            statement.setInt(1, creatorID);

            // execute the query
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    Quiz quiz = new Quiz(
                            rs.getString("quizName"),
                            rs.getString("subject"),
                            rs.getString("quizTopic"),
                            rs.getString("quizMode"),
                            rs.getString("difficulty"),
                            rs.getString("yearLevel"),
                            rs.getString("country"),
                            rs.getString("visibility"),
                            rs.getInt("creatorID")
                    );
                    quiz.setQuizID(rs.getInt("id"));
                    quizzes.add(quiz);
                }
            }
        } catch (SQLException e) {
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

