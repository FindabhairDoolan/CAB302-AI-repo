package com.example.quizapp.utils;

import com.example.quizapp.Controllers.QuizController;
import com.example.quizapp.Models.Quiz;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.Optional;

public class QuizManager {
    private static QuizManager instance;
    private Quiz currentQuiz;

    private QuizManager() {}

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

    private void startQuiz(Quiz quiz, String mode) {
        try {
            //Open quiz/quiz controller from pop up
            QuizController controller = SceneManager.switchSceneWithController("/com/example/quizapp/quiz.fxml", "Quiz");

            // Now inject values
            QuizManager.getInstance().setCurrentQuiz(quiz);
            controller.setQuiz(quiz);


        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

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

    private void showStartQuizConfirmation(Quiz quiz, String mode) {
        Alert startAlert = new Alert(Alert.AlertType.INFORMATION);
        startAlert.setTitle("Quiz Starting");
        startAlert.setHeaderText(null);
        startAlert.setContentText("Starting \"" + quiz.getName() + "\" in " + mode + " mode.");
        startAlert.showAndWait();
    }
}
