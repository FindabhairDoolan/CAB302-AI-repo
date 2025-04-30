package com.example.quizapp.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.*;

public class HomeController extends MenuBarController {
    @FXML
    private TextField searchField;

    @FXML
    private void handleSearch() {
        String query = searchField.getText();
        System.out.println("Search query: " + query);

        // TODO: Implement actual search logic here
    }
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/quizapp/home.fxml"));
        Scene scene = new Scene(loader.load());
        primaryStage.setTitle("Home");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
