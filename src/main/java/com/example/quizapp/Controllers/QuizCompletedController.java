package com.example.quizapp.Controllers;

import com.example.quizapp.Models.Quiz;
import com.example.quizapp.Models.SqliteQuestionDAO;
import com.example.quizapp.Models.SqliteQuizDAO;
import com.example.quizapp.utils.SceneManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.DecimalFormat;
import java.time.Duration;

/**
 * Controller for the quiz completion screen.
 * Displays the results of the completed quiz including score, difficulty, year level, and time taken.
 * Allows the user to finish and return to the home screen or retake the quiz.
 */
public class QuizCompletedController {

    @FXML
    private Button finishButton;

    @FXML
    private Label finalScoreLabel;

    @FXML
    private Label questionCountLabel;

    @FXML
    private Label difficultyYearLabel;

    @FXML
    private Label timeLabel;

    private int correctAnswers;
    private int totalQuestions;
    private String difficulty;
    private String yearLevel;
    private String mode;
    private int timerSeconds;

    @FXML
    private Button retakeButton;


    private Quiz quizTaken;
    /**
     * Sets the quiz object for the current quiz session.
     *
     * @param quiz the Quiz object taken by the user
     */
    public void setQuiz(Quiz quiz) {
        this.quizTaken = quiz;
    }

    /**
     * Handles the action when the finish button is clicked.
     * Navigates the user back to the home screen.
     */
    @FXML
    public void handleFinish() {

        Stage stage = (Stage) finishButton.getScene().getWindow();

        // Load the WelcomePage when finish button is clicked
        SceneManager.switchScene("/com/example/quizapp/home.fxml", "Home");
    }

    /**
     * Handles the action when the retake quiz button is clicked.
     * Loads the quiz screen and initializes it with the current quiz and settings.
     */
    @FXML
    private void handleRetakeQuiz() {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/quizapp/quiz.fxml"));
            Parent root = loader.load();

            QuizController quizController = loader.getController();
            quizController.setQuiz(quizTaken, mode);

            int numOfQs = new SqliteQuestionDAO().getNumberOfQuestions(quizTaken);
            quizController.setTotalQuestions(numOfQs);

            Stage stage = (Stage) retakeButton.getScene().getWindow();
            Scene scene = new Scene(root, 800, 550);
            stage.setScene(scene);
            stage.setTitle("Take quiz");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the current quiz attempt's results and displays them on page
     * @param correctAnswers the number of correct answers
     * @param totalQuestions the total questions in the quiz
     * @param difficulty the difficulty of the quiz
     * @param yearLevel the year level the quiz was designed for
     * @param mode the quiz mode
     * @param timerSeconds the remaining time on the timer in seconds
     */
    public void setResults(int correctAnswers, int totalQuestions, String difficulty, String yearLevel, String mode, int timerSeconds) {
        this.correctAnswers = correctAnswers;
        this.totalQuestions = totalQuestions;
        this.difficulty = difficulty;
        this.yearLevel = yearLevel;
        this.mode = mode;
        this.timerSeconds = timerSeconds;

        // Calculate the percentage
        double percentage = ((double) correctAnswers / totalQuestions) * 100;

        //Convert time into hours, minutes and seconds
        int hours = timerSeconds / 3600;
        int minutes = (timerSeconds % 3600) / 60;
        int seconds = timerSeconds % 60;

        finalScoreLabel.setText(String.format("%.2f%%", percentage));
        questionCountLabel.setText(String.format("✔ Final Score: %d/%d", correctAnswers, totalQuestions));
        difficultyYearLabel.setText(
                "🎓 " + yearLevel +
                        "\n🛠 Difficulty: " + difficulty
        );

        if (mode.equals("Exam")) {
            timeLabel.setText(
                    "⏱ " + String.format("%02d:%02d:%02d", hours, minutes, seconds)
            );
        } else {
            timeLabel.setText("⏱ --:--:--");
        }
    }

}