package com.example.quizapp.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import static com.example.quizapp.Main.HEIGHT;
import static com.example.quizapp.Main.WIDTH;

public class SceneManager {

    //How to apply SceneManager to Controller for page switching:
    //  SceneManager.switchScene("/com/example/quizapp/InsertFXML_Name.fxml", "InsertWindowTitle");
    //  Example of this available in MenuBar & Quiz Controllers
    // Make sure to import import com.example.quizapp.utils.SceneManager;

    private static Stage primaryStage;

    // Set the main stage (used when launching the app)
    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    // Get the main stage (if needed)
    public static Stage getPrimaryStage() {
        if (primaryStage == null) {
            throw new IllegalStateException("Main Stage is not initialized. Please call setMainStage() first.");
        }
        return primaryStage;
    }


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
