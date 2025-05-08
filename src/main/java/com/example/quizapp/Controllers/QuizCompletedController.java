package com.example.quizapp.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.DecimalFormat;

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
    public void handleFinish() {

        Stage stage = (Stage) finishButton.getScene().getWindow();

        // Load the WelcomePage when finish button is clicked
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/quizapp/home.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load(), 800, 550);
            stage.setScene(scene);
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
        questionCountLabel.setText(String.format("Final Score: %d/%d", correctAnswers, totalQuestions));
        difficultyYearLabel.setText(String.format("Year: %s, Difficulty: %s", yearLevel, difficulty));
    }
}