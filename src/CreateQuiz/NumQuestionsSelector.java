import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;

public class NumQuestionsSelector extends VBox {
    private ComboBox<Integer> questionDropdown;

    public NumQuestionsSelector() {
        // Create the ComboBox (dropdown) for selecting the no. of questions
        questionDropdown = new ComboBox<>();

        // Add no. of questions options
        questionDropdown.getItems().addAll(5, 10, 15, 20, 25);

        // Set default value to 5 questions
        questionDropdown.setValue(5);

        // Add the dropdown to the VBox for display purposes
        this.getChildren().add(questionDropdown);
    }

    // Method to get the selected number of questions
    public int getSelectedQuestions() {
        return questionDropdown.getValue();
    }
}
