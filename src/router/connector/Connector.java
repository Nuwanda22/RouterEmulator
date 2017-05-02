package router.connector;

/**
 * Created by Nuwanda on 2017-05-02.
 */
public class Connector {
    public static void main(String[] args) {
        String command = args[0];
        String address = args[1];
        String port = args[2];

        System.out.println("Connecting to " + address + ":" + port);
        Manager m = new Manager();
        System.out.println(m.getHostName() + " " + m.getMACAddress());
//        try {
//            Socket socket = new Socket(address, Integer.parseInt(port));
//            OutputStream in = socket.getOutputStream();
//            DataOutputStream dis = new DataOutputStream(in);
//
//            Runtime.getRuntime().addShutdownHook(new Thread(()->{
//
//            }));
//
//            dis.close();
//            in.close();
//            socket.close();
//        }
//        catch (IOException e) {
//            System.out.println("Error: \n\t"+ e.getMessage());
//        }
    }
}
