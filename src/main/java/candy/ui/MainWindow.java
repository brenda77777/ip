package candy.ui;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainWindow {

    public void show(Stage stage) {
        Label label = new Label("Candy Task Manager GUI Loaded!");

        VBox root = new VBox();
        root.getChildren().add(label);

        Scene scene = new Scene(root, 400, 300);

        stage.setTitle("Candy");
        stage.setScene(scene);
        stage.show();
    }
}