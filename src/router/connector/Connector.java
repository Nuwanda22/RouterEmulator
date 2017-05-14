package router.connector;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by Kyle John on 5/10/2017.
 */
public class Connector {

    private Socket socket;

    public Connector()
    {
        socket = new Socket();
    }

    public Result ConnectTo(String address, int port) {
        Result result = new Result();

        try {
            socket.connect(new InetSocketAddress(address, port));
        } catch (IOException e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
            return result;
        }

        result.setSuccess(true);

        return result;
    }

    public Result Authenticate(String ssid, String password) {
        Result result = new Result();

        try {
            // Auth
            OutputStream outputStream = socket.getOutputStream();
            byte[] writeMessage = String.format("Auth %s %s", ssid, password).getBytes();
            outputStream.write(writeMessage);

            // Response
            InputStream inputStream = socket.getInputStream();
            byte[] readMessage = new byte[1024];
            int size;
            while (true){
                size = inputStream.read(readMessage);
                if(size != -1) {
                    break;
                }
            }

            // parse
            String response = new String(readMessage, 0, size);
            String[] a = response.split("-");
            if(a[0].equals("success")){
                result.setSuccess(true);
            } else {
                result.setSuccess(false);
                result.setMessage(a[1]);
            }

        } catch (IOException e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
            return result;
        }

        return result;
    }

    public Result AllocateDHCP(String hostName, String MAC) {
        Result result = new Result();

        try {
            // Allocate
            OutputStream outputStream = socket.getOutputStream();
            byte[] writeMessage = String.format("Device %s %s", hostName, MAC).getBytes();
            outputStream.write(writeMessage);

            // Response
            InputStream inputStream = socket.getInputStream();
            byte[] readMessage = new byte[1024];
            int size;
            while (true){
                size = inputStream.read(readMessage);
                if(size != -1) {
                    break;
                }
            }

            // parse
            String response = new String(readMessage, 0, size);
            String[] a = response.split("-");
            if(a[0].equals("success")){
                result.setSuccess(true);
                result.setMessage(a[1]);
            } else {
                result.setSuccess(false);
                result.setMessage(a[1]);
            }

        } catch (IOException e) {
            result.setSuccess(false);
            result.setMessage(e.getMessage());
            return result;
        }

        result.setSuccess(true);

        return result;
    }
}
