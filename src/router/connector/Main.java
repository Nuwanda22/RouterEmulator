package router.connector;

import java.util.Scanner;

/**
 * Created by Nuwanda on 2017-05-02.
 */
public class Main {
    public static void main(String[] args) {
        Connector connector = new Connector();
        Manager manager = new Manager();

        // Connect
        System.out.println("Connecting to server...");
        Result connection = connector.ConnectTo(args[1], Integer.parseInt(args[2]));
        if (!connection.isSuccess()) {
            System.out.println("Error: " + connection.getMessage());
            return;
        } else {
            System.out.println("Connected to server.\n");
        }

        // Auth
        System.out.println("Authenticating...");
        System.out.printf("SSID: %s | Password: %s\n", args[3], args[4]);
        Result authentication = connector.Authenticate(args[3], args[4]);
        if (!authentication.isSuccess()) {
            System.out.println("Error: " + authentication.getMessage());
            return;
        } else {
            System.out.println("Authenticated.\n");
        }

        // Allocate
        System.out.println("Allocating DHCP...");
        Result allocation = connector.AllocateDHCP(manager.getHostName(), manager.getMACAddress());
        if (!allocation.isSuccess()) {
            System.out.println("Error: " + allocation.getMessage());
            return;
        } else {
            System.out.println("Allocated.");
            System.out.println("Your IP address is " + allocation.getMessage());
        }

        // Wait to exit
        System.out.println("Press enter to close connection.");
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        System.out.println("Closed.");
    }
}
