package com.example.cab302assessment1project.Controller;

import com.example.cab302assessment1project.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class CreateQuizController {
    public Button backButton;

    public void onCreate(ActionEvent actionEvent) {
    }

    //When back button is pressed, returns user to previous page
    @FXML
    public void onBack(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) backButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
        stage.setScene(scene);
        //This will lead back to the home page in future
    }
}
