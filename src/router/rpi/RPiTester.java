package router.rpi;

import java.util.Scanner;

/**
 * Created by Nuwanda on 7/12/2017.
 */
public class RPiTester {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        RPiController controller = new RPiController();
        controller.addListener(new RPiControllerListener() {
            @Override
            public void onResponseRecived(RPiResponse response) {
                if (response.getStatus()) {
                    System.out.println("[Info] Response " + response.getCommand() + ": " + response.getResult());
                } else {
                    System.out.println("[Error] Response Error Message: " + response.getMessage());
                }
            }
        });

        System.out.print("Enter the [address:port]: ");
        String[] address = scanner.nextLine().split(":");

        if (controller.connect(address[0], Integer.parseInt(address[1]))) {
            System.out.println("Success to connect.");

            inputLoop:
            while (true) {
                int value = scanner.nextInt();

                switch (value) {
                    case 1:
                        controller.request("power", true);
                        break;
                    case 2:
                        controller.request("ssid", "hello world");
                        break;
                    case 0:
                        break inputLoop;
                    default:
                        System.out.println("Wrong number. Try again.");
                        break;
                }

            }

            controller.close();
            System.out.println("The end");

        } else {
            System.out.println("Fail to connect.");
        }
    }
}
