package com.example.quizapp.Controllers;

import com.example.quizapp.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import com.example.quizapp.Models.SqliteUserDAO;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Controller class for the create quiz page
 */
public class CreateQuizController {
    @FXML
    public Button backButton;
    @FXML
    public Button createButton;

    @FXML
    private ComboBox<String> subjectComboBox;
    @FXML
    private ComboBox<String> yearLevelComboBox;
    @FXML
    private VBox numQuestionsContainer;

    @FXML
    private ToggleGroup difficultyGroup;
    @FXML
    private ToggleGroup modeToggleGroup;
    @FXML
    private TextArea topicTextArea;

    private ComboBox<Integer> questionDropdown;
    private static final List<Integer> QUESTION_COUNT_OPTIONS = List.of(5, 10, 15, 20, 25);

        @FXML
        public void initialize () {
            ComboBox<Integer> questionDropdown = new ComboBox<>();
            questionDropdown.getItems().addAll(QUESTION_COUNT_OPTIONS);
            questionDropdown.setValue(QUESTION_COUNT_OPTIONS.get(0));
            numQuestionsContainer.getChildren().add(questionDropdown);
        }

    /**
     *Compiles user customisations choices to generate a personalised quiz, sends
     * the user to the quiz page.
     * @throws IOException
     */
    @FXML
    public void onCreate() throws IOException {

        // Check if a subject has been selected
        if (subjectComboBox.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No selected subject");
            alert.setHeaderText(null);
            alert.setContentText("You must select a subject.");
            alert.showAndWait();
            return;
        }

        // Check if a year level has been selected
        if (yearLevelComboBox.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No selected year level");
            alert.setHeaderText(null);
            alert.setContentText("You must select a year level.");
            alert.showAndWait();
            return;
        }


        // Check if a difficulty level has been selected
        if (difficultyGroup.getSelectedToggle() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No selected difficulty");
            alert.setHeaderText(null);
            alert.setContentText("You must select a difficulty level.");
            alert.showAndWait();
            return;
        }

        //If the user hasn't selected a quiz mode they may not create the quiz
        if (modeToggleGroup.getSelectedToggle() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No selected mode");
            alert.setHeaderText(null);
            alert.setContentText("You must select a quiz mode.");
            alert.showAndWait();

            return;
        }

        //Get inputted customisation inputs and send to AI
        ComboBox<Integer> questionDropdown = (ComboBox<Integer>) numQuestionsContainer.getChildren().get(0);
        Integer selectedQuestions = questionDropdown.getValue();
        ToggleButton selectedDifficultyButton = (ToggleButton) difficultyGroup.getSelectedToggle();
        String selectedDifficulty = selectedDifficultyButton.getText();
        String selectedYearLevel = yearLevelComboBox.getValue();
        String selectedSubject    = subjectComboBox.getValue();

        // Send user to Quiz page
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/com/example/quizapp/Quiz.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), Main.WIDTH, Main.HEIGHT);

        // Get the controller and set the total questions
        QuizController quizController = fxmlLoader.getController();
        quizController.setTotalQuestions(selectedQuestions);
        quizController.setDifficulty(selectedDifficulty);
        quizController.setYearLevel(selectedYearLevel);
        quizController.setSubject(selectedSubject);

        Stage stage = (Stage) createButton.getScene().getWindow();
        stage.setScene(scene);

    }

    /**
     *Sends user to the previous page they were on.
     * @throws IOException
     */
    @FXML
    public void onBack() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit Create Quiz");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to exit? Your changes may not be saved.");

        // Define Yes and No buttons
        ButtonType yesButton = new ButtonType("Yes");
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

        // Set the buttons to the alert
        alert.getButtonTypes().setAll(yesButton, noButton);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == yesButton) {
            // User chose Yes – go to dashboard
            Stage stage = (Stage) backButton.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/quizapp/home.fxml"));
            try {
                Scene scene = new Scene(fxmlLoader.load(), Main.WIDTH, Main.HEIGHT);
                stage.setScene(scene);
                //This will lead back to the home page
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // If No is selected, do nothing

    }
}
