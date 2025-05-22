package com.example.quizapp.Controllers;

import com.example.quizapp.Models.*;
import com.example.quizapp.utils.AuthManager;
import com.example.quizapp.utils.QuizManager;
import com.example.quizapp.utils.SceneManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.util.List;
import java.util.Optional;

public class MyQuizController extends MenuBarController {
    @FXML private TableView<Quiz> quizTable;
    @FXML private TableColumn<Quiz, String> nameCol;
    @FXML private TableColumn<Quiz, String> subjectCol;
    @FXML private TableColumn<Quiz, String> topicCol;
    @FXML private TableColumn<Quiz, Void> actionCol;

    private final IQuizDAO quizDAO = new SqliteQuizDAO();

    @FXML
    public void initialize() {
        // 1) Bind data columns
        nameCol   .setCellValueFactory(new PropertyValueFactory<>("Name"));
        subjectCol.setCellValueFactory(new PropertyValueFactory<>("subject"));
        topicCol  .setCellValueFactory(new PropertyValueFactory<>("Topic"));

        // 2) Placeholder when there are no quizzes
        Label noDataLabel = new Label("You haven't created any quizzes yet.");
        Button createBtn   = new Button("Create Quiz");
        createBtn.setOnAction(e -> {
            SceneManager.switchScene(
                    "/com/example/quizapp/create-quiz-view.fxml",
                    "Create Quiz"
            );
        });
        VBox placeholder = new VBox(10, noDataLabel, createBtn);
        placeholder.setAlignment(Pos.CENTER);
        quizTable.setPlaceholder(placeholder);

        // 3) Load only quizzes by the logged-in user
        int myId = AuthManager.getInstance().getCurrentUser().getUserID();
        List<Quiz> myQuizzes = quizDAO.getQuizzesByCreator(myId);
        ObservableList<Quiz> data = FXCollections.observableArrayList();
        if (myQuizzes != null) {
            data.addAll(myQuizzes);
        }
        quizTable.setItems(data);

        // 4) Add Take/Edit/Delete buttons
        actionCol.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Quiz, Void> call(TableColumn<Quiz, Void> col) {
                return new TableCell<>() {
                    private final Button takeBtn   = new Button("Take");
                    private final Button editBtn   = new Button("Edit");
                    private final Button deleteBtn = new Button("Delete");
                    //Toggle switch and knob
                    private final ToggleButton visTog = new ToggleButton("Public");
                    private final Region thumb = new Region();

                    private final HBox box         = new HBox(12, takeBtn, editBtn, deleteBtn, visTog);

                    {
                        visTog.getStyleClass().add("switch-toggle");
                        visTog.setFocusTraversable(false);
                        thumb.getStyleClass().add("graphic"); // .graphic targets the knob in CSS
                        visTog.setGraphic(thumb);
                        visTog.setContentDisplay(ContentDisplay.LEFT);


                        visTog.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
                            visTog.setText(isNowSelected ? "Private" : "Public");
                            // Optionally update the model or save change:
                            Quiz quiz = getTableView().getItems().get(getIndex());
                            quiz.setVisibility(isNowSelected ? "Private" : "Public");
                            quizDAO.updateQuiz(quiz); // update DB if needed
                        });

                        takeBtn.setOnMouseClicked(e -> {
                            //Stash Quiz in Quiz manager when button is clicked
                            Quiz quiz = getTableView().getItems().get(getIndex());
                            //get instance of Quiz Manager
                            QuizManager qm = QuizManager.getInstance();
                            //Get instance of homeController to use dynamic method
                            qm.setCurrentQuiz(quiz);
                            //Open quiz
                            qm.openQuiz(quiz);
                        });
                        editBtn.setOnAction(e -> {
                            SceneManager.switchScene("/com/example/quizapp/quiz-editor.fxml", "Edit Quiz");
                        });
                        visTog.setOnAction(e -> {
                                                    });
                        deleteBtn.setOnAction(e -> {
                            Quiz q = getTableView().getItems().get(getIndex());
                            Alert confirm = new Alert(
                                    Alert.AlertType.CONFIRMATION,
                                    "Delete \"" + q.getName() + "\"? This cannot be undone.",
                                    ButtonType.YES, ButtonType.NO
                            );
                            confirm.setTitle("Confirm Deletion");
                            confirm.setHeaderText(null);

                            Optional<ButtonType> result = confirm.showAndWait();
                            if (result.orElse(ButtonType.NO) == ButtonType.YES) {
                                quizDAO.deleteQuiz(q);
                                getTableView().getItems().remove(q);
                            }
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        setGraphic(empty ? null : box);
                    }
                };
            }
        });
    }
}
