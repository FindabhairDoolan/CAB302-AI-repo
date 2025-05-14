package com.example.quizapp.data;

import com.example.quizapp.Models.*;
import java.sql.Connection;
import java.sql.Statement;

/*
This class is for initializing the database. It adds two initial users, alice and bob, but does not
remove users created by you. Other tables are cleaned and initialized in every function call.
 */

public class DatabaseSeeder {

    private final Connection connection;
    private final SqliteUserDAO userDAO;
    private final SqliteQuizDAO quizDAO;
    private final SqliteQuestionDAO questionDAO;
    private final SqliteQuizAttemptDAO quizAttemptDAO;
    private final AuthManager authManager;

    public DatabaseSeeder() {
        this.connection = SqliteConnection.getInstance();
        this.userDAO = new SqliteUserDAO();
        this.quizDAO = new SqliteQuizDAO();
        this.questionDAO = new SqliteQuestionDAO();
        this.quizAttemptDAO = new SqliteQuizAttemptDAO();
        this.authManager = AuthManager.getInstance();
    }

    public void seed() {
        try {
            Statement stmt = connection.createStatement();
            stmt.executeUpdate("DELETE FROM quizAttempts");
            stmt.executeUpdate("DELETE FROM questions");
            stmt.executeUpdate("DELETE FROM quizzes");
            //stmt.executeUpdate("DELETE FROM users");

            User alice = userDAO.getUserByEmail("alice@example.com");
            if (alice == null) {
                userDAO.addUser(new User("alice", "alice@example.com", authManager.hashPassword("password1")));
                alice = userDAO.getUserByEmail("alice@example.com");
            }

            User bob = userDAO.getUserByEmail("bob@example.com");
            if (bob == null) {
                userDAO.addUser(new User("bob", "bob@example.com", authManager.hashPassword("password2")));
                bob = userDAO.getUserByEmail("bob@example.com");
            }

            int aliceID = alice.getUserID();
            int bobID = bob.getUserID();

            Quiz quiz1 = new Quiz("Algebra Basics", "Mathematics", "Algebra","Online", "Easy", "Year 10", "Australia", aliceID);
            Quiz quiz2 = new Quiz("Photosynthesis", "Biology", "Photosynthesis", "Online", "Medium", "Year 11", "Australia", bobID);
            Quiz quiz3 = new Quiz("World War II", "History", "World War II", "Online", "Easy", "Year 10", "Australia", aliceID);
            quizDAO.addQuiz(quiz1);
            quizDAO.addQuiz(quiz2);
            quizDAO.addQuiz(quiz3);

            int quiz1ID = quizDAO.getQuizByName("Algebra Basics").getQuizID();
            int quiz2ID = quizDAO.getQuizByName("Photosynthesis").getQuizID();
            int quiz3ID = quizDAO.getQuizByName("World War II").getQuizID();

            for (int i = 1; i <= 5; i++) {
                questionDAO.addQuestion(new Question(quiz1ID, "What is 2 + " + i + "?", "" + (2 + i), "" + (2 + i + 1), "" + (2 + i - 1), "" + (2 * i)));
                questionDAO.addQuestion(new Question(quiz2ID, "What is produced in photosynthesis step " + i + "?", "Oxygen", "Carbon Dioxide", "Water", "Glucose"));
                questionDAO.addQuestion(new Question(quiz3ID, "Who led Germany in WW2 (Q" + i + ")?", "Hitler", "Stalin", "Churchill", "Roosevelt"));
            }

            quizAttemptDAO.addQuizAttempt(new QuizAttempt(quiz1ID, aliceID, 4));
            quizAttemptDAO.addQuizAttempt(new QuizAttempt(quiz2ID, aliceID, 3));
            quizAttemptDAO.addQuizAttempt(new QuizAttempt(quiz3ID, aliceID, 5));
            quizAttemptDAO.addQuizAttempt(new QuizAttempt(quiz1ID, aliceID, 4));

            quizAttemptDAO.addQuizAttempt(new QuizAttempt(quiz1ID, bobID, 3));
            quizAttemptDAO.addQuizAttempt(new QuizAttempt(quiz2ID, bobID, 5));
            quizAttemptDAO.addQuizAttempt(new QuizAttempt(quiz3ID, bobID, 2));
            quizAttemptDAO.addQuizAttempt(new QuizAttempt(quiz3ID, bobID, 4));

            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
