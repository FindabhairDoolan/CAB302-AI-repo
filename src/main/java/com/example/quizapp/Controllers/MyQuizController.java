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
import javafx.util.Callback;

import java.util.List;
import java.util.Optional;

/**
 * Controller for the "My Quizzes" view.
 * Displays quizzes created by the currently logged-in user in a table,
 * and provides buttons for taking, editing, deleting, and toggling quiz visibility.
 */
public class MyQuizController extends MenuBarController {

    /** Table view that displays the user's quizzes. */
    @FXML private TableView<Quiz> quizTable;

    /** Column for displaying the quiz name. */
    @FXML private TableColumn<Quiz, String> nameCol;

    /** Column for displaying the quiz subject. */
    @FXML private TableColumn<Quiz, String> subjectCol;

    /** Column for displaying the quiz topic. */
    @FXML private TableColumn<Quiz, String> topicCol;

    /** Column containing action buttons (Take, Edit, Delete, Toggle Visibility). */
    @FXML private TableColumn<Quiz, Void> actionCol;

    /** DAO used for retrieving and modifying quiz data from the database. */
    private final IQuizDAO quizDAO = new SqliteQuizDAO();

    /**
     * Initializes the controller.
     * Binds table columns, sets placeholder UI, and loads the user's quizzes.
     * Adds custom action buttons and toggle switch for quiz visibility.
     */
    @FXML
    public void initialize() {
        // Bind table columns to Quiz model properties
        nameCol.setCellValueFactory(new PropertyValueFactory<>("Name"));
        subjectCol.setCellValueFactory(new PropertyValueFactory<>("subject"));
        topicCol.setCellValueFactory(new PropertyValueFactory<>("Topic"));

        // Set placeholder UI when no quizzes exist
        Label noDataLabel = new Label("You haven't created any quizzes yet.");
        Button createBtn = new Button("Create Quiz");
        createBtn.setOnAction(e -> {
            SceneManager.switchScene("/com/example/quizapp/create-quiz-view.fxml", "Create Quiz");
        });
        VBox placeholder = new VBox(10, noDataLabel, createBtn);
        placeholder.setAlignment(Pos.CENTER);
        quizTable.setPlaceholder(placeholder);

        // Load current user's quizzes
        int myId = AuthManager.getInstance().getCurrentUser().getUserID();
        List<Quiz> myQuizzes = quizDAO.getQuizzesByCreator(myId);
        ObservableList<Quiz> data = FXCollections.observableArrayList();
        if (myQuizzes != null) {
            data.addAll(myQuizzes);
        }
        quizTable.setItems(data);

        QuizManager qm = QuizManager.getInstance();

        // Set up action column with buttons and toggle switch
        actionCol.setCellFactory(new Callback<>() {
            @Override
            public TableCell<Quiz, Void> call(TableColumn<Quiz, Void> col) {
                return new TableCell<>() {
                    private final Button takeBtn = new Button("Take");
                    private final Button editBtn = new Button("Edit");
                    private final Button deleteBtn = new Button("Delete");
                    private final ToggleButton visTog = new ToggleButton();
                    private final Region thumb = new Region();
                    private final HBox box = new HBox(12, takeBtn, editBtn, deleteBtn, visTog);

                    {
                        // Style the toggle button
                        visTog.getStyleClass().add("switch-toggle");
                        visTog.setFocusTraversable(false);
                        thumb.getStyleClass().add("graphic");
                        visTog.setGraphic(thumb);
                        visTog.setContentDisplay(ContentDisplay.LEFT);

                        // Action: Take quiz
                        takeBtn.setOnMouseClicked(e -> {
                            Quiz quiz = getTableView().getItems().get(getIndex());
                            qm.setCurrentQuiz(quiz);
                            qm.openQuiz(quiz);
                        });

                        // Action: Edit quiz
                        editBtn.setOnAction(e -> {
                            SceneManager.switchScene("/com/example/quizapp/quiz-editor.fxml", "Edit Quiz");
                        });

                        // Action: Delete quiz
                        deleteBtn.setOnAction(e -> {
                            Quiz q = getTableView().getItems().get(getIndex());
                            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                                    "Delete \"" + q.getName() + "\"? This cannot be undone.",
                                    ButtonType.YES, ButtonType.NO);
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

                        if (!empty) {
                            Quiz quiz = getTableView().getItems().get(getIndex());

                            // Set initial toggle state and label based on visibility
                            if ("Public".equals(quiz.getVisibility())) {
                                visTog.setSelected(false);
                                visTog.setText("Public");
                            } else {
                                visTog.setSelected(true);
                                visTog.setText("Private");
                            }

                            // Listener for visibility toggle
                            visTog.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
                                String newVisibility = visTog.isSelected() ? "Private" : "Public";
                                visTog.setText(newVisibility);
                                quiz.setVisibility(newVisibility);
                                quizDAO.updateQuiz(quiz);

                                // Notify user of change
                                String message = newVisibility.equals("Public")
                                        ? "Everyone can see and take your quiz."
                                        : "Only you can see and take your quiz.";
                                Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION,
                                        "'" + quiz.getName() + "' has been set to " + newVisibility + ". " + message,
                                        ButtonType.OK);
                                confirmation.setTitle("Visibility Changed");
                                confirmation.setHeaderText(null);
                                confirmation.showAndWait();
                            });
                        }
                    }
                };
            }
        });
    }
}
