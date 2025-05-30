package com.example.quizapp.Controllers;
import com.example.quizapp.Models.*;
import com.example.quizapp.utils.*;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.List;
import java.util.Optional;
import javafx.geometry.Insets;

/**
 * Controller for the Home view of the Quiz App.
 * Handles displaying quizzes, filtering, searching, and initializing user-specific data.
 * Inherits common menu bar logic from {@link MenuBarController}.
 */
public class HomeController extends MenuBarController  {
    /** Displays the welcome message with the user's name. */
    @FXML
    public Label welcomeLabel;

    /** Overlay pane shown when filter settings are toggled. */
    @FXML
    public AnchorPane filterOverlay;

    /** Field for entering a search query to look up quizzes. */
    @FXML
    private TextField searchField;

    /** ListView to display raw search results (currently unused in GUI). */
    @FXML
    private ListView<Quiz> quizResults;

    /** Pane where quiz thumbnails are dynamically added. */
    @FXML
    private FlowPane quizResultsWindow;

    /** Dropdown to select quiz difficulty for filtering. */
    @FXML
    public ComboBox<String> difficultySetting;

    /** Dropdown to select year level for filtering quizzes. */
    @FXML
    public ComboBox<String> yearSetting;

    /** Dropdown to select country (reserved for future use). */
    @FXML
    public ComboBox<String> countrySetting;

    /** Dropdown to select subject for filtering. */
    @FXML
    public ComboBox<String> subjectSetting;

    /** DAO object for interacting with the quiz database. */
    private IQuizDAO quizDAO = new SqliteQuizDAO();

    /**
     * Triggered when the user submits a search query.
     * Searches for matching quizzes and updates the display.
     */
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



    /**
     * Shows or hides the filter overlay.
     */
    public void toggleFilter() {
        filterOverlay.setVisible(!filterOverlay.isVisible());
    }

    /**
     * Clears all selected filter and search values in the UI.
     */
    public void clearFilter(){
        yearSetting.setValue(null);
        subjectSetting.setValue(null);
        difficultySetting.setValue(null);


    }

    /**
     * Applies the selected filters and updates the quiz results accordingly.
     */
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


/**
 * Updates the UI to display the provided list of quizzes as thumbnail cards.
 */
    private void displayQuizzes(List<Quiz> quizzes) {
        quizResultsWindow.getChildren().clear();
        User user = AuthManager.getInstance().getCurrentUser();
        for (Quiz quiz : quizzes) {
            if ((quiz.getVisibility().equals("Public")) || (quiz.getCreatorID() == user.getUserID())){
                AnchorPane card = createQuizCard(quiz);
                quizResultsWindow.getChildren().add(card);

            }

        }
    }



    /**
     * Creates a styled thumbnail card for a quiz.
     *
     * @param quiz The quiz to represent
     * @return An AnchorPane representing the quiz card
     */
    private AnchorPane createQuizCard(Quiz quiz) {
        AnchorPane card = new AnchorPane();
        int cardWidth = 200;
        int cardHeight = 90;

        card.setPrefSize(cardWidth, cardHeight);
        card.getStyleClass().add("thumbnail-background");

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
            // navigate to quiz view -> moved all logic to QuizManager
            QuizManager qm = QuizManager.getInstance();
            qm.openQuiz(quiz);

        });

        return card;
    }



    /**
     * Initializes the Home view.
     * Sets up the welcome message, loads quizzes, and populates filter options.
     */
    @FXML
    public void initialize() {
        User user = AuthManager.getInstance().getCurrentUser();
        String username = user.getUserName(); // You can fetch this from session/auth logic
        welcomeLabel.setText("Hi, " + username + "!");

        //Loads all available quizzes
        List<Quiz> quizzes = quizDAO.getAllQuizzes();

        //Set FlowPlane (spacing between quiz thumbnails)
        quizResultsWindow.setHgap(10);  // Horizontal gap between cards
        quizResultsWindow.setVgap(10);  // Vertical gap between cards
        //Filling FlowPane with quizzes
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
