module com.example.cab302assessment1project {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.cab302assessment1project to javafx.fxml;
    exports com.example.cab302assessment1project;
    exports com.example.cab302assessment1project.Controller;
    opens com.example.cab302assessment1project.Controller to javafx.fxml;
}