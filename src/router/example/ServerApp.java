package router.example;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.application.Platform;

/**
 * Created by Kyle John on 5/10/2017.
 */
public class ServerApp extends Application {

    // WINDOWS TOOL
    AnchorPane ROOT = new AnchorPane();
    Button startBtn = new Button("SERVER START");
    Label isOK = new Label();
    Label totalCount = new Label();
    TextField inputField = new TextField();
    TextField outputField = new TextField();

    // NETWORK TOOL
    List<Joiner> li = new ArrayList<Joiner>();
    ServerSocket mainServerSocket= null;

    // main
    public static void main(String[] args) {
        launch();
    }

    // Client Socket ar for ArrayList
    class Joiner {
        Socket UserInfo_socket;

        Joiner(Socket socketNumber) {
            UserInfo_socket = socketNumber;
        }
    }

    // Connect class
    class ConnectThread extends Thread {
        ServerSocket mainServerSocket = null;

        ConnectThread(ServerSocket mainServerSocket) {
            this.mainServerSocket = mainServerSocket;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    Socket serverSocket = mainServerSocket.accept();
                    outputField.setText("사용자 접속!");

                    li.add(new Joiner(serverSocket));
                    Platform.runLater(() -> {
                        totalCount.setText(li.size() + " 명");
                    });

                    ServerReader serverReader = new ServerReader(serverSocket);
                    serverReader.start();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Reader class
    class ServerReader extends Thread {
        Socket serverSocket = null;

        ServerReader(Socket serverSocket) {
            this.serverSocket = serverSocket;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    InputStream inputStream = serverSocket.getInputStream();
                    byte[] byteArray = new byte[256];
                    int size = inputStream.read(byteArray);

                    if (size == -1) { // if(Logout)
                        for (int i = 0; i < li.size();) {
                            if (serverSocket == li.get(i).UserInfo_socket) {
                                li.remove(i);
                            } else {
                                i++;
                            }
                            Platform.runLater(() -> {
                                totalCount.setText(li.size() + " 명");
                            });
                        }
                        break;
                    }

                    String readMessage = new String(byteArray, 0, size, "UTF-8");
                    outputField.setText(readMessage);
                }
            } catch (Exception e) {
                for (int i = 0; i < li.size();) {
                    if (serverSocket == li.get(i).UserInfo_socket) {
                        li.remove(i);
                    } else {
                        i++;
                    }
                    Platform.runLater(() -> {
                        totalCount.setText(li.size() + " 명");
                    });
                }
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
        ROOT.getChildren().add(totalCount);

        // Label
        isOK.setLayoutX(130);
        isOK.setLayoutY(15);
        totalCount.setLayoutX(340);
        totalCount.setLayoutY(15);
        totalCount.setText(li.size() + " 명");

        // Server Start btn
        startBtn.setPrefHeight(20);
        startBtn.setPrefWidth(100);
        startBtn.setLayoutX(10);
        startBtn.setLayoutY(10);
        startBtn.setOnAction((event) -> {
            if (startBtn.getText().equals("SERVER START")) {
                try {
                    mainServerSocket = null;
                    mainServerSocket = new ServerSocket();
                    mainServerSocket.bind(new InetSocketAddress(InetAddress.getLocalHost(), 4040));
                    isOK.setText("서버가 열렸습니다");
                    startBtn.setText("SERVER STOP");
                    totalCount.setText(li.size() + " 명");

                    ConnectThread connectThread = new ConnectThread(mainServerSocket);
                    connectThread.start();
                } catch (Exception e) {
                }
            } else if (startBtn.getText().equals("SERVER STOP")) {
                try {
                    inputField.clear();
                    outputField.clear();
                    isOK.setText("서버가 닫혔습니다");
                    startBtn.setText("SERVER START");

                    // to send 'SERVER CLOSE' message to Client
                    String sendMessage = "FFFF";
                    byte[] byteArray = sendMessage.getBytes("UTF-8");
                    for (int i = 0; i < li.size(); i++) {
                        OutputStream outputStream = li.get(i).UserInfo_socket.getOutputStream();
                        outputStream.write(byteArray);
                    }
                    li.clear();
                    mainServerSocket.close();
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
                    byte[] byteArray = sendMessage.getBytes("UTF-8");
                    for (int i = 0; i < li.size(); i++) {
                        OutputStream outputStream = li.get(i).UserInfo_socket.getOutputStream();
                        outputStream.write(byteArray);
                    }
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
        stage.setTitle("SERVER");
        stage.show();
    }//
}
