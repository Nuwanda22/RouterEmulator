package router.example;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * Created by Kyle John on 5/10/2017.
 */
public class ClientApp extends Application {

    // WINDOWS TOOL
    AnchorPane ROOT = new AnchorPane();
    Button startBtn = new Button("JOIN");
    Label isOK = new Label();
    TextField inputField = new TextField();
    TextField outputField = new TextField();

    // NETWORK TOOL
    Scanner input = new Scanner(System.in);
    Socket clientSocket= null;

    // main
    public static void main(String[] args) {
        launch();
    }

    // Reader class
    class ClientReader extends Thread {
        Socket clientSocket = null;

        ClientReader(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    InputStream inputStream = clientSocket.getInputStream();
                    byte[] byteArray = new byte[256];
                    int size = inputStream.read(byteArray);

                    String readMessage = new String(byteArray, 0, size, "UTF-8");
                    if(readMessage.equals("FFFF")) {
                        inputField.clear();
                        outputField.clear();
                        readMessage = "서버가 종료되었습니다";
                        Platform.runLater(() -> {
                            startBtn.setText("JOIN");
                        });
                    }
                    outputField.setText(readMessage);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Application start
    @Override
    public void start(Stage stage) throws Exception {
        ROOT.setPrefHeight(400);
        ROOT.setPrefWidth(500);
        ROOT.getChildren().add(startBtn);
        ROOT.getChildren().add(isOK);
        ROOT.getChildren().add(inputField);
        ROOT.getChildren().add(outputField);

        // Label
        isOK.setLayoutX(130);
        isOK.setLayoutY(15);

        // SERVER JOIN btn
        startBtn.setPrefHeight(20);
        startBtn.setPrefWidth(100);
        startBtn.setLayoutX(10);
        startBtn.setLayoutY(10);
        startBtn.setOnAction((event) -> {
            if (startBtn.getText().equals("JOIN")) {
                try {
                    clientSocket = new Socket();
                    clientSocket.connect(new InetSocketAddress(InetAddress.getLocalHost(), 4040));
                    isOK.setText("서버에 접속하였습니다");
                    startBtn.setText("LOGOUT");

                    ClientReader clientReader = new ClientReader(clientSocket);
                    clientReader.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (startBtn.getText().equals("LOGOUT")) {
                try {
                    isOK.setText("서버에 접속해제하였습니다");
                    startBtn.setText("JOIN");
                    clientSocket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // input Field
        inputField.setLayoutX(10);
        inputField.setLayoutY(60);
        inputField.setPrefWidth(350);
        inputField.setOnKeyPressed((event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                try {
                    String sendMessage = inputField.getText();
                    OutputStream outputStream = clientSocket.getOutputStream();
                    byte[] byteArray = sendMessage.getBytes("UTF-8");
                    outputStream.write(byteArray);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                inputField.clear();
            }
        });

        // output Field
        outputField.setLayoutX(10);
        outputField.setLayoutY(100);
        outputField.setPrefWidth(350);

        // stage
        Scene scene = new Scene(ROOT);
        stage.setScene(scene);
        stage.setTitle("CLIENT");
        stage.show();
    }//
}
