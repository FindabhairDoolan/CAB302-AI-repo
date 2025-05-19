package com.example.quizapp.Controllers;
import com.example.quizapp.Models.*;
import com.example.quizapp.utils.SceneManager;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import javafx.geometry.Insets;
import javafx.stage.Stage;


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
        //Get input
        String search = searchField.getText();
        //Search database for quizzes matching input
        List<Quiz> quizzes = quizDAO.searchQuiz(search);
        //Display results
        displayQuizzes(quizzes); //Thumbnail View

        //debug
        System.out.println("Searching for: " + search);
        System.out.println("Results found: " + quizzes.size());
        for (Quiz q : quizzes) {
            System.out.println("Quiz in DB: " + q.getTopic());
        }

    }



    //Quiz Display: Filter Popup
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
                .filter(q -> subject == null || subject.isEmpty() ||  "Select option...".equals(subject) || q.getSubject().equalsIgnoreCase(subject))
                .filter(q -> country == null || country.isEmpty() ||  "Select option...".equals(country) || q.getCountry().equalsIgnoreCase(country))

                .toList();
        //return filter
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
            Stage stage = (Stage) quizResultsWindow.getScene().getWindow();
            QuizController controller = SceneManager.switchSceneWithController("/com/example/quizapp/quiz.fxml", "Quiz", stage);

            // Now inject values
            QuizManager.getInstance().setCurrentQuiz(quiz);
            controller.setDifficulty(quiz.getDifficulty());
            controller.setYearLevel(quiz.getYearLevel());
            controller.setSubject(quiz.getSubject());
            controller.setMode(mode);
            controller.setQuiz(quiz);


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

    //Create a thumbnail on home page for each quiz in database
    private AnchorPane createQuizCard(Quiz quiz) {
        AnchorPane card = new AnchorPane();
        int cardWidth = 200;
        int cardHeight = 90;


        card.setPrefSize(cardWidth, cardHeight);
        card.setStyle("-fx-border-color: #ccc; -fx-border-radius: 5; -fx-padding: 10;");

        VBox content = new VBox(5); // spacing between elements
        content.setPadding(new Insets(10));
        content.setMaxWidth(150);  // Ensure VBox doesn't stretch beyond the card size

        // Labels for name, year, subject, topic, and difficulty
        Label name = new Label(quiz.getName());
        name.setWrapText(true);
        name.setMaxWidth(130); // Limit label width
        name.setMinHeight(20); // Minimum height to avoid too small labels

        Label year = new Label("Year: " + quiz.getYearLevel());
        Label subject = new Label("Subject: " + quiz.getSubject());
        Label topic = new Label("Topic: " + quiz.getTopic());
        Label difficulty = new Label("Difficulty: " + quiz.getDifficulty());

        // Set wrap text and max width for each label
        for (Label lbl : List.of(name, year, subject, topic, difficulty)) {
            lbl.setWrapText(true);
            lbl.setMaxWidth(130);
            lbl.setMinHeight(20);  // Set a minimum height for each label
        }

        // Adding labels to the VBox
        content.getChildren().addAll(name, year, subject, topic, difficulty);
        card.getChildren().add(content);


        //When card is clicked go to quiz
        card.setOnMouseClicked(event -> {
            System.out.println("Clicked quiz: " + quiz.getName());
            // navigate to quiz view
            openQuiz(quiz);

        });

        return card;
    }







    //Initialise in window
    @FXML
    public void initialize() {
        //Loads all available quizzes, at first
        List<Quiz> quizzes = quizDAO.getAllQuizzes();

        // quizResultsWindow is FlowPane
        quizResultsWindow.setHgap(10);  // Horizontal gap between cards
        quizResultsWindow.setVgap(10);  // Vertical gap between cards

        //Creating quiz window
        displayQuizzes(quizzes);

        // Setup filter values
        difficultySetting.getItems().addAll("Easy", "Medium", "Hard", "Any");
        yearSetting.getItems().addAll("Year 1", "Year 2", "Year 3", "Year 4", "Year 5", "Year 6",
                "Year 7","Year 8","Year 9","Year 10","Year 11","Year 12");
        subjectSetting.getItems().addAll("English", "Mathematics","Social Science","Science","Information Technology");
        countrySetting.getItems().addAll("Australia");

        //Set default entries for filter comboboxes
        countrySetting.setValue("Australia");  // Sets "Australia" as default selected
        subjectSetting.setValue("Select option...");
        difficultySetting.setValue("Select option...");
        yearSetting.setValue("Select option...");


    }

}
