package com.example.quizapp.Controllers;

import com.example.quizapp.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.*;

import java.io.IOException;

public class HomeController extends MenuBarController {
    @FXML
    private TextField searchField;
    @FXML
    public Button tempButton;

    @FXML
    private void handleSearch() {
        String query = searchField.getText();
        System.out.println("Search query: " + query);

        // TODO: Implement actual search logic here
    }

    @FXML
    protected void onTempButtonClick() throws IOException {
        Stage stage = (Stage) tempButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("create-quiz-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 550);
        stage.setScene(scene);
    }

    @FXML
    protected void onTempButton2Click() throws IOException {
        Stage stage = (Stage) tempButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("quiz-history.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 550);
        stage.setScene(scene);
    }

    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/quizapp/home.fxml"));
        Scene scene = new Scene(loader.load());
        primaryStage.setTitle("Home");
        primaryStage.setScene(scene);
        primaryStage.show();
    }



}
