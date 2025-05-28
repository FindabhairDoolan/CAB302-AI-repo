package com.example.quizapp.utils;

import com.example.quizapp.Controllers.QuizController;
import com.example.quizapp.Models.OllamaResponse;
import com.example.quizapp.Models.Question;
import com.example.quizapp.Models.Quiz;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.Optional;
import org.json.JSONObject;

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
            controller.setQuiz(quiz, mode);


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
    public static boolean isValidJson(String response) {
        try {
            new JSONObject(response); // Try parsing the string as a JSON object
            return true;              // JSON is valid
        } catch (Exception e) {
            return false;             // JSON is invalid
        }
    }

    /**
     * Calls the AI to generate a single question.
     *
     * @param prompt The AI prompt to generate a single question.
     * @param quizId The ID of the quiz for context.
     * @return Optional<Question> if the AI successfully generates the question.
     */
    public static Optional<Question> generateSingleQuestionWithAI(String prompt, int quizId) {
        try {
            // Call the AI API
            OllamaResponse response = new OllamaResponse(prompt);
            String aiResponse = response.ollamaReturnResponse();

            if (aiResponse == null || aiResponse.isBlank()) {
                throw new IllegalArgumentException("AI returned an empty response.");
            }

            // Trim and auto-correct common JSON issues
            aiResponse = aiResponse.trim();

            // Attempt to auto-fix: add missing closing brace
            if (!aiResponse.endsWith("}")) {
                aiResponse += "}";
            }

            // Basic structure validation
            if (!isValidJson(aiResponse)) {
                throw new IllegalArgumentException("AI returned an invalid JSON response: " + aiResponse);
            }

            // Parse the JSON
            JSONObject json = new JSONObject(aiResponse);

            String questionText = json.getString("question");
            String correctAnswer = json.getString("correctAnswer");
            String incorrect1 = json.getString("incorrectAnswer1");
            String incorrect2 = json.getString("incorrectAnswer2");
            String incorrect3 = json.getString("incorrectAnswer3");

            Question question = new Question(
                    quizId,
                    questionText,
                    correctAnswer,
                    incorrect1,
                    incorrect2,
                    incorrect3
            );

            return Optional.of(question);

        } catch (Exception e) {
            System.err.println("Error generating question with AI:");
            e.printStackTrace();
        }

        return Optional.empty();
    }

}
