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

/**
 * Controller class for the Quiz page
 */
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

    /**
     * Set the difficulty of the current quiz on the quiz page
     * @param difficulty the difficulty of the current quiz
     */
    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    /**
     * Set the year level of the current quiz on the quiz page
     * @param yearLevel the year level the current quiz was designed for
     */
    public void setYearLevel(String yearLevel) {
        this.yearLevel = yearLevel;
    }

    /**
     * Set the subject of the current quiz on the quiz page
     * @param subject the subject of the current quiz
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * Set the mode of the current quiz on the quiz page
     * @param mode the mode of the current quiz
     */
    public void setMode(String mode) {this.mode = mode;}

    /**
     * Set the timer of the current quiz on the quiz page
     * @param timerSeconds the timer in seconds of the current quiz
     */
    public void setTimer(int timerSeconds) {this.timerSeconds = timerSeconds;}

    /**
     * Set the quiz to be in view mode, the user cannot edit answers, get a new score etc
     * If in view mode, the user may only view the selected past attempt of a quiz
     * @param viewMode True: The user is viewing a past quiz attempt, False: The user is taking a quiz
     * @param attempt the attempt the user is viewing
     */
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
     * Sets the initial values of the quiz
     * @param quiz the quiz to be displayed
     * @param mode the mode of the quiz practice/exam
     */
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
     * Loads the quiz according to the viewing mode and quiz mode chosen
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
                timerSeconds = quiz.getTimer();
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

        //If it's exam mode
        if(mode.equals("Exam")){
            //If it's not question 1, display the previous button
            if(questionIndex > 1){
                previousButton.setVisible(true);
                previousButton.setManaged(true);
            }
            else{
                previousButton.setVisible(false);
                previousButton.setManaged(false);
            }

            //Checks if the user has already selected an answer for this question
            //Selects the answer if previously selected
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

        //If it is view mode
        if (isViewMode && previousAnswers.size() >= questionIndex) {
            showingFeedback = true;

            //Check what the user answered and what the correct answer is, highlight answers accordingly
            String selectedAnswer = previousAnswers.get(questionIndex - 1);
            String correctAnswer = question.getCorrectAnswer();
            for (Toggle toggle : answerToggleGroup.getToggles()) {
                RadioButton rb = (RadioButton) toggle;
                String text = rb.getText();
                rb.setStyle("");  // reset style first

                boolean isCorrect = text.equals(correctAnswer);
                boolean isSelected = text.equals(selectedAnswer);

                //If the answer was selected
                if (isSelected) {
                    answerToggleGroup.selectToggle(rb);
                    //If the answer was correct, display as green
                    if (isCorrect) {
                        rb.setStyle("-fx-font-weight: bold; -fx-text-fill: green;");
                        feedbackLabel.setText("Correct");
                    //If the answer was wrong, display as red
                    } else {
                        feedbackLabel.setText("Incorrect");
                    }
                //If this answer was the correct answer, display as green
                } else if (isCorrect) {
                    rb.setStyle("-fx-font-weight: bold; -fx-text-fill: green;");
                }
            }
            //If the user did not select an answer, the answer is incorrect
            if(selectedAnswer == null){
                feedbackLabel.setText("Incorrect");
            }

            feedbackLabel.setVisible(true);
        }
        updateProgressLabel();
    }

    /**
     * Updates the quiz progress label
     */
    private void updateProgressLabel() {
        progressLabel.setText("Question " + questionIndex + " of " + totalQuestions);
    }


    /**
     * In practice mode: evaluates the user's answer and displays feedback on first press,
     * on second press it displays the next question.
     * In exam mode: goes to the next question
     */
    @FXML
    public void onNext() {
        //If it is exam mode
        if (mode.equals("Exam")){
            //If it is not the last question
            if (questionIndex < totalQuestions) {
                RadioButton selected = (RadioButton) answerToggleGroup.getSelectedToggle();

                //If the user has selected an answer, add it to the answer list
                if (selected != null){
                    String selectedText = selected.getText();
                    examAnswers.set(questionIndex - 1, selectedText);
                }

                questionIndex++;
                loadQuestion(questionList.get(questionIndex - 1));

            //If it is the last question
            } else {
                RadioButton selected = (RadioButton) answerToggleGroup.getSelectedToggle();

                //If the user has selected an answer, add it to the answer list
                if (selected != null){
                    String selectedText = selected.getText();
                    examAnswers.set(questionIndex - 1, selectedText);
                }
                handleExamFinish();
            }
        }
        //If it is practice mode and this is the first button press
        else if (!showingFeedback) {
            RadioButton selected = (RadioButton) answerToggleGroup.getSelectedToggle();
            //If the user has not selected an answer, prompt to select an answer
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
        //If it is the second button press
        else {
            //If it is not the last question, display the next question
            if (questionIndex < totalQuestions) {
                questionIndex++;
                loadQuestion(questionList.get(questionIndex - 1));

            //If it is the last question, send user to quiz complete screen
            } else {
                //If not view mode, save the attempt to the database
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

        //If the user has selected an answer, add it to the selected answers list
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

                //If timer reaches 0, force the quiz to end
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
     * Confirms user is satisfied with exam answers if they finish early
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

            //Check how many answers were correct
            for (int i = 0; i < selectedAnswers.size(); i++) {
                Question currentQuestion = questionList.get(i);
                String correctText = currentQuestion.getCorrectAnswer();
                String selectedText = selectedAnswers.get(i);

                if (selectedText != null && selectedText.equals(correctText)) {
                    correctAnswers++;
                }
            }

            //If not view mode, save the quiz attempt to the database
            if (!isViewMode) saveQuizAttemptToDatabase();
            showQuizCompletedScreen(); //Send the user to quiz complete page
        }
        // If No is selected, do nothing
    }
}

