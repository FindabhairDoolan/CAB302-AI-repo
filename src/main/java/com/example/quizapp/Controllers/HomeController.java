package com.example.quizapp.Controllers;

import com.example.quizapp.Main;
import com.example.quizapp.Models.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;

import java.util.List;

public class HomeController extends MenuBarController  {
    @FXML
    public AnchorPane filterOverlay;
    @FXML
    private TextField searchField;
    @FXML
    private ListView<Quiz> quizResults;
    @FXML
    private FlowPane quizResultsWindow;
    @FXML
    public ComboBox<String> difficultySetting;
    @FXML
    public ComboBox<String> yearSetting;
    @FXML
    public ComboBox<String> countrySetting; //unused; for later implementation
    @FXML
    public ComboBox<String> subjectSetting;

    private IQuizDAO quizDAO = new SqliteQuizDAO();



    //Quiz Display: Search results
    @FXML
    private void handleSearch() {
        //Clear quizzes displayed
        quizResults.getItems().clear();
        //Get input
        String search = searchField.getText();
        //Search database for quizzes matching input
        List<Quiz> quizzes = quizDAO.searchQuiz(search);
        //Display results
        quizResults.getItems().addAll(quizzes); //List View
        displayQuizzes(quizzes); //Thumbnail View

        //debug
        System.out.println("Searching for: " + search);
        System.out.println("Results found: " + quizzes.size());
        for (Quiz q : quizzes) {
            System.out.println("Quiz in DB: " + q.getQuizTopic());
        }

    }



    //Quiz Display: Filtering Popup
    public void toggleFilter() {
        filterOverlay.setVisible(!filterOverlay.isVisible());
    }

    public void clearFilter(){
        yearSetting.setValue(null);
        subjectSetting.setValue(null);
        difficultySetting.setValue(null);


    }

    public void applyFilter() {
        String difficulty = difficultySetting.getValue();
        String year = yearSetting.getValue();
        String subject = subjectSetting.getValue();
        String country = countrySetting.getValue();


        List<Quiz> filtered = quizDAO.getAllQuizzes().stream()
                //Ignores filter if selection is null or empty (or other cases) otherwise checks for match
                .filter(q -> difficulty == null || difficulty.isEmpty() ||  "Any".equals(difficulty) || "Select option...".equals(difficulty)  || q.getDifficulty().equalsIgnoreCase(difficulty))
                .filter(q -> year == null || year.isEmpty() ||  "Select option...".equals(year) ||q.getYearLevel().equalsIgnoreCase(year))
                .filter(q -> subject == null || subject.isEmpty() ||  "Select option...".equals(subject) || q.getQuizTopic().equalsIgnoreCase(subject))
                .filter(q -> country == null || country.isEmpty() ||  "Select option...".equals(country) || q.getCountry().equalsIgnoreCase(country))

                .toList();
        //return filter
        quizResults.getItems().setAll(filtered); //List View
        displayQuizzes(filtered); //Thumbnail
        filterOverlay.setVisible(false); // Hide overlay after applying
    }

    private void displayQuizzes(List<Quiz> quizzes) {
        quizResultsWindow.getChildren().clear();
        for (Quiz quiz : quizzes) {
            AnchorPane card = createQuizCard(quiz);
            quizResultsWindow.getChildren().add(card);
        }
    }

    private AnchorPane createQuizCard(Quiz quiz) {
        AnchorPane card = new AnchorPane();
        card.setPrefSize(150, 100);
        card.setStyle("-fx-background-color: #f4f4f4; -fx-border-color: #ccc; -fx-border-radius: 5; -fx-padding: 10;");

        Label name = new Label(quiz.getQuizName());
        name.setLayoutX(10);
        name.setLayoutY(10);

        Label topic = new Label("Subject: " + quiz.getQuizTopic());
        topic.setLayoutX(10);
        topic.setLayoutY(25);

        Label year = new Label(quiz.getYearLevel());
        year.setLayoutX(10);
        year.setLayoutY(55);

        Label difficulty = new Label("Difficulty: " + quiz.getDifficulty());
        difficulty.setLayoutX(10);
        difficulty.setLayoutY(40);



        card.getChildren().addAll(name, difficulty, year, topic);

        //When card is clicked go to quiz
        card.setOnMouseClicked(event -> {
            System.out.println("Clicked quiz: " + quiz.getQuizName());
            // navigate to quiz view
        });

        return card;
    }



    //Initialise in window
    @FXML
    public void initialize() {
        //Loads all available quizzes, at first
        List<Quiz> quizzes = quizDAO.getAllQuizzes();
        quizResults.getItems().setAll(quizzes);

        //Creating quiz window
        displayQuizzes(quizzes);

        // Setup filter values
        difficultySetting.getItems().addAll("Easy", "Medium", "Hard", "Any");
        yearSetting.getItems().addAll("Year 1", "Year 2", "Year 3", "Year 4", "Year 5", "Year 6",
                "Year 7","Year 8","Year 9","Year 10","Year 11","Year 12");
        subjectSetting.getItems().addAll("English", "Mathematics","Social Science","Science","IT");
        countrySetting.getItems().addAll("Australia");

        //Set default entries for filter comboboxes
        countrySetting.setValue("Australia");  // Sets "Australia" as default selected
        subjectSetting.setValue("Select option...");
        difficultySetting.setValue("Select option...");
        yearSetting.setValue("Select option...");

    }

}
