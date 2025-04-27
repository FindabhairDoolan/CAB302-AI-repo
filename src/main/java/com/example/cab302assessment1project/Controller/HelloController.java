package com.example.cab302assessment1project.Controller;

import com.example.cab302assessment1project.HelloApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloController {
    public Button tempButton;

    @FXML
    protected void onTempButtonClick() throws IOException {
        Stage stage = (Stage) tempButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("create-quiz-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), HelloApplication.WIDTH, HelloApplication.HEIGHT);
        stage.setScene(scene);
        //Will be in the home page controller, this is just temporary
    }
}