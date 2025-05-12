package com.example.quizapp.Controllers;

import com.example.quizapp.Main;
import com.example.quizapp.Models.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.commons.lang3.ObjectUtils;

import java.io.IOException;
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
    private ComboBox<String> yearLevelComboBox;
    @FXML
    private VBox numQuestionsContainer;
    @FXML
    private ToggleGroup difficultyGroup;
    @FXML
    private ToggleGroup modeToggleGroup;
    @FXML
    private TextArea topicTextArea;

    private SqliteUserDAO userDAO;
    private SqliteQuizDAO quizDAO;
    private SqliteQuestionDAO questionDAO;

    public CreateQuizController() {
        userDAO = new SqliteUserDAO();
        quizDAO = new SqliteQuizDAO();
        questionDAO = new SqliteQuestionDAO();
    }

    @FXML
    public void initialize() {
        // Add the question dropdown from SqliteUser DAO to the numQuestionsContainer
        ComboBox<Integer> questionDropdown = userDAO.getQuestionDropdown();
        numQuestionsContainer.getChildren().add(questionDropdown);
    }

    /**
     *Compiles user customisations choices to generate a personalised quiz, sends
     * the user to the quiz page.
     * @throws IOException
     */
    @FXML
    public void onCreate() throws IOException {

        //If the user hasn't selected a quiz mode they may not create the quiz
        if (modeToggleGroup.getSelectedToggle() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("No selected mode");
            alert.setHeaderText(null);
            alert.setContentText("You must select a quiz mode.");
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

        //Get inputted customisation inputs
        ComboBox<Integer> questionDropdown = (ComboBox<Integer>) numQuestionsContainer.getChildren().get(0);
        Integer selectedQuestions = questionDropdown.getValue();
        ToggleButton selectedDifficultyButton = (ToggleButton) difficultyGroup.getSelectedToggle();
        String selectedDifficulty = selectedDifficultyButton.getText();
        String selectedYearLevel = yearLevelComboBox.getValue();
        String userTopic = topicTextArea.getText();
        ToggleButton selectedModeButton = (ToggleButton) modeToggleGroup.getSelectedToggle();
        String selectedMode = selectedModeButton.getText();
        String selectedCountry = "Australia"; //Placeholder until country selection is implemented

        //Send request to AI
        String questionPrompt = String.format("You are a helpful assistant. Please output the following data as JSON:\n" +
                        "            {\n" +
                        "              \"Quiz\": [\n" +
                        "                {\n" +
                        "                  \"question\": \"...\",\n" +
                        "                  \"correctAnswer\": \"...\",\n" +
                        "                  \"incorrectAnswer1\": \"...\",\n" +
                        "                  \"incorrectAnswer2\": \"...\",\n" +
                        "                  \"incorrectAnswer3\": \"...\"\n" +
                        "                }\n" +
                        "              ]\n" +
                        "            }\n" +
                        "\n" +
                        "            Populate the 'quiz' array with %d entries for %d questions on the topic of %s, subject of %s %s math, %s difficulty .\n" +
                        "            Use realistic data for:\n" +
                        "            - question\n" +
                        "            - correctAnswer\n" +
                        "            - incorrectAnswer1\n" +
                        "            - incorrectAnswer2\n" +
                        "            - incorrectAnswer3\n" +
                        "\n" +
                        "            Only return valid JSON without additional commentary.\n"
                , selectedQuestions, selectedQuestions, userTopic, selectedYearLevel, selectedCountry, selectedDifficulty);
        OllamaResponse generateQuestionResponse = new OllamaResponse(questionPrompt);

        try {
            String JSONResponse = generateQuestionResponse.ollamaReturnResponse();
            String titlePrompt = String.format("Generate a single title that summarises the topic of this " +
                    "quiz:\n%s\nThe response is just one title in quotations like such: \"title\"", JSONResponse);
            OllamaResponse generateTitleResponse = new OllamaResponse(titlePrompt);
            String titleResponse = generateTitleResponse.ollamaReturnResponse();

            if(quizDAO.getQuizByName(titleResponse) != null){
                int duplicateNum = 1;
                String titleDuplicate = titleResponse + duplicateNum;
                while (quizDAO.getQuizByName(titleDuplicate) != null) {
                    duplicateNum ++;
                    titleDuplicate = titleResponse + duplicateNum;
                }
                titleResponse = titleDuplicate;
            }

            if (selectedMode.equals("Exam")) {
                String timePrompt = String.format("Generate a reasonable total time limit for a person" +
                        " in %s to complete all the following questions in this quiz:\n" +
                        "%s\nThe response is only the total time in seconds to complete the entire quiz." +
                        " Only display the seconds.", selectedYearLevel, JSONResponse);
                OllamaResponse generateTimeResponse = new OllamaResponse(timePrompt);
                String timeResponse = generateTimeResponse.ollamaReturnResponse();
            }

            User user = AuthManager.getInstance().getCurrentUser();
            Quiz quiz = new Quiz(titleResponse, userTopic, selectedMode, selectedDifficulty, selectedYearLevel, selectedCountry, user.getUserID());
            quizDAO.addQuiz(quiz);

            //Get quiz now that ID has been auto incremented in database
            quiz = quizDAO.getQuizByName(titleResponse);
            QuizManager.getInstance().setCurrentQuiz(quiz);
            int quizID = quizDAO.getQuizByName(titleResponse).getQuizID();

            questionDAO.addAIQuestions(JSONResponse, quizID);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // Send user to Quiz page
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/com/example/quizapp/Quiz.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), Main.WIDTH, Main.HEIGHT);

        // Get the controller and set the total questions
        QuizController quizController = fxmlLoader.getController();
        quizController.setTotalQuestions(selectedQuestions);
        quizController.setDifficulty(selectedDifficulty);
        quizController.setYearLevel(selectedYearLevel);

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
