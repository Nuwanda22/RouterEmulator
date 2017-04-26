package sample;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Main extends Application {
    private final TableView table = new TableView();

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));

        GridPane page = (GridPane)FXMLLoader.load(Main.class.getResource("sample.fxml"));
        Scene scene = new Scene(page);

        TableView tableView = (TableView) scene.lookup("#connectedTable");

        primaryStage.setTitle("Wireless Router Emulator");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 1000, 400));
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
