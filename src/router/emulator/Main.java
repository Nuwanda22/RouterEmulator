package router.emulator;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

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
            loader.<MainController>getController().cancelTimer();
        });

        //new ConnectThread(loader.getController()).start();
    }

    public static void main(String[] args) {
        launch(args);
    }

    class ConnectThread extends Thread {

        private ServerSocket serverSocket;
        private MainController controller;

        public ConnectThread(MainController controller) throws IOException {
            this.serverSocket = new ServerSocket();
            this.controller = controller;
        }

        @Override
        public void run() {
            try {
                serverSocket.bind(new InetSocketAddress("127.0.0.1", 12345));
                System.out.println("Open server 127.0.0.1:12345");
                System.out.println("Waiting to client...\n");

                while (true) {
                    Socket socket = serverSocket.accept();
                    new ClientBinderThread(socket, controller).start();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static int id;
    class ClientBinderThread extends Thread {

        private Socket clientSocket;
        private MainController controller;


        public ClientBinderThread(Socket clientSocket, MainController controller){
            this.clientSocket = clientSocket;
            this.controller = controller;
id++;
            System.out.println(id + " Accept client");
        }

        @Override
        public void run() {
            while (true) {
                try {
                    InputStream inputStream = clientSocket.getInputStream();
                    byte[] received = new byte[1024];
                    int size = inputStream.read(received);
                    System.out.println("Size: " + size);

                    String message = new String(received, 0, size);
                    System.out.println("Message: " + message);
                    String[] a = message.split(" ");

                    OutputStream outputStream = clientSocket.getOutputStream();
                    if(a[0].equals("Auth")) {
                        String[] output = null;
                        String result = controller.Authenticate(a[1], a[2], output) ? "success" : "fail-" + output[0];
                        outputStream.write(result.getBytes());
                    } else if(a[0].equals("Device")) {
                        String[] output = null;
                        String result = controller.AllocateDHCP(a[1], a[2], output) ? "success" : "fail-" + output[0];
                        outputStream.write(result.getBytes());
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
