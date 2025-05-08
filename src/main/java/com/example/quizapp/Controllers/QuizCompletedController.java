package com.example.quizapp.Controllers;

import com.example.quizapp.Models.Quiz;
import com.example.quizapp.Models.SqliteQuizDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class QuizCompletedController {

    @FXML
    private Button finishButton;

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
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/quizapp/home.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load(), 800, 550);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRetakeQuiz() {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/quizapp/quiz.fxml"));
            Parent root = loader.load();

            QuizController quizController = loader.getController();
            if (quizController == null) {System.out.println("wtf)");};
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
}