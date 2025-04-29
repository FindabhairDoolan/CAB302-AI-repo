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
import java.util.Optional;

/**
 * Controller class for the create quiz page
 */
public class CreateQuizController {
    public Button backButton;

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

    @FXML
    public void onBack(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Exit Create Quiz");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to go back? Your changes may not be saved.");

        // Define Yes and No buttons
        ButtonType yesButton = new ButtonType("Yes");
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

        // Set the buttons to the alert
        alert.getButtonTypes().setAll(yesButton, noButton);

        // Show the alert and wait for user response
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == yesButton) {
            // User chose Yes – go back to the dashboard
            Stage stage = (Stage) backButton.getScene().getWindow(); // Get the current stage
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/quizapp/WelcomePage.fxml"));
            try {
                Scene scene = new Scene(fxmlLoader.load(), Main.WIDTH, Main.HEIGHT);
                stage.setScene(scene);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // If No is selected, do nothing and close the dialog
    }

    /**
     *Compiles user customisations choices to generate a personalised quiz, sends
     * the user to the quiz page.
     * @param actionEvent
     */
    public void onCreate(ActionEvent actionEvent) {
        //Get inputted customisation inputs and send to AI
        //retrieve AI response and store new quiz in database
        //Send user to Quiz page
    }

    /**
     *Sends user to the previous page they were on.
     * @param actionEvent
     * @throws IOException
     */

}
