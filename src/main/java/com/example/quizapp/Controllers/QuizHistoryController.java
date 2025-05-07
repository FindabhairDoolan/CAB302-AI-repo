package com.example.quizapp.Controllers;

import com.example.quizapp.Models.Quiz;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class QuizHistoryController {

    @FXML
    public ListView<String> quizListView;

    @FXML
    private Button backButton;


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

    //create a list of mock quizzes for testing etc
    public ObservableList<Quiz> createMockQuizzes() {
        ObservableList<Quiz> quizzes = FXCollections.observableArrayList();

        // Add some mock quizzes to the list
        quizzes.add(new Quiz("Math Quiz", "Mathematics", "Online", "Medium", "High School", "USA", 101));
        quizzes.add(new Quiz("Science Quiz", "Science", "In-Person", "Hard", "College", "UK", 102));
        quizzes.add(new Quiz("History Quiz", "History", "Online", "Easy", "High School", "Australia", 103));

        return quizzes;
    }

    public ObservableList<Quiz> getQuizzes() {
        ObservableList<Quiz> quizzes = FXCollections.observableArrayList();


        return quizzes;
    }

    //display the quizzes
    public void displayQuizzes() {
        ObservableList<Quiz> quizzes = createMockQuizzes();

        ObservableList<String> quizDisplayList = FXCollections.observableArrayList();

        for (Quiz quiz : quizzes) {
            quizDisplayList.add(quiz.getQuizName() + " - " + quiz.getQuizTopic());
        }

        quizListView.setItems(quizDisplayList);
    }

    //populate the listview when the scene is loaded
    @FXML
    public void initialize() {
        displayQuizzes();
    }
}
