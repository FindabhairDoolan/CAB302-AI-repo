package com.example.quizapp.Controllers;

import com.example.quizapp.Models.*;
import com.example.quizapp.utils.AlertManager;
import com.example.quizapp.utils.AuthManager;
import com.example.quizapp.utils.QuizManager;
import com.example.quizapp.utils.SceneManager;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.util.Duration;
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
    @FXML
    private Label timerLabel;
    @FXML
    private Button previousButton;

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
    private String mode;
    private Timeline timer;
    private int timerSeconds;
    private boolean timerPause = false;
    private List<String> examAnswers = new ArrayList<>();

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

    public void setMode(String mode) {this.mode = mode;}

    public void setTimer(int timerSeconds) {this.timerSeconds = timerSeconds;}

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

    public void setQuiz(Quiz quiz, String mode) {
        this.quiz = quiz;

        setDifficulty(quiz.getDifficulty());
        setYearLevel(quiz.getYearLevel());
        setSubject(quiz.getSubject());
        setMode(mode);

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
            AlertManager.alertErrorWait("Quiz has no questions", "No questions found for this quiz, " +
                    "please return to the home page and add questions to this quiz in the quiz editor.");
        }

        //preload all questions if coming from another controller
        totalQuestions = questionList.size();
        if (isViewMode) this.correctAnswers = (int) (attempt.getScore() * totalQuestions / 100.0);

        if (mode.equals("Exam")){
            if (isViewMode){
                timerLabel.setVisible(true);
                timerPause = true;
            }
            else{
                timerSeconds = quiz.getMode();
                timerLabel.setVisible(true);
            }
            startTimer();

            //Initialise list of answers user has selected in exam mode
            while (examAnswers.size() < totalQuestions) {
                examAnswers.add(null);
            }
        }
        else{ //If practice mode, no timer and timer is -1 to symbolise no time in attempt
            timerLabel.setVisible(false);
            timerSeconds = -1;
        }

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
     * Loads question differently based on exam, viewing and practice mode.
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

        //If it's exam mode and it's not question 1, display the previous button
        if(mode.equals("Exam")){
            if(questionIndex > 1){
                previousButton.setVisible(true);
                previousButton.setManaged(true);
            }
            else{
                previousButton.setVisible(false);
                previousButton.setManaged(false);
            }

            String selectedAnswer = examAnswers.get(questionIndex - 1);
            for (Toggle toggle : answerToggleGroup.getToggles()) {
                RadioButton rb = (RadioButton) toggle;
                String text = rb.getText();

                boolean isSelected = text.equals(selectedAnswer);

                if (isSelected) {
                    answerToggleGroup.selectToggle(rb);
                }
            }

        }

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
                } else if (isCorrect) {
                    rb.setStyle("-fx-font-weight: bold; -fx-text-fill: green;");
                }
            }
            if(selectedAnswer == null){
                feedbackLabel.setText("Incorrect");
            }

            feedbackLabel.setVisible(true);
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
     * In practice mode: evaluates the user's answer and displays feedback on first press
     * On second press it displays the next question
     * In exam mode: goes to the next question
     */
    @FXML
    public void onNext() {
        if (mode.equals("Exam")){
            if (questionIndex < totalQuestions) {
                RadioButton selected = (RadioButton) answerToggleGroup.getSelectedToggle();

                if (selected != null){
                    String selectedText = selected.getText();
                    examAnswers.set(questionIndex - 1, selectedText);
                }

                questionIndex++;
                loadQuestion(questionList.get(questionIndex - 1));
            } else {
                RadioButton selected = (RadioButton) answerToggleGroup.getSelectedToggle();

                if (selected != null){
                    String selectedText = selected.getText();
                    examAnswers.set(questionIndex - 1, selectedText);
                }
                handleExamFinish();
            }
        }
        else if (!showingFeedback) {
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

    /**
     * Sends the user to the previous question in Exam mode
     */
    @FXML
    public void onPrevious() {
        RadioButton selected = (RadioButton) answerToggleGroup.getSelectedToggle();

        if (selected != null){
            String selectedText = selected.getText();
            examAnswers.set(questionIndex - 1, selectedText);
        }

        questionIndex--;
        loadQuestion(questionList.get(questionIndex - 1));
    }

    /**
     * Adds quiz attempt to the database
     */
    private void saveQuizAttemptToDatabase() {
        QuizAttempt attempt = new QuizAttempt(quiz.getQuizID(), user.getUserID(), (correctAnswers * 100.0) / totalQuestions, timerSeconds, selectedAnswers);
        quizAttemptDAO.addQuizAttempt(attempt);
    }

    /**
     * Sends the user to the quiz completion page
     */
    private void showQuizCompletedScreen() {

        try {
            timerPause = true;

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/quizapp/quiz-completed.fxml"));
            Parent root = fxmlLoader.load();

            QuizCompletedController QCcontroller = fxmlLoader.getController();
            QCcontroller.setQuiz(quiz);
            QCcontroller.setResults(correctAnswers, totalQuestions, difficulty, yearLevel, mode, timerSeconds);
            Stage stage = (Stage) nextButton.getScene().getWindow();
            Scene scene = new Scene(root, 800, 550);
            stage.setScene(scene);
            stage.setTitle("Quiz Result");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initialises the timer if in exam mode
     */
    private void startTimer() {
        updateLabel(); // Set initial time

        timer = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            if (!timerPause){ //Only reduce time if not paused
                timerSeconds--;
                updateLabel();

                if (timerSeconds <= 0) {
                    timer.stop();
                    onTimeUp();
                }
            }
        }));
        timer.setCycleCount(Timeline.INDEFINITE);
        timer.play();
    }

    /**
     * Update the timer label every second
     */
    private void updateLabel() {
        int hours = timerSeconds / 3600;
        int minutes = (timerSeconds % 3600) / 60;
        int seconds = timerSeconds % 60;

        timerLabel.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
    }

    /**
     * When the timer reaches 0 time remaining, force ends the quiz
     */
    private void onTimeUp() {

        AlertManager.alertErrorShow("Time's up!", "Timer has reached 00:00:00, final results are calculated.");

        //Get selected answer on current page if there is one and update the list of answers
        RadioButton selected = (RadioButton) answerToggleGroup.getSelectedToggle();
        if (selected != null){
            String selectedText = selected.getText();
            examAnswers.set(questionIndex - 1, selectedText);
        }

        selectedAnswers = examAnswers;

        //Check how many answers were correct
        for (int i = 0; i < selectedAnswers.size(); i++) {
            Question currentQuestion = questionList.get(i);
            String correctText = currentQuestion.getCorrectAnswer();
            String selectedText = selectedAnswers.get(i);

            if (selectedText != null && selectedText.equals(correctText)) {
                correctAnswers++;
            }
        }

        saveQuizAttemptToDatabase();
        showQuizCompletedScreen();
    }

    /**
     * Confirms user is satisfied with exam answers
     */
    @FXML
    public void handleExamFinish() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Finish Quiz");
        alert.setHeaderText(null);
        alert.setContentText("This is the last question, are you sure you are satisfied with your answers?");

        // Define Yes and No buttons
        ButtonType yesButton = new ButtonType("Yes");
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

        // Set the buttons to the alert
        alert.getButtonTypes().setAll(yesButton, noButton);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == yesButton) {
            // User chose Yes – finish quiz
            selectedAnswers = examAnswers;

            for (int i = 0; i < selectedAnswers.size(); i++) {
                Question currentQuestion = questionList.get(i);
                String correctText = currentQuestion.getCorrectAnswer();
                String selectedText = selectedAnswers.get(i);

                if (selectedText != null && selectedText.equals(correctText)) {
                    correctAnswers++;
                }
            }

            if (!isViewMode) saveQuizAttemptToDatabase();
            showQuizCompletedScreen();
        }
        // If No is selected, do nothing
    }
}

