package com.example.quizapp.Controllers;

import com.example.quizapp.Models.*;
import com.example.quizapp.utils.AlertManager;
import com.example.quizapp.utils.AuthManager;
import com.example.quizapp.utils.QuizManager;
import com.example.quizapp.utils.SceneManager;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import java.util.*;

public class QuizController {

    //Declaration of FXML variables
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


    //Declaration of further variables
    User user = AuthManager.getInstance().getCurrentUser();
    private boolean showingFeedback = false;
    private int questionIndex = 1;
    private int totalQuestions;
    private List<Question> questionList;
    SqliteQuestionDAO questionDAO = new SqliteQuestionDAO();
    SqliteQuizAttemptDAO quizAttemptDAO = new SqliteQuizAttemptDAO();
    private Quiz quiz = QuizManager.getInstance().getCurrentQuiz();
    private List<String> selectedAnswers = new ArrayList<>();

    private int correctAnswers = 0;
    private String difficulty;
    private String yearLevel;
    private String subject;

    private boolean isViewMode = false;
    private List<String> previousAnswers = new ArrayList<>();
    private QuizAttempt attempt;


    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public void setYearLevel(String yearLevel) {
        this.yearLevel = yearLevel;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setViewMode(boolean viewMode, QuizAttempt attempt) {
        this.isViewMode = viewMode;
        this.previousAnswers = attempt.getAnswers();
        this.attempt = attempt;
    }
    /**
     * Exits the quiz and returns to home page
     */
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
            SceneManager.switchScene("/com/example/quizapp/home.fxml", "Dashboard");
        }
        // If No is selected, do nothing
    }

    /**
     * Initialises the Quiz page
     */
    @FXML
    public void initialize() {}

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;

        setDifficulty(quiz.getDifficulty());
        setYearLevel(quiz.getYearLevel());
        setSubject(quiz.getSubject());
        setMode(quiz.getMode());

        loadQuiz();
    }

    /**
     * Loads the quiz by getting quiz questions from database
     * Also sets progress label data
     */
    private void loadQuiz() {

        // If quizID is null, use default (1)
        int idToLoad = (quiz != null) ? quiz.getQuizID() : 1;
        questionList = questionDAO.getQuestionsForQuiz(idToLoad);

        if (questionList.isEmpty()) {
            //System.err.println("No questions found for quiz ID " + idToLoad);
            //Show an alert dialog to the user
            AlertManager.alertError("Quiz has no questions", "No questions found for this quiz, " +
                    "please return to the home page and add questions to this quiz in the quiz editor.");
        }

        //preload all questions if coming from another controller
        totalQuestions = questionList.size();
        if (isViewMode) this.correctAnswers = (int) (attempt.getScore() * totalQuestions / 100.0);

        loadQuestion(questionList.get(questionIndex - 1));
        updateProgressLabel(); // Update the progress label on initialization
        setTotalQuestions(totalQuestions);
    }

    /**
     * Sets the total questions in the quiz and updates the quiz progress
     * @param total The total number of questions in the quiz
     */
    public void setTotalQuestions(int total) {
        this.totalQuestions = total; // Set the total number of questions
        updateProgressLabel(); // Update the progress label when total questions are set
    }

    /**
     * Loads the question by resetting traces from previous question and displaying
     * the new question and answers for the next question in the quiz.
     * @param question A list of the question objects for this generated quiz
     */
    private void loadQuestion(Question question) {
        feedbackLabel.setVisible(false);
        answerToggleGroup.selectToggle(null);
        showingFeedback = false;

        //Enable changing answer before submitting answer
        option1.setDisable(isViewMode);
        option2.setDisable(isViewMode);
        option3.setDisable(isViewMode);
        option4.setDisable(isViewMode);

        //Display question and answers
        questionsLabel.setText(question.getQuestionText());
        List<String> answers = question.getShuffledAnswers();
        option1.setText(answers.get(0));
        option2.setText(answers.get(1));
        option3.setText(answers.get(2));
        option4.setText(answers.get(3));

        updateProgressLabel(); // Updating the progress heading as each question is done
        nextButton.setVisible(true);
        if (isViewMode && previousAnswers.size() >= questionIndex) {
            showingFeedback = true;
            String selectedAnswer = previousAnswers.get(questionIndex - 1);
            String correctAnswer = question.getCorrectAnswer();
            for (Toggle toggle : answerToggleGroup.getToggles()) {
                RadioButton rb = (RadioButton) toggle;
                String text = rb.getText();
                rb.setStyle("");  // reset style first

                boolean isCorrect = text.equals(correctAnswer);
                boolean isSelected = text.equals(selectedAnswer);

                if (isSelected) {
                    answerToggleGroup.selectToggle(rb);
                    if (isCorrect) {
                        rb.setStyle("-fx-font-weight: bold; -fx-text-fill: green;");
                        feedbackLabel.setText("Correct");
                    } else {
                        feedbackLabel.setText("Incorrect");
                    }
                    feedbackLabel.setVisible(true);
                } else if (isCorrect) {
                    rb.setStyle("-fx-font-weight: bold; -fx-text-fill: green;");
                }
            }
        }
        updateProgressLabel();
    }

    /**
     * Updates the progress of the quiz
     */
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

            //Disable changing answer during feedback
            option1.setDisable(true);
            option2.setDisable(true);
            option3.setDisable(true);
            option4.setDisable(true);


            String selectedText = selected.getText();
            Question currentQuestion = questionList.get(questionIndex - 1);
            boolean isCorrect = selectedText.equals(currentQuestion.getCorrectAnswer());
            selectedAnswers.add(selectedText);
            showingFeedback = true;
            feedbackLabel.setText(isCorrect ? "Correct" : "Incorrect");
            feedbackLabel.setVisible(true);

            // Increment correctAnswers if the answer is correct
            if (isCorrect) {
                correctAnswers++;
            }
        }
        else {
            if (questionIndex < totalQuestions) {
                questionIndex++;
                loadQuestion(questionList.get(questionIndex - 1));
            } else {
                if (!isViewMode) saveQuizAttemptToDatabase();
                showQuizCompletedScreen();
            }
        }
    }

    private void saveQuizAttemptToDatabase() {
        QuizAttempt attempt = new QuizAttempt(quiz.getQuizID(), user.getUserID(), (correctAnswers * 100.0) / totalQuestions, selectedAnswers);
        quizAttemptDAO.addQuizAttempt(attempt);
    }

    /**
     * Sends the user to the quiz completion page
     */
    private void showQuizCompletedScreen() {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/quizapp/quiz-completed.fxml"));
            Parent root = fxmlLoader.load();

            QuizCompletedController QCcontroller = fxmlLoader.getController();
            QCcontroller.setQuiz(quiz);
            QCcontroller.setResults(correctAnswers, totalQuestions, difficulty, yearLevel);
            Stage stage = (Stage) nextButton.getScene().getWindow();
            Scene scene = new Scene(root, 800, 550);
            stage.setScene(scene);
            stage.setTitle("Quiz Result");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setMode(String mode) {
    }
}

