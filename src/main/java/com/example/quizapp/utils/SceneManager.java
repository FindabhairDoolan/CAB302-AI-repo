package com.example.quizapp.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import static com.example.quizapp.Main.HEIGHT;
import static com.example.quizapp.Main.WIDTH;
/**
 * SceneManager is a utility class for managing scene transitions in a JavaFX application.
 */
public class SceneManager {

    /** The primary stage used throughout the application. */
    private static Stage primaryStage;

    /**
     * Sets the primary stage. This method should be called once when the application launches.
     *
     * @param stage The main application stage.
     */
    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    /**
     * Returns the primary stage of the application.
     *
     * @return The main
     * @throws IllegalStateException if the stage has not been initialized via {@link #setPrimaryStage(Stage)}.
     */
    public static Stage getPrimaryStage() {
        if (primaryStage == null) {
            throw new IllegalStateException("Main Stage is not initialized. Please call setMainStage() first.");
        }
        return primaryStage;
    }

    /**
     * Switches to a new scene specified by an FXML file.
     *
     * @param fxmlPath The relative path to the FXML file (e.g., "/com/example/quizapp/Quiz.fxml").
     * @param title    The title to set for the stage window.
     * @throws RuntimeException if the FXML file cannot be loaded.
     */
    public static void switchScene(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxmlPath));
            Scene newScene = new Scene(loader.load(), WIDTH, HEIGHT);

            Stage stage = getPrimaryStage();

            stage.setScene(newScene);
            stage.setTitle(title);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not load FXML file: " + fxmlPath);
        }
    }
    /**
     * Switches to a new scene and returns the controller instance of the newly loaded FXML.
     *
     * @param fxmlPath The relative path to the FXML file (e.g., "/com/example/quizapp/Quiz.fxml").
     * @param title    The title to set for the stage window.
     * @param <T>      The type of the controller.
     * @return The controller instance associated with the loaded FXML.
     * @throws RuntimeException if the FXML file cannot be loaded.
     */
    public static <T> T switchSceneWithController(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxmlPath));
            Scene newScene = new Scene(loader.load(), WIDTH, HEIGHT);

            Stage stage = getPrimaryStage();

            stage.setScene(newScene);
            stage.setTitle(title);
            stage.show();

            return loader.getController();

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not load FXML file: " + fxmlPath);
        }
    }
}
