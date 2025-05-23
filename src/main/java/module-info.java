module com.example.cab302assessment1project {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires ollama4j;
    requires org.apache.commons.lang3;
    requires org.json;
    requires com.google.gson;
    requires com.fasterxml.jackson.databind;
    requires java.sql;


    opens com.example.quizapp to javafx.fxml;
    exports com.example.quizapp;

    opens com.example.quizapp.Controllers to javafx.fxml;
    exports com.example.quizapp.Controllers;
    exports com.example.quizapp.Models;
    opens com.example.quizapp.Models to javafx.fxml;
    exports com.example.quizapp.utils;
    opens com.example.quizapp.utils to javafx.fxml;
}