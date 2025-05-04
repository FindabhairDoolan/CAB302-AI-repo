package com.example.quizapp.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import static com.example.quizapp.Main.HEIGHT;
import static com.example.quizapp.Main.WIDTH;

public class SceneManager {


    public static void switchScene(String fxmlPath, String title, Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(SceneManager.class.getResource(fxmlPath));
            Scene newScene = new Scene(loader.load(), WIDTH, HEIGHT);

            stage.setScene(newScene);
            stage.setTitle(title);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not load FXML file: " + fxmlPath);
        }
    }
}
