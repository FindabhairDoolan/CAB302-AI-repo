package com.example.quizapp.Controllers;

import com.example.quizapp.Main;
import com.example.quizapp.Models.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.util.List;

public class HomeController extends MenuBarController {
    @FXML
    public AnchorPane filterOverlay;
    @FXML
    private TextField searchField;

    @FXML
    public ListView<Quiz> quizResults;
    @FXML
    public ComboBox<String> difficultySetting;
    @FXML
    public ComboBox<String> yearSetting;
    @FXML
    public ComboBox<String> countrySetting; //unused; for later implementation
    @FXML
    public ComboBox<String> subjectSetting;

    private IQuizDAO quizDAO = new SqliteQuizDAO();



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
        //Loads all available quizzes, at first
        List<Quiz> quizzes = quizDAO.getAllQuizzes();
        quizResults.getItems().setAll(quizzes);

        // Setup filter values
        difficultySetting.getItems().addAll("Easy", "Medium", "Hard", "Any");
        yearSetting.getItems().addAll("Year 1", "Year 2", "Year 3", "Year 4", "Year 5", "Year 6",
                "Year 7","Year 8","Year 9","Year 10","Year 11","Year 12");
        subjectSetting.getItems().addAll("English", "Mathematics","Social Science","Science","IT");
        countrySetting.getItems().addAll("Australia");

        //Set default entries for combobox
        countrySetting.setValue("Australia");  // Sets "Australia" as default selected
        subjectSetting.setValue("Select option...");
        difficultySetting.setValue("Select option...");
        yearSetting.setValue("Select option...");

    }

//    public void start(Stage primaryStage) throws Exception {
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/quizapp/home.fxml"));
//        Scene scene = new Scene(loader.load());
//        primaryStage.setTitle("Home");
//        primaryStage.setScene(scene);
//        primaryStage.show();
//    }


    public void toggleFilter() {
        filterOverlay.setVisible(!filterOverlay.isVisible());
    }

<<<<<<< HEAD
=======
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
        //return filter strings
        quizResults.getItems().setAll(filtered);
        filterOverlay.setVisible(false); // Hide overlay after applying

    }
>>>>>>> HomePage
}

