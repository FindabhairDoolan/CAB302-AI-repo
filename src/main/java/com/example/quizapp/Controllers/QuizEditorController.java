package com.example.quizapp.Controllers;

import com.example.quizapp.Models.Question;
import com.example.quizapp.Models.Quiz;
import com.example.quizapp.utils.QuizManager;
import com.example.quizapp.Models.SqliteQuestionDAO;
import com.example.quizapp.Models.SqliteQuizDAO;
import com.example.quizapp.utils.AlertManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.net.URL;
import java.sql.SQLException;
import java.util.*;

/**
 * Controller class for the Quiz Editor screen.
 * Allows users to edit existing quiz questions, regenerate them using AI,
 * and save or discard their changes.
 *
 * This controller loads the current quiz from {@link QuizManager},
 * displays the quiz questions using JavaFX UI nodes,
 * and allows regeneration and management of the question list.

 */
public class QuizEditorController implements Initializable {
    @FXML
    private VBox questionsContainer;
    @FXML
    private Button saveButton;
    @FXML
    private Button discardButton;
    @FXML
    private Button generateQuestionButton;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private BorderPane rootPane;

    private final QuizManager qm = QuizManager.getInstance();
    private Quiz currentQuiz;
    private SqliteQuestionDAO questionDAO = new SqliteQuestionDAO();
    private SqliteQuizDAO quizDAO = new SqliteQuizDAO();

    // Stores current working list of questions being edited
    private List<Question> questionList = new ArrayList<>();

    /**
     * Helper class to store the original question reference (for potential enhancements).
     */
    private static class QuestionCardData {
        Question originalQuestion;

        QuestionCardData(Question originalQuestion) {
            this.originalQuestion = originalQuestion;
        }
    }

    /**
     * Called automatically after the FXML layout has been loaded.
     * Initializes the quiz editor with current quiz data.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        currentQuiz = qm.getCurrentQuiz();
        qm.setCurrentQuiz(currentQuiz);
        loadQuestions();

        generateQuestionButton.setOnAction(e -> generateAIQuestionForEdit());
        discardButton.setOnAction(e -> discardChanges());
        saveButton.setOnAction(e -> saveChanges());
    }

    /**
     * Sets the quiz to be edited. Can be used to change the current quiz dynamically.
     *
     * @param quiz The quiz to load into the editor.
     */
    public void setQuizToEdit(Quiz quiz) {
        this.currentQuiz = quiz;
        loadQuestions();
    }

    /**
     * Loads questions from the database for the current quiz and displays them in the UI.
     */
    private void loadQuestions() {
        // Fetch questions from the database
        List<Question> originalQuestions = questionDAO.getQuestionsForQuiz(currentQuiz.getQuizID());
        questionList.clear();
        questionList.addAll(originalQuestions);

        // Clear the container before adding updated questions
        questionsContainer.getChildren().clear();

        for (Question question : questionList) {
            Node questionNode = createQuestionNode(question); // Create each question's UI node
            questionsContainer.getChildren().add(questionNode);
        }
    }

    /**
     * Creates a JavaFX node representing a single question, including its text and options.
     * Adds regenerate and delete buttons.
     *
     * @param question The question to render.
     * @return A VBox node representing the question card.
     */
    private Node createQuestionNode(Question question) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(10));
        card.setStyle("-fx-border-color: #ccc; -fx-border-radius: 5");
        card.getStyleClass().add("thumbnail-background");

        // Display Question (Read-Only)
        Label questionLabel = new Label("Question:");
        Label questionText = new Label(question.getQuestionText());
        questionText.setWrapText(true); // Ensure long questions wrap in the UI

        // Display Options (Answers) (Read-Only)
        Label optionsLabel = new Label("Options:");
        VBox optionsBox = new VBox(5);

        // First three options stacked vertically
        optionsBox.getChildren().addAll(
                new Label("1. " + question.getCorrectAnswer()),
                new Label("2. " + question.getIncorrectAnswer1()),
                new Label("3. " + question.getIncorrectAnswer2())
        );

        // Create last option label
        Label lastOptionLabel = new Label("4. " + question.getIncorrectAnswer3());

        // Regenerate and Delete Buttons
        Button regenerateButton = new Button("🔄 Regenerate");
        regenerateButton.setOnAction(e -> {
            regenerateQuestion(question, card);
        });

        Button deleteButton = new Button("🗑 Delete");
        deleteButton.setOnAction(e -> {
            questionList.remove(question); // Remove from in-memory list
            questionsContainer.getChildren().remove(card); // Remove from UI
        });

        // HBox containing last option and buttons, buttons aligned right
        HBox lastLineBox = new HBox(10);
        lastLineBox.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);

        // Use HBox grow priority to push buttons right
        HBox.setHgrow(lastOptionLabel, Priority.ALWAYS);
        lastOptionLabel.setMaxWidth(Double.MAX_VALUE);

        lastLineBox.getChildren().addAll(lastOptionLabel, regenerateButton, deleteButton);

        // Add the options and last line box
        optionsBox.getChildren().add(lastLineBox);

        // Add all elements to the card
        card.getChildren().addAll(questionLabel, questionText, optionsLabel, optionsBox);

        return card;
    }

    /**
     * Regenerates the provided question using AI and updates it both in-memory and in the UI.
     *
     * @param oldQuestion The question to be regenerated.
     * @param card        The UI card representing the question to regenerate.
     */
    private void regenerateQuestion(Question oldQuestion, VBox card) {
        try {
            // Build AI prompt for regenerating a similar question
            String aiPrompt = String.format(
                    "You are a helpful assistant. Based on the example provided, generate a new valid JSON object for a multiple-choice question with the same structure. Do not return any extra text, quotes, or markdown. Only return the raw JSON object.:\n" +
                            "{\n" +
                            "  \"question\": \"...\",\n" +
                            "  \"correctAnswer\": \"...\",\n" +
                            "  \"incorrectAnswer1\": \"...\",\n" +
                            "  \"incorrectAnswer2\": \"...\",\n" +
                            "  \"incorrectAnswer3\": \"...\"\n" +
                            "}\n\n" +
                            "Regenerate a new question based on this:\n" +
                            "'%s'\n\n" +
                            "Keep the topic: '%s', subject: '%s', and difficulty: '%s'.\n" +
                            "Use realistic and appropriate data for all fields.\n" +
                            "Do not include any explanation, markdown, or additional text — only return the JSON.",
                    oldQuestion.getQuestionText(),
                    currentQuiz.getTopic(),
                    currentQuiz.getSubject(),
                    currentQuiz.getDifficulty()
            );

            // Call QuizManager to interact with AI
            Optional<Question> regeneratedQuestion = QuizManager.generateSingleQuestionWithAI(aiPrompt, currentQuiz.getQuizID());

            // Check if regeneration was successful
            if (regeneratedQuestion.isPresent()) {
                Question newQuestion = regeneratedQuestion.get();

                // Replace the old question in the in-memory list
                int index = questionList.indexOf(oldQuestion);
                if (index == -1) {
                    System.err.println("Old question not found in in-memory list.");
                    AlertManager.alertErrorShow("Error", "Failed to update the question in memory.");
                    return;
                }
                questionList.set(index, newQuestion);

                // Update the UI
                Platform.runLater(() -> {
                    int cardIndex = questionsContainer.getChildren().indexOf(card);
                    if (cardIndex == -1) {
                        System.err.println("Card not found in questionsContainer.");
                        AlertManager.alertErrorShow("Error", "Failed to update the question in the UI.");
                        return;
                    }

                    try {
                        Node newQuestionNode = createQuestionNode(newQuestion); // Create card for new question
                        questionsContainer.getChildren().set(cardIndex, newQuestionNode);
                        AlertManager.alertErrorShow("Success", "Question regenerated and UI updated!");
                    } catch (Exception e) {
                        e.printStackTrace();
                        AlertManager.alertErrorShow("Error", "Failed to update the UI for the regenerated question.");
                    }
                });
            } else {
                System.err.println("AI failed to generate a question.");
                AlertManager.alertErrorShow("Error", "Failed to regenerate the question. Please try again.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            AlertManager.alertErrorShow("Error", "Something went wrong while regenerating the question.");
        }
    }

    /**
     * Generates a new AI-based question using the current quiz context
     * and appends it to the editor UI and working list.
     */
    private void generateAIQuestionForEdit() {
        try {
            // Build AI prompt (same logic as onCreate() but for ONE question)
            String quizTopic = currentQuiz.getTopic();
            String quizSubject = currentQuiz.getSubject();
            String quizDifficulty = currentQuiz.getDifficulty();

            String aiPrompt = String.format(
                    "You are a helpful assistant. Based on the example provided, generate a valid JSON object for a multiple-choice question. Format the response strictly as raw JSON without any missing keys or braces. Here's the template:\n" +
                            "  {\n" +
                            "    \"question\": \"...\",\n" +
                            "    \"correctAnswer\": \"...\",\n" +
                            "    \"incorrectAnswer1\": \"...\",\n" +
                            "    \"incorrectAnswer2\": \"...\",\n" +
                            "    \"incorrectAnswer3\": \"...\"\n" +
                            "  }\n" +
                            "  Always ensure the response is a complete JSON object and avoid truncating the output.\n"+
                            "Keep the topic: '%s', subject: '%s', and difficulty: '%s'.\n",
                    quizTopic, quizSubject, quizDifficulty
            );

            // Call QuizManager (or equivalent logic from onCreate()) to interact with AI
            Optional<Question> aiGeneratedQuestion = QuizManager.generateSingleQuestionWithAI(aiPrompt, currentQuiz.getQuizID());

            // Handle if the question is successfully generated
            if (aiGeneratedQuestion.isPresent()) {
                Question newQuestion = aiGeneratedQuestion.get();

                // Save it temporarily
                questionList.add(newQuestion);

                // Dynamically add it to the UI
                Node questionNode = createQuestionNode(newQuestion); // Create UI card for the question
                questionsContainer.getChildren().add(questionNode);
                AlertManager.alertErrorShow("Success", "New question generated!");
            } else {
                AlertManager.alertErrorShow("Error", "Failed to generate a new question. Please try again.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            AlertManager.alertErrorShow("Error", "Something went wrong while generating a question.");
        }
    }

    /**
     * Saves all questions in the temporary list to the database.
     */
    private void saveChanges() {
        try {
            // Bulk update questions in the database using the DAO
            questionDAO.setQuestions(currentQuiz.getQuizID(), questionList);
            AlertManager.alertErrorShow("Success", "Changes saved successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            AlertManager.alertErrorShow("Error", "Failed to save changes.");
        }

    }

    /**
     * Discards all unsaved changes and reloads the original questions from the database.
     */
    private void discardChanges() {
        loadQuestions(); // Reload from the database and reset the in-memory list
        AlertManager.alertErrorShow("Discarded", "All changes have been discarded.");
    }
}
