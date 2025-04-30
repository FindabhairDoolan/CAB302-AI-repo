package com.example.quizapp.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class QuizCompletedController {

    @FXML
    private Button finishButton;

    @FXML
    public void handleFinish() {

        Stage stage = (Stage) finishButton.getScene().getWindow();

        // Load the WelcomePage when finish button is clicked
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/quizapp/WelcomePage.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load(), 800, 550);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}