package com.example.quizapp.utils;

import com.example.quizapp.Controllers.QuizController;
import com.example.quizapp.Models.Quiz;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.Optional;

/**
 * The Quiz Manager class, manages the instances of quizzes
 */
public class QuizManager {
    private static QuizManager instance;
    private Quiz currentQuiz;

    private QuizManager() {}

    /**
     *Returns the singleton instance of the quiz manager class
     * @return
     */
    public static QuizManager getInstance() {
        if (instance == null) {
            instance = new QuizManager();
        }
        return instance;
    }

    /**
     * Given quiz will be the current quiz in use
     * @param quiz sets quiz to the current quiz
     */
    public void setCurrentQuiz(Quiz quiz) {
        this.currentQuiz = quiz;
    }

    /**
     * Returns the current quiz
     * @return Quiz: The current quiz
     */
    public Quiz getCurrentQuiz() {
        return this.currentQuiz;
    }

    public void openQuiz(Quiz quiz) {
        Alert quizPrompt = createQuizPrompt(quiz);
        Optional<ButtonType> result = quizPrompt.showAndWait();

        if (result.isPresent() && result.get().getText().equals("Yes")) {
            Optional<String> selectedMode = showModeSelection();

            selectedMode.ifPresent(mode -> {
                showStartQuizConfirmation(quiz, mode);
                startQuiz(quiz, mode);
            });
        }
    }

    /**
     * Creates a confirmation dialog prompting the user to start the selected quiz.
     *
     * @param quiz The quiz to be taken.
     * @return An Alert configured with quiz details and Yes/No options.
     */

    private Alert createQuizPrompt(Quiz quiz) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Start Quiz");
        alert.setHeaderText("Would you like to take " + quiz.getName() + " for "
                + quiz.getYearLevel() + " " + quiz.getSubject() + " now?");
        alert.setContentText(formatQuizInfo(quiz));

        ButtonType yes = new ButtonType("Yes");
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(yes, no);

        return alert;
    }

    /**
     * Initiates the quiz by loading the quiz scene and passing the quiz and selected mode.
     *
     * @param quiz The quiz to start.
     * @param mode The selected quiz mode ("Practice" or "Exam").
     */

    private void startQuiz(Quiz quiz, String mode) {
        try {
            //Open quiz/quiz controller from pop up
            QuizController controller = SceneManager.switchSceneWithController("/com/example/quizapp/quiz.fxml", "Quiz");

            // Now inject values
            QuizManager.getInstance().setCurrentQuiz(quiz);
            controller.setQuiz(quiz, mode);


        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Formats quiz metadata into a readable string for display in dialogs.
     *
     * @param quiz The quiz whose details are to be formatted.
     * @return A formatted string describing the quiz.
     */
    private String formatQuizInfo(Quiz quiz) {
        return String.format(
                "Quiz Name: %s%nTopic: %s%nSubject: %s%nLevel: %s%nDifficulty: %s",
                quiz.getName(),
                quiz.getTopic(),
                quiz.getSubject(),
                quiz.getYearLevel(),
                quiz.getDifficulty()
        );
    }

    /**
     * Shows a dialog prompting the user to choose between Practice and Exam mode.
     *
     * @return An Optional containing the selected mode if the user confirms, or empty if canceled.
     */
    private Optional<String> showModeSelection() {
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Select Quiz Mode");
        dialog.setHeaderText("Choose how you want to take the quiz:");

        ButtonType startButton = new ButtonType("Start", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(startButton, ButtonType.CANCEL);

        RadioButton practice = new RadioButton("Practice Mode");
        RadioButton exam = new RadioButton("Exam Mode");

        ToggleGroup modeGroup = new ToggleGroup();
        practice.setToggleGroup(modeGroup);
        exam.setToggleGroup(modeGroup);
        practice.setSelected(true); // default

        VBox content = new VBox(10, practice, exam);
        content.setPadding(new Insets(10));
        dialog.getDialogPane().setContent(content);

        dialog.setResultConverter(button -> {
            if (button == startButton) {
                return practice.isSelected() ? "Practice" : "Exam";
            }
            return null;
        });

        return dialog.showAndWait();
    }

    /**
     * Displays an informational dialog confirming that a quiz is starting.
     *
     * @param quiz The quiz that is being started.
     * @param mode The mode in which the quiz is being taken.
     */
    private void showStartQuizConfirmation(Quiz quiz, String mode) {
        Alert startAlert = new Alert(Alert.AlertType.INFORMATION);
        startAlert.setTitle("Quiz Starting");
        startAlert.setHeaderText(null);
        startAlert.setContentText("Starting \"" + quiz.getName() + "\" in " + mode + " mode.");
        startAlert.showAndWait();
    }
}
