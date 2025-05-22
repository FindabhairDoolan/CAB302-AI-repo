package com.example.quizapp.Controllers;

import com.example.quizapp.Models.*;
import com.example.quizapp.utils.AuthManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import java.io.IOException;
import java.util.List;

public class QuizHistoryController {

    @FXML
    public ListView<QuizWithScore> quizListView;

    @FXML
    private Button backButton;

    //populate the listview when the scene is loaded
    @FXML
    public void initialize() {
        displayQuizzes();
    }

    //return to home
    @FXML
    public void onBack() {
        Stage stage = (Stage) backButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/quizapp/home.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load(), 800, 550);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //create a list of mock quizzes for testing
    public ObservableList<Quiz> createMockQuizzes() {
        ObservableList<Quiz> quizzes = FXCollections.observableArrayList();

        // Add some mock quizzes to the list
        quizzes.add(new Quiz("Math Quiz", "Mathematics", "Fractions", "Online", "Medium", "High School", "USA", "Public",101));
        quizzes.add(new Quiz("Science Quiz", "Science","Chemistry", "In-Person", "Hard", "College", "UK", "Public",102));
        quizzes.add(new Quiz("History Quiz", "History", "The Industrial Revolution","Online", "Easy", "High School", "Australia", "Public", 103));

        return quizzes;
    }

    //A function to retrieve quizzes from the database
    public ObservableList<QuizWithScore> getQuizzes() {
        User user = AuthManager.getInstance().getCurrentUser();
        ObservableList<QuizWithScore> quizzes = FXCollections.observableArrayList(new SqliteQuizAttemptDAO().getQuizzesAttemptedByUser(user.getUserID()));
        return quizzes;
    }

    //Display the quizzes
    public void displayQuizzes() {
        ObservableList<QuizWithScore> quizzes = getQuizzes();
        quizListView.setItems(quizzes);

        quizListView.setCellFactory(list -> new ListCell<>() {
            @Override
            protected void updateItem(QuizWithScore item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);


                    setText(null);
                } else {
                    VBox box = new VBox(5);

                    String quizName = item.getQuiz().getName();
                    String topic = item.getQuiz().getTopic();
                    List<Integer> scores = item.getScores();

                    Label title = new Label(quizName + " - " + topic);
                    Label score = new Label("Scores: " + scores.toString());

                    Button retakeButton = new Button("Retake Quiz");
                    retakeButton.setOnAction(e -> handleRetakeQuiz(item.getQuiz()));

                    box.getChildren().addAll(title, score, retakeButton);
                    setGraphic(box);
                }
            }
        });
    }


    private void handleRetakeQuiz(Quiz quiz) {
    try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/quizapp/quiz.fxml"));
        Parent root = loader.load();

        QuizController quizController = loader.getController();
        quizController.setQuiz(quiz);

        int numOfQs = new SqliteQuizDAO().getNumberOfQuestions(quiz);
        quizController.setTotalQuestions(numOfQs);

        Stage stage = (Stage) backButton.getScene().getWindow();
        Scene scene = new Scene(root, 800, 550);
        stage.setScene(scene);
        stage.setTitle("Take quiz");
        stage.show();

    } catch (IOException e) {
        e.printStackTrace();
    }
    }

}
