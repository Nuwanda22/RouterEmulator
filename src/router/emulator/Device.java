package router.emulator;

import javafx.beans.property.SimpleStringProperty;

public class Device {
    private SimpleStringProperty deviceName;
    private SimpleStringProperty ipAddress;
    private SimpleStringProperty macAddress;

    public Device(String deviceName, String ipAddress, String macAddress){
        this.deviceName = new SimpleStringProperty(deviceName);
        this.ipAddress = new SimpleStringProperty(ipAddress);
        this.macAddress = new SimpleStringProperty(macAddress);
    }

    public String getDeviceName() {
        return deviceName.get();
    }
    public void setDeviceName(String deviceName) {
        this.deviceName.set(deviceName);
    }

    public String getIpAddress() {
        return ipAddress.get();
    }
    public void setIpAddress(String ipAddress) {
        this.ipAddress.set(ipAddress);
    }

    public String getMacAddress() {
        return macAddress.get();
    }
    public void setMacAddress(String macAddress) {
        this.macAddress.set(macAddress);
    }
}
