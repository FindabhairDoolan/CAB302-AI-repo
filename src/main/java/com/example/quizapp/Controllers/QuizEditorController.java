package com.example.quizapp.Controllers;

import com.example.quizapp.Models.Question;
import com.example.quizapp.Models.Quiz;
import com.example.quizapp.utils.QuizManager;
import com.example.quizapp.Models.SqliteQuestionDAO;
import com.example.quizapp.Models.SqliteQuizDAO;
import com.example.quizapp.Models.OllamaResponse;
import io.github.ollama4j.exceptions.OllamaBaseException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

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

    private Quiz currentQuiz;
    private SqliteQuestionDAO questionDAO = new SqliteQuestionDAO();
    private SqliteQuizDAO quizDAO = new SqliteQuizDAO();

    private static class QuestionCardData {
        Question originalQuestion;

        QuestionCardData(Question originalQuestion) {
            this.originalQuestion = originalQuestion;
        }
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        /*if (QuizManager.getInstance().getCurrentQuiz() == null) {
            Quiz sampleQuiz = new Quiz(
                    "Sample Quiz", "Math", "Algebra", 1,
                    "Easy", "Year 10", "Australia", "Public", 123
            );
            QuizManager.getInstance().setCurrentQuiz(sampleQuiz);
        }*/
        // Load quiz with ID 1 from the database
        SqliteQuizDAO quizDAO = new SqliteQuizDAO();
        // Load a quiz by name
        Quiz loadedQuiz = quizDAO.getQuizByName("World War II");
        // Set it as the current quiz
        this.currentQuiz = loadedQuiz;
        QuizManager.getInstance().setCurrentQuiz(loadedQuiz);
        if (loadedQuiz == null) {
            System.out.println("Quiz not found!");
        } else {
            this.currentQuiz = loadedQuiz;
            QuizManager.getInstance().setCurrentQuiz(loadedQuiz);
            loadQuestions();
        }


        generateQuestionButton.setOnAction(e -> generateQuestion());
        discardButton.setOnAction(e -> discardChanges());
        saveButton.setOnAction(e -> saveChanges());
    }
    public void setQuizToEdit(Quiz quiz) {
        this.currentQuiz = quiz;
        loadQuestions();
    }
    private void loadQuestions() {
        questionsContainer.getChildren().clear();
        if (currentQuiz == null) {
            System.out.println("No quiz loaded");
            return;
        }

        List<Question> questions = questionDAO.getQuestionsForQuiz(currentQuiz.getQuizID());
        for (Question q : questions) {
            Node qNode = createQuestionNode(q);
            questionsContainer.getChildren().add(qNode);
        }
    }
    private Node createQuestionNode(Question question) {
        VBox box = new VBox(5);
        box.setPadding(new Insets(10));

        Label qLabel = new Label("Q " + question.getQuestionText());
        Label options = new Label("Correct " + question.getCorrectAnswer());

        Button deleteBtn = new Button("Delete");
        deleteBtn.setOnAction(e -> {
            questionDAO.deleteQuestionfromQuiz(question);
            loadQuestions();
        });

        Button regenerateBtn = new Button("Regenerate");
        regenerateBtn.setOnAction(e -> regenerateQuestion(question));

        HBox buttonBox = new HBox(10, deleteBtn, regenerateBtn);
        box.getChildren().addAll(qLabel, options, buttonBox);
        box.setStyle("-fx-border-color: black; -fx-border-width: 1; -fx-background-color: #f5f5f5;");

        return box;
    }
    private void regenerateQuestion(Question oldQuestion) {
        try {
            questionDAO.deleteQuestion(oldQuestion);
            String prompt = "Generate 1 quiz question in JSON format with keys: question, correctAnswer, incorrectAnswer1, incorrectAnswer2, incorrectAnswer3.";
            OllamaResponse response = new OllamaResponse(prompt);
            String aiResponse = response.ollamaReturnResponse();
            questionDAO.addAIQuestions(aiResponse, currentQuiz.getQuizID());
            loadQuestions();
        } catch (OllamaBaseException | IOException | InterruptedException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void generateQuestion() {
        try {
            String prompt = "Generate 1 quiz question in JSON format with keys: question, correctAnswer, incorrectAnswer1, incorrectAnswer2, incorrectAnswer3.";
            OllamaResponse response = new OllamaResponse(prompt);
            String aiResponse = response.ollamaReturnResponse();
            questionDAO.addAIQuestions(aiResponse, currentQuiz.getQuizID());
            loadQuestions();
        } catch (OllamaBaseException | IOException | InterruptedException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void saveChanges() {
        for (Node node : questionsContainer.getChildren()) {
            if (node instanceof VBox card && card.getUserData() instanceof QuestionCardData data) {
                Question q = data.originalQuestion;
                if (q.getQuestionID() == 0) {
                    questionDAO.addQuestion(q);
                }
            }
        }
        loadQuestions();
        System.out.println("Changes saved.");
    }

    private void discardChanges() {
        loadQuestions(); // Reset view to original state
        System.out.println("Changes discarded.");
    }
/*
    private VBox createQuestionCard(Question question, boolean isNew) {
        VBox card = new VBox(10);
        card.setStyle("-fx-border-color: #ccc; -fx-border-radius: 5; -fx-padding: 10; -fx-background-color: #f9f9f9;");

        HBox header = new HBox(10);
        Label titleLabel = new Label("Question");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Button regenerateBtn = new Button("\uD83D\uDD04");
        Button deleteBtn = new Button("\uD83D\uDDD1");

        VBox finalCard = card;
        deleteBtn.setOnAction(e -> {
            if (isNew || question.getQuestionID() == 0) {
                questionsContainer.getChildren().remove(finalCard);
            } else {
                questionDAO.deleteQuestion(question);
                questionsContainer.getChildren().remove(finalCard);
            }
        });

        regenerateBtn.setOnAction(e -> {
            Question regenerated = generateAIQuestion(question.getQuizID());
            VBox newCard = createQuestionCard(regenerated, true);
            int index = questionsContainer.getChildren().indexOf(finalCard);
            questionsContainer.getChildren().remove(finalCard);
            questionsContainer.getChildren().add(index, newCard);
        });

        header.getChildren().addAll(titleLabel, spacer, regenerateBtn, deleteBtn);

        Label questionTextLabel = new Label(question.getQuestionText());

        VBox optionsBox = new VBox(5);
        List<String> options = Arrays.asList(
                question.getCorrectAnswer(),
                question.getIncorrectAnswer1(),
                question.getIncorrectAnswer2(),
                question.getIncorrectAnswer3()
        );
        Collections.shuffle(options);

        for (String option : options) {
            Label optionLabel = new Label(option);
            optionsBox.getChildren().add(optionLabel);
        }

        card.getChildren().addAll(header, questionTextLabel, optionsBox);
        card.setUserData(new QuestionCardData(question));

        return card;
    }

    private Question generateAIQuestion(int quizId) {
        // Simulated AI-generated question. Replace with actual AI generation logic.
        return new Question(
                quizId,
                "What is the capital of France?",
                "Paris",
                "London",
                "Berlin",
                "Madrid"
        );
    }*/
}
