package ca.ualberta.smartbroom;

/**
 * Class to hold description of a bluetooth device
 *
 * Created by Stephen on 2015-01-31.
 */
public class BLEDevice {
    private String mDeviceName;
    private String mDeviceAddress;

    public BLEDevice(String deviceName, String deviceAddress) {
        setDeviceName(deviceName);
        setDeviceAddress(deviceAddress);
    }

    public String getDeviceName() {
        return mDeviceName;
    }

    public void setDeviceName(String mDeviceName) {
        this.mDeviceName = mDeviceName;
    }

    public String getDeviceAddress() {
        return mDeviceAddress;
    }

    public void setDeviceAddress(String mDeviceAddress) {
        this.mDeviceAddress = mDeviceAddress;
    }
}
