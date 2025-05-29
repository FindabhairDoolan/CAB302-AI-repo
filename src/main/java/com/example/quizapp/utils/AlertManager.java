package com.example.quizapp.utils;

import javafx.scene.control.Alert;

/**
 * Utility class for calling Alerts
 */
public class AlertManager {

    /**
     * Displays an error alert that waits for user response
     * @param title title of the alert
     * @param content main content of the alert
     */
    public static void alertErrorWait(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Displays an error alert
     * @param title title of the alert
     * @param content main content of the alert
     */
    public static void alertErrorShow(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.show();
    }
}
