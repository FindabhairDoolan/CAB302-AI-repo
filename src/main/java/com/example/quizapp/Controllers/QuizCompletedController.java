package com.example.quizapp.Controllers;

import com.example.quizapp.Models.Quiz;
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

public class QuizCompletedController {

    @FXML
    private Button finishButton;

    @FXML
    private Label finalScoreLabel;

    @FXML
    private Label questionCountLabel;

    @FXML
    private Label difficultyYearLabel;

    private int correctAnswers;
    private int totalQuestions;
    private String difficulty;
    private String yearLevel;

    @FXML
    private Button retakeButton;


    private Quiz quizTaken;

    public void setQuiz(Quiz quiz) {
        this.quizTaken = quiz;
    }

    @FXML
    public void handleFinish() {

        Stage stage = (Stage) finishButton.getScene().getWindow();

        // Load the WelcomePage when finish button is clicked
        SceneManager.switchScene("/com/example/quizapp/home.fxml", "Home");
    }

    @FXML
    private void handleRetakeQuiz() {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/quizapp/quiz.fxml"));
            Parent root = loader.load();

            QuizController quizController = loader.getController();
            quizController.setQuiz(quizTaken);

            int numOfQs = new SqliteQuizDAO().getNumberOfQuestions(quizTaken);
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

    public void setResults(int correctAnswers, int totalQuestions, String difficulty, String yearLevel) {
        this.correctAnswers = correctAnswers;
        this.totalQuestions = totalQuestions;
        this.difficulty = difficulty;
        this.yearLevel = yearLevel;

        // Calculate the percentage
        double percentage = ((double) correctAnswers / totalQuestions) * 100;

        finalScoreLabel.setText(String.format("%.2f%%", percentage));
        questionCountLabel.setText(String.format("✔ Final Score: %d/%d", correctAnswers, totalQuestions));
        difficultyYearLabel.setText(String.format("🎓 %s%n🛠  ️Difficulty: %s", yearLevel, difficulty));
        //timeLabel.setText("⏱️  " + String.format("%02d:%02d", mins, secs));
    }

}