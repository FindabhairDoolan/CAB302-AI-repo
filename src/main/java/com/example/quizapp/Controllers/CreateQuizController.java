package com.example.quizapp.Controllers;

import com.example.quizapp.Main;
import com.example.quizapp.Models.*;
import com.example.quizapp.utils.AlertManager;
import com.example.quizapp.utils.AuthManager;
import com.example.quizapp.utils.QuizManager;
import com.example.quizapp.utils.SceneManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

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
    private TextArea topicTextArea;

    private ComboBox<Integer> questionDropdown;
    private SqliteUserDAO userDAO;
    private SqliteQuizDAO quizDAO;
    private SqliteQuestionDAO questionDAO;


    /**
     * Constructor for the Create Quiz controller
     */
    public CreateQuizController() {
        userDAO = new SqliteUserDAO();
        quizDAO = new SqliteQuizDAO();
        questionDAO = new SqliteQuestionDAO();
    }

    /**
     * Initialises variables in the create quiz controller
     */
    @FXML
    public void initialize() {
        questionDropdown = quizDAO.getQuestionDropdown();
        numQuestionsContainer.getChildren().add(questionDropdown);
    }

    /**
     * Compiles user customisation choices to generate a personalised quiz using AI
     * Stores generated quiz in the database
     * Prompts user if they want to take the quiz once generated.
     */
    @FXML
    public void onCreate() throws IOException {

        //Check if all quiz settings are input and valid
        boolean validSettings = validateQuizSettings();
        if (!validSettings) {
            return;
        }

        //Display information window and cancel generation if user cancels
        boolean confirmation = confirmGenerate();
        if (!confirmation) {
            return;
        }

        //For debug purposes
        System.out.println("Generating quiz...");

        //Get inputted customisation inputs
        ComboBox<Integer> questionDropdown = (ComboBox<Integer>) numQuestionsContainer.getChildren().get(0);
        Integer selectedQuestions = questionDropdown.getValue();
        ToggleButton selectedDifficultyButton = (ToggleButton) difficultyGroup.getSelectedToggle();
        String selectedDifficulty = selectedDifficultyButton.getText();
        String selectedYearLevel = yearLevelComboBox.getValue();
        String selectedSubject = subjectComboBox.getValue();
        String userTopic = topicTextArea.getText();
        String selectedCountry = "Australia"; //Placeholder for if country selection is implemented

        //The prompt sent to the AI to generate the quiz
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
                        "            Populate the 'quiz' array with %d entries for %d questions on the topic of %s, subject of %s %s %s, %s difficulty .\n" +
                        "            Use realistic data for:\n" +
                        "            - question\n" +
                        "            - correctAnswer\n" +
                        "            - incorrectAnswer1\n" +
                        "            - incorrectAnswer2\n" +
                        "            - incorrectAnswer3\n" +
                        "\n" +
                        "            Only return valid JSON without additional commentary.\n"
                , selectedQuestions, selectedQuestions, userTopic, selectedYearLevel, selectedCountry, selectedSubject, selectedDifficulty);
        OllamaResponse generateQuestionResponse = new OllamaResponse(questionPrompt);

        try {
            //Get quiz response from AI
            String JSONResponse = generateQuestionResponse.ollamaReturnResponse();

            //The prompt sent to the AI to generate a title
            String titlePrompt = String.format("Generate a single title that summarises the topic of this " +
                    "quiz:\n%s\nThe response is just one title in quotations like such: \"title\"", JSONResponse);
            OllamaResponse generateTitleResponse = new OllamaResponse(titlePrompt);
            String titleResponse = generateTitleResponse.ollamaReturnResponse();
            //Remove quotations around the title
            titleResponse = titleResponse.replaceAll("^\"|\"$", "");

            //Ensure the quiz title is unique if duplicates
            titleResponse = generateUniqueQuizTitle(titleResponse);

            //Request the AI to generate a default timer
            String timePrompt = String.format("You are a helpful assistant. Please output the following data as JSON:\n" +
                    "            {\n" +
                    "              \"Quiz\": [\n" +
                    "                {\n" +
                    "                  \"timerSeconds\": \"...\",\n" +
                    "                }\n" +
                    "              ]\n" +
                    "            }\n" +
                    "\n" +
                    "            Populate the 'quiz' array with a timer in seconds, the time should be a number with no unit provided." +
                    " The time is based upon the provided quiz questions for a %s Australian student:\n" +
                    "*Generated quiz here*\n" +
                    "\n" +
                    "            Use realistic data for:\n" +
                    "            - timerSeconds\n" +
                    "\n" +
                    "            Only return valid JSON without additional commentary.\n", selectedYearLevel, JSONResponse);
            OllamaResponse generateTimeResponse = new OllamaResponse(timePrompt);
            int timeResponse = quizDAO.retrieveTimer(generateTimeResponse.ollamaReturnResponse());

            //Get current user to make them the quiz creator
            User user = AuthManager.getInstance().getCurrentUser();

            //Add generated quiz to database
            Quiz quiz = new Quiz(titleResponse, selectedSubject, userTopic, timeResponse, selectedDifficulty, selectedYearLevel, selectedCountry, "Public", user.getUserID());
            quizDAO.addQuiz(quiz);

            //Get the current quiz now that ID has been auto incremented in database
            quiz = quizDAO.getQuizByName(titleResponse);
            QuizManager.getInstance().setCurrentQuiz(quiz);
            int quizID = quizDAO.getQuizByName(titleResponse).getQuizID();

            //Add the generated questions to the database
            questionDAO.addAIQuestions(JSONResponse, quizID);

            //Navigate to the quiz page
            QuizManager quizManager = QuizManager.getInstance();
            quizManager.openQuiz(quiz);


        } catch (Exception ex) {
            ex.printStackTrace();

            //Delete wrongly generated quiz and questions if exists
            Quiz failedQuiz = QuizManager.getInstance().getCurrentQuiz();
            List<Question> failedQuestions = questionDAO.getQuestionsForQuiz(failedQuiz.getQuizID());
            for (Question question : failedQuestions) {
                questionDAO.deleteQuestion(question);
            }
            quizDAO.deleteQuiz(failedQuiz);

            //Display error window
            AlertManager.alertErrorWait("Quiz generation error", "An error occurred while generating your quiz, please try again.");
        }
    }

    /**
     * Sends user back to Home page if they confirm
     * @throws IOException
     */
    @FXML
    public void onBack() throws IOException {
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
            SceneManager.switchScene("/com/example/quizapp/home.fxml", "Quiz Master");
        }
            // If No is selected, do nothing

    }

    /**
     *Displays a confirmation alert regarding if the user wishes to start quiz generation
     * @return boolean: true if user confirms, false if user cancels
     */
    private boolean confirmGenerate() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm quiz generation");
        alert.setHeaderText(null);
        alert.setContentText("Quiz generation process may take up to a few minutes, start generation?");

        // Define Yes and No buttons
        ButtonType yesButton = new ButtonType("Yes");
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

        // Set the buttons to the alert
        alert.getButtonTypes().setAll(yesButton, noButton);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == noButton) {
            // User chose Yes – go to dashboard
            SceneManager.switchScene("/com/example/quizapp/home.fxml", "Quiz Master");
            return false;
        }

        // If Yes is selected, start generation
        return true;
    }

    /**
     * Takes title of a quiz and ensure it is unique for the current user
     * Adds a number to the title indicating duplicate number if it is a duplicate
     * @param title The title of the quiz to ensure uniqueness
     * @return unique title for the quiz
     */
    private String generateUniqueQuizTitle (String title){
        User user = AuthManager.getInstance().getCurrentUser();
        Quiz existingQuiz = quizDAO.getQuizByName(title);

        //If there is a duplicate quiz, and its by the same user
        if (existingQuiz != null && existingQuiz.getCreatorID() == user.getUserID()) {
            int duplicateNum = 1;
            String titleDuplicate = title + "(" + duplicateNum + ")";
            //While there is still a duplicate
            while (quizDAO.getQuizByName(titleDuplicate) != null) {
                duplicateNum++;
                titleDuplicate = title + "(" + duplicateNum + ")";
            }
            title = titleDuplicate;
        }
        return title;
    }

    /**
     * Checks if all required quiz settings are selected
     * @return boolean: true if all settings are selected, false if a setting is not inputted
     */
    public boolean validateQuizSettings () {
        // Check if a subject has been selected
        if (subjectComboBox.getValue() == null) {
            AlertManager.alertErrorWait("No selected subject","You must select a subject.");
            return false;
        }

        // Check if a year level has been selected
        if (yearLevelComboBox.getValue() == null) {
            AlertManager.alertErrorWait("No selected year level", "You must select a year level.");
            return false;
        }

        // Check if a difficulty level has been selected
        if (difficultyGroup.getSelectedToggle() == null) {
            AlertManager.alertErrorWait("No selected difficulty", "You must select a difficulty level.");
            return false;
        }

        // Check if a topic has been input
        if (topicTextArea.getText().isBlank()) {
            AlertManager.alertErrorWait("No topic input", "You must input a topic for the quiz.");
            return false;
        }

        return true;
    }


}