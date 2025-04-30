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

/**
 * Controller class for the create quiz page
 */
public class CreateQuizController {
    public Button backButton;
    public Button createButton;

    @FXML
    private VBox numQuestionsContainer; // Reference to the VBox in FXML
    private SqliteUserDAO userDAO; // Declare SqliteUser DAO

    public CreateQuizController() {
        userDAO = new SqliteUserDAO(); // Initialize the SqliteUser DAO
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
     * @param actionEvent
     * @@throws IOException
     */
    public void onCreate(ActionEvent actionEvent) throws IOException {
        //Get inputted customisation inputs and send to AI
        //retrieve AI response and store new quiz in database

        //Send user to Quiz page
        Stage stage = (Stage) createButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("quiz.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), Main.WIDTH, Main.HEIGHT);
        stage.setScene(scene);
    }

    /**
     *Sends user to the previous page they were on.
     * @param actionEvent
     * @throws IOException
     */
    @FXML
    public void onBack(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) backButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("home.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), Main.WIDTH, Main.HEIGHT);
        stage.setScene(scene);
    }
}
