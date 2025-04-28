package com.example.cab302assessment1project;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Main class that initialises the application
 */
public class HelloApplication extends Application {
    //Title and standard window sizes for all pages
    public static final String TITLE = "AI Quiz";
    public static final int WIDTH = 800;
    public static final int HEIGHT = 550;

    /**
     * Initialises the application on start, displays the scene
     * @param stage
     * @throws IOException
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * The main method of the project, launches JavaFX
     */
    public static void main(String[] args) {
        launch();
    }
}