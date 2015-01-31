package ca.ualberta.smartbroom.conn;

import android.util.Log;

import com.bluecreation.melodysmart.BatteryService;
import com.bluecreation.melodysmart.DataService;
import com.bluecreation.melodysmart.DeviceInfoService;
import com.bluecreation.melodysmart.MelodySmartDevice;
import com.bluecreation.melodysmart.MelodySmartListener;

/**
 * Created by Stephen on 2015-01-31.
 */
public class BLEDeviceConnection implements MelodySmartListener {

    private static final String TAG = "BLEDeviceConnection";

    private MelodySmartDevice device;
    private int nextInfoType = 0;

    public BLEDeviceConnection() {
        /* Get the instance of the Melody Smart Android library and initialize it */
        device = MelodySmartDevice.getInstance();
        device.registerListener(this);
        device.getDataService().registerListener(dataServiceListener);
        device.getBatteryService().registerListener(batteryServiceListener);
        device.getDeviceInfoService().registerListener(deviceInfoListener);
    }

    private BatteryService.Listener batteryServiceListener = new BatteryService.Listener() {

        @Override
        public void onBatteryLevelChanged(final int level) {
            Log.d(TAG, "Got battery level " + level);
        }

        @Override
        public void onConnected(boolean found) {
            Log.d(TAG, "Connected to battery level service");
            device.getBatteryService().enableNotifications(true);
        }
    };

    private DataService.Listener dataServiceListener = new DataService.Listener() {

        @Override
        public void onConnected(final boolean found) {
            if (found) {
                device.getDataService().enableNotifications(true);
            }
        }

        @Override
        public void onReceived(final String data) {
            Log.d(TAG, "Data received: " + data);
        }
    };


    private DeviceInfoService.Listener deviceInfoListener = new DeviceInfoService.Listener() {
        @Override
        public void onInfoRead(final DeviceInfoService.INFO_TYPE type, final String value) {
            switch(type) {
                case MANUFACTURER_NAME:
                    Log.d(TAG, "Manufacturer name: " + value);
                    break;
                case MODEL_NUMBER:
                    Log.d(TAG, "Model number: " + value);
                    break;
                case SERIAL_NUMBER:
                    Log.d(TAG, "Serial number: " + value);
                    break;
                case HARDWARE_REV:
                    Log.d(TAG, "HW rev: " + value);
                    break;
                case FIRMWARE_REV:
                    Log.d(TAG, "FW rev " + value);
                    break;
                case SOFTWARE_REV:
                    Log.d(TAG, "SW rev: " + value);
                    break;
                case SYSTEM_ID:
                    Log.d(TAG, "System id: " + value);
                    break;
                case PNP_ID:
                    Log.d(TAG, "PNP id: " + value);
                    break;
            }
            readNextInfo();
        }

        @Override
        public void onConnected(boolean found) {
            /* Start reading all the characteristics */
            readNextInfo();
        }
    };

    @Override
    public void onDeviceConnected() {

    }

    @Override
    public void onDeviceDisconnected() {

    }

    @Override
    public void onOtauAvailable() {

    }

    private void readNextInfo() {
        DeviceInfoService.INFO_TYPE[] types = DeviceInfoService.INFO_TYPE.values();
        if (nextInfoType < types.length) {
            device.getDeviceInfoService().read(types[nextInfoType]);
            nextInfoType++;
        }
    }
}
