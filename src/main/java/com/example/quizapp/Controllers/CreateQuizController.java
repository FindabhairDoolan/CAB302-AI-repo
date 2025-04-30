package com.example.quizapp.Controllers;

import com.example.quizapp.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import com.example.quizapp.Models.SqliteUserDAO;

import java.io.IOException;
import java.util.Optional;

/**
 * Controller class for the create quiz page
 */
public class CreateQuizController {
    public Button backButton;
    public Button createButton;

    @FXML
    private VBox numQuestionsContainer;
    private SqliteUserDAO userDAO;

    public CreateQuizController() {
        userDAO = new SqliteUserDAO();
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
     * @@throws IOException
     */
    public void onCreate() throws IOException {
        //Get inputted customisation inputs and send to AI
        ComboBox<Integer> questionDropdown = (ComboBox<Integer>) numQuestionsContainer.getChildren().get(0);
        Integer selectedQuestions = questionDropdown.getValue();

        // Send user to Quiz page
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/com/example/quizapp/quiz.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), Main.WIDTH, Main.HEIGHT);

        // Get the controller and set the total questions
        QuizController quizController = fxmlLoader.getController();
        quizController.setTotalQuestions(selectedQuestions);

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
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("home.fxml"));
            try {
                Scene scene = new Scene(fxmlLoader.load(), Main.WIDTH, Main.HEIGHT);
                stage.setScene(scene);
                //This will lead back to the home page in future
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // If No is selected, do nothing

    }
}
