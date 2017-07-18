package router.controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("main.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Router");
        primaryStage.setScene(new Scene(root, 500, 300));
        primaryStage.show();

        // block user access
        primaryStage.setOnCloseRequest((event) -> {
//            loader.<MainController>getController().cancelTimer();
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
