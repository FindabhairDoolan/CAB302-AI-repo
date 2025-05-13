package com.example.quizapp.Controllers;

import com.example.quizapp.Main;
import com.example.quizapp.Models.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.*;

import java.util.List;

public class HomeController extends MenuBarController {
    @FXML
    private TextField searchField;
    @FXML

    private IQuizDAO quizDAO = new SqliteQuizDAO();

    @FXML
    private ListView<Quiz> quizResults;


    @FXML
    private void handleSearch() {
        quizResults.getItems().clear();
        String search = searchField.getText();
        List<Quiz> quizzes = quizDAO.searchQuiz(search);
        quizResults.getItems().addAll(quizzes);
        //debug
        System.out.println("Searching for: " + search);
        System.out.println("Results found: " + quizzes.size());
        for (Quiz q : quizzes) {
            System.out.println("Quiz in DB: " + q.getQuizTopic());
        }

    }

    @FXML
    public void initialize() {
        List<Quiz> quizzes = quizDAO.getAllQuizzes();
        quizResults.getItems().setAll(quizzes);
    }

    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/quizapp/home.fxml"));
        Scene scene = new Scene(loader.load());
        primaryStage.setTitle("Home");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}

