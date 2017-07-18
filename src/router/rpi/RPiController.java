package router.rpi;

import com.google.gson.Gson;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashSet;

/**
 * Created by Nuwanda on 7/11/2017.
 */
public class RPiController implements Closeable {

    private static final Gson gson = new Gson();

    private Socket socket;
    private boolean runReadThread = true;
    private HashSet<RPiControllerListener> listeners = new HashSet<RPiControllerListener>();

    public boolean connect(String address, int port) {
        socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(address, port));
            new ReadThread().start();
            return true;
        } catch (Exception e) {
            System.out.print("[Error] ");
            //e.printStackTrace();
            return false;
        }
    }

    public void request(String command, Object value) {
        if (socket != null && socket.isConnected()) {
            String requestString = getJsonFrom(command, value);
            System.out.println("[Info] Request: " + requestString);

            write(requestString);
        } else {
            System.out.println("[Error] Socket is not connected");
        }
    }

    public void addListener(RPiControllerListener listener) {
        listeners.add(listener);
    }

    public void removeListener(RPiControllerListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void close() {
        runReadThread = false;
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void write(String text) {
        try {
            OutputStream stream = socket.getOutputStream();
            byte[] bytes = text.getBytes("UTF-8");
            stream.write(bytes);
        } catch (Exception e) {
            System.out.print("[Error] " + e.getMessage());
            //e.printStackTrace();
        }
    }

    private String read() {
        try {
            if (socket != null && socket.isConnected()) {
                InputStream stream = socket.getInputStream();

                byte[] bytes = new byte[1024];
                int size = stream.read(bytes);

                String responseString = new String(bytes, 0, size, "UTF-8");
                if (!responseString.contains("status")) {
                    return "";
                }

                System.out.println("[Info] Response: " + responseString);
                return responseString;
            }
        } catch (Exception e) {
            if (!e.getMessage().equals("Socket closed")) {
                System.out.print("[Error] " + e.getMessage());
            } else {
                return "";
            }
        }

        return null;
    }

    private String getJsonFrom(String command, Object value) {
        return gson.toJson(new RPiRequest(command, value));
    }

    private class ReadThread extends Thread {
        @Override
        public void run() {
            while (runReadThread) {
                String responseString = read();
                if (responseString == null) {
                    System.out.println("[Log] Response is null.");
                } else if (!responseString.equals("")) {
                    RPiResponse response = gson.fromJson(responseString, RPiResponse.class);
                    listeners.forEach(listener -> listener.onResponseRecived(response));
                }
            }
        }
    }
}
