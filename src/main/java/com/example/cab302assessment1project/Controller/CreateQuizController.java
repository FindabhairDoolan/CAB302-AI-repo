package com.example.cab302assessment1project.Controller;

import com.example.cab302assessment1project.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Controller class for the create quiz page
 */
public class CreateQuizController {
    public Button backButton;

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
    @FXML
    public void onBack(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) backButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
        stage.setScene(scene);
        //This will lead back to the home page in future
    }
}
