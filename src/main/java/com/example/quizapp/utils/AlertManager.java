package com.example.quizapp.utils;

import javafx.scene.control.Alert;

public class AlertManager {

    /**
     * Displays an error alert
     * @param title title of the alert
     * @param content main content of the alert
     */
    public static void alertError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
