package com.example.quizapp.Controllers;

import com.example.quizapp.Models.Question;
import com.example.quizapp.Models.SqliteQuestionDAO;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class QuizController {

    @FXML
    private Label feedbackLabel;
    @FXML
    private Button exitButton;
    @FXML
    private Label questionsLabel;
    @FXML
    private Label progressLabel;
    @FXML
    private Button nextButton;

    @FXML
    private RadioButton option1;
    @FXML
    private RadioButton option2;
    @FXML
    private RadioButton option3;
    @FXML
    private RadioButton option4;
    @FXML
    private ToggleGroup answerToggleGroup;

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

    private boolean showingFeedback = false;
    private int questionIndex = 1;
    private int totalQuestions;
    private List<Question> questionList;
    SqliteQuestionDAO questionDAO = new SqliteQuestionDAO();

    // Placeholder, to be replaced with AI logic
    private String correctAnswer = "Option A";

    @FXML
    public void initialize() {
        questionList = questionDAO.getQuestionsForQuiz(1);
        loadQuestion(questionList.get(questionIndex - 1));
        updateProgressLabel(); // Update the progress label on initialization
    }

    public void setTotalQuestions(int total) {
        this.totalQuestions = total; // Set the total number of questions
        updateProgressLabel(); // Update the progress label when total questions are set
    }

    private void loadQuestion(Question question) {
        feedbackLabel.setVisible(false);
        answerToggleGroup.selectToggle(null);
        showingFeedback = false;

        questionsLabel.setText(question.getQuestionText());
        List<String> answers = question.getShuffledAnswers();
        option1.setText(answers.get(0));
        option2.setText(answers.get(1));
        option3.setText(answers.get(2));
        option4.setText(answers.get(3));

        updateProgressLabel(); // Updating the progress heading as each question is done
        nextButton.setVisible(true);
    }

    private void updateProgressLabel() {
        progressLabel.setText("Question " + questionIndex + " of " + totalQuestions);
    }


    /**
     * Evaluates the user's answer and displays feedback on first press
     * On second press it displays the next question
     */
    public void onNext() {
        if (!showingFeedback) {
            RadioButton selected = (RadioButton) answerToggleGroup.getSelectedToggle();
            if (selected == null) {
                feedbackLabel.setText("Please select an answer by clicking the circles.");
                feedbackLabel.setVisible(true);
                return;
            }

            String selectedText = selected.getText();
            boolean isCorrect = selectedText.equals(questionList.get(questionIndex - 1).getCorrectAnswer());

            feedbackLabel.setText(isCorrect ? "Correct" : "Incorrect");
            feedbackLabel.setVisible(true);
            showingFeedback = true;
        } else {
            if (questionIndex < totalQuestions) {
                questionIndex++;
                loadQuestion(questionList.get(questionIndex - 1));
            } else {
                showQuizCompletedScreen();
            }
        }
    }

    private void showQuizCompletedScreen() {
        Stage stage = (Stage) nextButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/quizapp/quiz-completed.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load(), 800,550);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

