package com.example.quizapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;


/**
 * Main class that initialises the application
 */

public class Main extends Application {

//Title and standard window sizes for all pages


public static final String TITLE = "AI Quiz";
public static final int WIDTH = 800;
public static final int HEIGHT = 550;

/**
 * Initialises the application on start, displays the scene
 * @param primaryStage
 * @throws IOException
*/

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("WelcomePage.fxml"));
        Scene scene = new Scene(loader.load(), WIDTH, HEIGHT);
        primaryStage.setTitle(TITLE);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

/**
 * The main method of the project, launches JavaFX
 */

    public static void main(String[] args) {
        launch();
    }
}