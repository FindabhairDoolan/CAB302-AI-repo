package com.example.quizapp.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.Node;

import java.net.URL;
import java.util.ResourceBundle;

public class QuizEditorController implements Initializable {
    @FXML private VBox questionsContainer;
    @FXML private Button saveButton;
    @FXML private Button discardButton;
    @FXML private Button generateQuestionButton;
    @FXML private ScrollPane scrollPane;
    @FXML private BorderPane rootPane;

    private int questionCount = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        generateQuestionButton.setOnAction(e -> addQuestion());
        discardButton.setOnAction(e -> discardChanges());
        saveButton.setOnAction(e -> saveChanges());
        //backButton.setOnAction(e -> goBack());

        // Load existing quiz questions from DB
        loadQuestionsFromDatabase();
    }

    private void addQuestion() {
        questionCount++;
        VBox card = createQuestionCard("Question " + questionCount);
        questionsContainer.getChildren().add(questionsContainer.getChildren().size() - 1, card);
    }

    private VBox createQuestionCard(String title) {
        VBox card = new VBox(5);

        // Header with delete and clone buttons
        HBox header = new HBox(10);
        Label titleLabel = new Label(title);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Button cloneBtn = new Button("📝");
        Button deleteBtn = new Button("🗑");

        deleteBtn.setOnAction(e -> questionsContainer.getChildren().remove(card));
        cloneBtn.setOnAction(e -> {
            VBox cloned = createQuestionCard(title + " (Copy)");
            questionsContainer.getChildren().add(questionsContainer.getChildren().indexOf(card) + 1, cloned);
        });

        header.getChildren().addAll(titleLabel, spacer, cloneBtn, deleteBtn);

        Label questionText = new Label("[question text here]");
        VBox options = new VBox(5);
        for (char c = 'A'; c <= 'D'; c++) {
            RadioButton option = new RadioButton("[option " + c + "]");
            options.getChildren().add(option);
        }

        card.getChildren().addAll(header, questionText, options);
        return card;
    }

    private void loadQuestionsFromDatabase() {
        // TODO: Pull from SQLite DB and populate `questionsContainer` with real data
    }

    private void saveChanges() {
        // TODO: Gather questions and save to SQLite
        System.out.println("Saving changes to DB...");
    }

    private void discardChanges() {
        // TODO: Possibly reload from DB or navigate away without saving
        System.out.println("Changes discarded.");
    }


}
