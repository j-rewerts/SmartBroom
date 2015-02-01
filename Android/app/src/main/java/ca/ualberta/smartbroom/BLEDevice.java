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
        return (mDeviceName == null) ? "Unknown" : mDeviceName;
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

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof BLEDevice)) return false;
        BLEDevice dev = (BLEDevice) other;
        if (getDeviceName().equals(dev.getDeviceName()) && getDeviceAddress().equals(dev.getDeviceAddress()))
            return true;
        else
            return false;
    }

    @Override
    public String toString() {
        return getDeviceName() + " (" + getDeviceAddress() + ")";
    }
}
