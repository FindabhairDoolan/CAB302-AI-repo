module com.example.cab302assessment1project {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.quizapp to javafx.fxml;
    exports com.example.quizapp;

    opens com.example.quizapp.Controllers to javafx.fxml;
    exports com.example.quizapp.Controllers;
    exports com.example.quizapp.Models;
    opens com.example.quizapp.Models to javafx.fxml;
}