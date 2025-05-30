package com.example.quizapp.Models;

import javafx.scene.control.ComboBox;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The Sqlite Quiz DAO, communicates with the quizzes table in the database and modifies data
 */
public class SqliteQuizDAO implements IQuizDAO {

    private Connection connection;
    private ComboBox<Integer> questionDropdown;

    /**
     * The constructor for the Sqlite Quiz DAO
     */
    public SqliteQuizDAO() {
        connection = SqliteConnection.getInstance();
        createTable();
        initializeQuestionDropdown();
        //insertSampleData(); //for testing
    }

    /**
     * Initialises the question dropdown with defined values
     */
    private void initializeQuestionDropdown() {
        questionDropdown = new ComboBox<>(); // Initialize the ComboBox
        // Add number of questions options
        questionDropdown.getItems().addAll(5, 10, 15, 20, 25);
        // Set default value to 5 questions
        questionDropdown.setValue(5);
    }

    /**
     * Gets the Question Dropdown combo box
     * @return the Question dropdown combo box
     */
    public ComboBox<Integer> getQuestionDropdown() {
        return questionDropdown; // Return the ComboBox
    }

    /**
     * Retrieves the quiz timer in seconds from the AI JSON response
     * @param JSONResponse
     * @return the timer for the quiz in seconds
     */
    public int retrieveTimer(String JSONResponse){
        JSONObject json = new JSONObject(JSONResponse);
        JSONArray quizArray = json.getJSONArray("Quiz");
        JSONObject timerObj = quizArray.getJSONObject(0);

        return timerObj.getInt("timerSeconds");
    }

    /**
     * Creates the quizzes table in the sqlite database if it does not exist
     */
    private void createTable() {
        // Create table if not exists
        try {
            Statement statement = connection.createStatement();
            String query = "CREATE TABLE IF NOT EXISTS quizzes ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "name VARCHAR NOT NULL,"
                    + "subject    VARCHAR NOT NULL,"
                    + "topic VARCHAR NOT NULL,"
                    + "timer INTEGER NOT NULL,"
                    + "difficulty VARCHAR NOT NULL," //is "not null" necessary?
                    + "yearLevel VARCHAR NOT NULL," //is "not null" necessary?
                    + "country VARCHAR NOT NULL," //is "not null" necessary?
                    + "visibility VARCHAR,"
                    + "creatorID INTEGER NOT NULL,"
                    + "FOREIGN KEY (creatorID) REFERENCES users(id) ON DELETE SET NULL" //or on delete cascade?
                    + ")";
            statement.execute(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a quiz to the sqlite database quizzes table
     * @param quiz the quiz to be stored in the database
     */
    @Override
    public void addQuiz(Quiz quiz) {
        try {
            String query = "INSERT INTO quizzes (name, subject, topic, timer, difficulty, yearLevel, country, visibility, creatorID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, quiz.getName());
            statement.setString(2, quiz.getSubject());
            statement.setString(3, quiz.getTopic());
            statement.setInt(4, quiz.getTimer());
            statement.setString(5, quiz.getDifficulty());
            statement.setString(6, quiz.getYearLevel());
            statement.setString(7, quiz.getCountry());
            statement.setString(8, "Public");
            statement.setInt(9, quiz.getCreatorID());
            statement.executeUpdate();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates the quiz in the database with new values
     * @param quiz the quiz with updated values
     */
    @Override
    public void updateQuiz(Quiz quiz) {
        try {
            String query = "UPDATE quizzes SET name = ?, subject = ?, topic = ?, timer = ?, difficulty = ?, yearLevel = ?, country = ?, visibility = ?, creatorID = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, quiz.getName());
            statement.setString(2, quiz.getSubject());
            statement.setString(3, quiz.getTopic());
            statement.setInt(4, quiz.getTimer());
            statement.setString(5, quiz.getDifficulty());
            statement.setString(6, quiz.getYearLevel());
            statement.setString(7, quiz.getCountry());
            statement.setString(8, quiz.getVisibility());
            statement.setInt(9, quiz.getCreatorID());
            statement.setInt(10, quiz.getQuizID()); // Ensure the WHERE clause works

            // 🔥 Actually execute the update
            statement.executeUpdate();

            System.out.println("Quiz DAO: visibility updated to " + quiz.getVisibility());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes the quiz from the sqlite database quizzes table
     * @param quiz the quiz to be deleted
     */
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

    /**
     * Searches for quizzes in the sqlite database quizzes table by topic
     * @param topic the topic of the quiz
     * @return A list of quizzes with the same subject
     */
    @Override
    public List<Quiz> searchQuiz(String topic) {
        List<Quiz> quizzes = new ArrayList<>();
        try {
            //query and database are changed to lowercase and, and search is
            // for both topic and name to ensure more reliable and better matching
            String query = "SELECT * FROM quizzes WHERE LOWER(topic) LIKE ? OR LOWER(name) LIKE ?";

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
                        rs.getInt("timer"),
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

    /**
     * Retrieves all the quizzes from the sqlite database quizzes table
     * @return A list of all quizzes in the database
     */
    @Override
    public List<Quiz> getAllQuizzes() {
        List<Quiz> quizzes = new ArrayList<>();
        try {
            String query = "SELECT * FROM quizzes";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Quiz quiz = new Quiz(
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
                quiz.setQuizID(rs.getInt("id"));
                quizzes.add(quiz);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return quizzes;
    }

    /**
     * Searches for the quiz in the sqlite database quizzes table by name
     * @param name the name of the quiz
     * @return the quiz with the same name, or null if not existent
     */
    @Override
    public Quiz getQuizByName(String name) {
        Quiz quiz = null;
        try {
            String query = "SELECT * FROM quizzes where name = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, name);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                //might be some problems with the question id and quizID?
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
                quiz.setQuizID(rs.getInt("id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return quiz;
    }

    /**
     * Searches for the quizzes made by the same user in the sqlite database
     * @param creatorID the ID of the user who created the quiz
     * @return A list of the quizzes the user created
     */
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
                    quiz.setQuizID(rs.getInt("id"));
                    quizzes.add(quiz);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return quizzes;
    }



    /*@Override
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
    // Added this method to load quiz by quiz for testing purpose for edit quiz page
    public Quiz getQuizById(int quizId) {
        Quiz quiz = null;
        try {
            String query = "SELECT * FROM quizzes WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, quizId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                quiz = new Quiz(
                        rs.getString("name"),
                        rs.getString("subject"),
                        rs.getString("topic"),
                        rs.getInt("mode"),
                        rs.getString("difficulty"),
                        rs.getString("yearLevel"),
                        rs.getString("country"),
                        rs.getString("visibility"),
                        rs.getInt("creatorID")
                );
                quiz.setQuizID(rs.getInt("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return quiz;
    }*/


}

