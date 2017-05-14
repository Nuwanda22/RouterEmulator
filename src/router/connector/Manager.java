package router.connector;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Created by Nuwanda on 2017-05-02.
 */
public class Manager {
    private final Random rand = new Random();
    private final String lexicon = "ABCDEFGHIJKLMNOPQRSTUVWXYZ12345674890";
    private final Set<String> identifiers = new HashSet<String>();

    public String getMACAddress(){
        byte[] macBytes = new byte[6];
        rand.nextBytes(macBytes);
        macBytes[0] = (byte)(macBytes[0] & (byte)254);

        StringBuilder macAddress = new StringBuilder(18);
        for (byte b : macBytes) {
            if(macAddress.length() > 0) macAddress.append(":");
            macAddress.append(String.format("%02x", b));
        }
        return macAddress.toString();
    }

    public String getHostName() {
        StringBuilder deviceName = new StringBuilder();

        while(deviceName.toString().length() == 0) {
            for(int i = 0; i < 7; i++) {
                deviceName.append(lexicon.charAt(rand.nextInt(lexicon.length())));
            }
            if(identifiers.contains(deviceName.toString())) {
                deviceName = new StringBuilder();
            }
        }

        return "Desktop-" + deviceName.toString();
    }
}
