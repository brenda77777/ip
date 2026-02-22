package candy.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
        AnchorPane root = fxmlLoader.load();

        Scene scene = new Scene(root);
        scene.getStylesheets().add(Main.class.getResource("/view/MainWindow.css").toExternalForm());

        stage.setTitle("Candy");
        stage.setResizable(true);
        stage.setMaximized(false);
        stage.setMinHeight(600.0);
        stage.setMinWidth(400.0);
        stage.setScene(scene);
        stage.show();
    }
}