package router.emulator;

import javafx.collections.ObservableList;
import router.connector.Manager;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Kyle John on 5/10/2017.
 */
public class DeviceListUpdater {
    private ObservableList<Device> devices;
    private Random random = new Random();
    private Manager manager = new Manager();

    private String[] addresses = new String[] {
            "192.168.137.168",
            "192.168.137.27",
            "192.168.137.104",
            "192.168.137.150",
            "192.168.137.41",
            "192.168.137.30",
            "192.168.137.105",
            "192.168.137.73"
    };

    public DeviceListUpdater(ObservableList<Device> devices) {
        this.devices = devices;
    }

    public void updateList() {
        int size = devices.size();

        if (size == 0){
            devices.add(new Device(manager.getHostName(), addresses[random.nextInt(8)], manager.getMACAddress()));
        } else if (size == 8) {
            devices.remove(random.nextInt(size));
        } else {
            int percent = random.nextInt(10);
            if(percent > 3){
                devices.add(new Device(manager.getHostName(), addresses[random.nextInt(8)], manager.getMACAddress()));
            } else {
                devices.remove(random.nextInt(size));
            }
        }
    }
}
