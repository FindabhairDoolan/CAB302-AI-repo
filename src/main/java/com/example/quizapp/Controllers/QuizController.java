package com.example.quizapp.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import java.util.Optional;

public class QuizController {

    @FXML
    private Label feedbackLabel;

    @FXML
    private Button exitButton;

    @FXML
    public void handleExit() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Exit Quiz");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to exit? Your progress might be lost.");

        // Define Yes and No buttons
        ButtonType yesButton = new ButtonType("Yes");
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

        // Set the buttons to the alert
        alert.getButtonTypes().setAll(yesButton, noButton);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == yesButton) {
            // User chose Yes – go to dashboard
            Stage stage = (Stage) exitButton.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/quizapp/WelcomePage.fxml"));
            try {
                Scene scene = new Scene(fxmlLoader.load(), 800, 550);
                stage.setScene(scene);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // If No is selected, do nothing
    }

    /**
     * Evaluates the user's answer and displays feedback on first press
     * On second press it displays the next question
     * @param actionEvent
     */
    public void onNext(ActionEvent actionEvent) {
    }

    // Placeholder method to check the answer
    private boolean checkAnswer(String answer) {
        // Replace with actual AI logic later
        // For now, the correct answer is "Option A"
        return "Option A".equals(answer);
    }
}
