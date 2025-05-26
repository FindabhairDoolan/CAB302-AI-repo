package com.example.quizapp.Controllers;

import com.example.quizapp.Models.*;
import com.example.quizapp.utils.AuthManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class QuizHistoryController {

    @FXML
    private TableView<QuizWithScore> quizTable;

    @FXML
    private TableColumn<QuizWithScore, String> nameCol;

    @FXML
    private TableColumn<QuizWithScore, String> subjectCol;

    @FXML
    private TableColumn<QuizWithScore, String> topicCol;

    @FXML
    private TableColumn<QuizWithScore, String> scoreCol;

    @FXML
    private TableColumn<QuizWithScore, String> timeCol;

    @FXML
    private TableColumn<QuizWithScore, Void> actionCol;

    @FXML
    public void initialize() {
        setupColumns();
        loadQuizData();
    }

    private void setupColumns() {
        nameCol.setCellValueFactory(data -> javafx.beans.binding.Bindings.createStringBinding(() ->
                data.getValue().getQuiz().getName()));

        subjectCol.setCellValueFactory(data -> javafx.beans.binding.Bindings.createStringBinding(() ->
                data.getValue().getQuiz().getSubject()));

        topicCol.setCellValueFactory(data -> javafx.beans.binding.Bindings.createStringBinding(() ->
                data.getValue().getQuiz().getTopic()));

        scoreCol.setCellValueFactory(data -> javafx.beans.binding.Bindings.createStringBinding(() ->
                String.valueOf(data.getValue().getScore())));

        timeCol.setCellValueFactory(data -> javafx.beans.binding.Bindings.createStringBinding(() ->
                String.valueOf(data.getValue().getAttempt().getAttemptTime())));

        actionCol.setCellFactory(new Callback<>() {
            @Override
            public TableCell<QuizWithScore, Void> call(final TableColumn<QuizWithScore, Void> param) {
                return new TableCell<>() {
                    private final Button retakeBtn = new Button("Retake");
                    private final Button viewBtn = new Button("View");
                    private final Button downLoadBtn = new Button("Download");
                    private final HBox box = new HBox(8, retakeBtn, viewBtn, downLoadBtn);

                    {
                        retakeBtn.setOnAction(event -> {
                            QuizWithScore item = getTableView().getItems().get(getIndex());
                            handleRetakeQuiz(item.getQuiz());
                        });

                        viewBtn.setOnAction(event -> {
                            QuizWithScore item = getTableView().getItems().get(getIndex());
                            handleViewQuiz(item.getAttempt(), item.getQuiz());//Add parameter that gets time in seconds
                        });

                        downLoadBtn.setOnAction(event -> {
                            QuizWithScore item = getTableView().getItems().get(getIndex());
                            handleDownLoadQuiz(item.getQuiz());
                        });

                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(box);
                        }
                    }
                };
            }
        });
    }

    private void loadQuizData() {
        ObservableList<QuizWithScore> quizData = getQuizzes();
        quizTable.setItems(quizData);
    }

    private ObservableList<QuizWithScore> getQuizzes() {
        User user = AuthManager.getInstance().getCurrentUser();
        return FXCollections.observableArrayList(
                new SqliteQuizAttemptDAO().getQuizzesAttemptedByUser(user.getUserID())
        );
    }

    //Add parameter for the time it took to complete quiz in the attempt (int timerSeconds)
    private void handleViewQuiz(QuizAttempt attempts, Quiz quiz) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/quizapp/quiz.fxml"));
            Parent root = loader.load();

            QuizController controller = loader.getController();
            controller.setViewMode(true, attempts);

            //Replace this with the below if statement
            controller.setQuiz(quiz, "Practice");

            //When attempt time is displayed on quiz history page, implement this if statement
            //So that the quiz completion time can be displayed
//            if(time.equals("--:--:--")){
//                controller.setQuiz(quiz, "Practice");
//            }
//            else{
//                controller.setTimer(timerSeconds);
//                controller.setQuiz(quiz, "Exam");
//            }

            Stage stage = (Stage) quizTable.getScene().getWindow();
            stage.setTitle("View Quiz Attempt");
            stage.setScene(new Scene(root, 800, 550));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleDownLoadQuiz(Quiz quiz) {

        //Intialize file chooser and the name and format the file is saved
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialFileName(quiz.getName().replaceAll("\\s+", "_") + ".txt");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));

        //Show the save window
        File file = fileChooser.showSaveDialog(quizTable.getScene().getWindow());

        //Write the quiz into a .txt file
        if (file != null) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
                writer.println("Name: " + quiz.getName());
                writer.println("Subject: " + quiz.getSubject());
                writer.println("Topic: " + quiz.getTopic());
                writer.println("Difficulty: " + quiz.getDifficulty());
                writer.println("Year level: " + quiz.getYearLevel());
                writer.println("Country: " + quiz.getCountry());
                writer.println();

                List<Question> questions = new SqliteQuestionDAO().getQuestionsForQuiz(quiz.getQuizID());

                int index = 1;
                for (Question q : questions) {
                    writer.println(index++ + ". " + q.getQuestionText());
                    for (String answer : q.getShuffledAnswers()) {
                        writer.println("   ○ " + answer);
                    }
                    writer.println();
                }

                //Show alert message with success info
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Quiz downloaded successfully!", ButtonType.OK);
                alert.setHeaderText(null);
                alert.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to save quiz.", ButtonType.OK);
                alert.setHeaderText(null);
                alert.showAndWait();
            }
        }
    }

    private void handleRetakeQuiz(Quiz quiz) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/quizapp/quiz.fxml"));
            Parent root = loader.load();

            QuizController quizController = loader.getController();

            //When attempt time is displayed on this page, implement this if statement
            //So that the quiz can be retaken in same mode as the attempt
//            if(time.equals("--:--:--")){
//                controller.setQuiz(quiz, "Practice");
//            }
//            else{
//                controller.setQuiz(quiz, "Exam");
//            }
            quizController.setQuiz(quiz, "Practice");

            Stage stage = (Stage) quizTable.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 550));
            stage.setTitle("Take Quiz");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
