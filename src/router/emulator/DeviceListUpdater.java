package router.emulator;

import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Kyle John on 5/10/2017.
 */
public class DeviceListUpdater {
    private ObservableList<Device> devices;
    private static ArrayList<Device> users;
    private Random random = new Random();

    static {
        users = new ArrayList<>(8);
        users.add(new Device("Wilson", "192.168.137.168", "58:27:e4:ec:c7:d1"));
        users.add(new Device("Jones", "192.168.137.27", "71:91:5a:84:77:b8"));
        users.add(new Device("Thompson", "192.168.137.104", "8c:68:b9:41:24:d3"));
        users.add(new Device("Phillip", "192.168.137.150", "0d:01:70:54:25:da"));
        users.add(new Device("Alice", "192.168.137.41", "14:eb:03:43:83:90"));
        users.add(new Device("Louise", "192.168.137.30", "f0:e6:bf:13:ad:8e"));
        users.add(new Device("Alexander", "192.168.137.105", "a6:29:d1:5f:c6:60"));
        users.add(new Device("Michael", "192.168.137.73", "14:eb:03:43:83:90"));
    }

    public DeviceListUpdater(ObservableList<Device> devices) {
        this.devices = devices;
    }

    public void updateList() {
        int size = devices.size();

        if (size == 0){
            devices.add(users.get(random.nextInt(8)));
        } else if (size == 8) {
            devices.remove(random.nextInt(size));
        } else {
            int percent = random.nextInt(10);
            if(percent > 3){
                devices.add(users.get(random.nextInt(8)));
            } else {
                devices.remove(random.nextInt(size));
            }
        }
    }
}
