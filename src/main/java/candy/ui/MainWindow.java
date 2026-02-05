package candy.ui;

import candy.Candy;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;

public class MainWindow {
    private final Candy candy = new Candy();

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private VBox dialogContainer;

    @FXML
    private TextField userInput;

    @FXML
    private Button sendButton;

    private final Image userImage =
            new Image(this.getClass().getResourceAsStream("/images/user1.jpeg"));
    private final Image dukeImage =
            new Image(this.getClass().getResourceAsStream("/images/user2.jpeg"));

    @FXML
    public void initialize() {
        dialogContainer.getChildren().add(
                DialogBox.getCandyDialog("Hello! I'm Candy 😊\nWhat can I do for you?", dukeImage)
        );

        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setVvalue(1.0);

        dialogContainer.heightProperty().addListener(
                (obs, oldVal, newVal) -> scrollPane.setVvalue(1.0)
        );
    }

    @FXML
    private void handleUserInput() {
        String userText = userInput.getText();
        String candyText = candy.getResponse(userText);

        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(userText, userImage),
                DialogBox.getCandyDialog(candyText, dukeImage)
        );

        userInput.clear();
    }
}