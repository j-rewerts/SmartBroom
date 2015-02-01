package ca.ualberta.smartbroom.conn;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.bluecreation.melodysmart.BatteryService;
import com.bluecreation.melodysmart.DataService;
import com.bluecreation.melodysmart.DeviceInfoService;
import com.bluecreation.melodysmart.MelodySmartDevice;
import com.bluecreation.melodysmart.MelodySmartListener;

import ca.ualberta.smartbroom.BLEDevice;

/**
 * Created by Stephen on 2015-01-31.
 */
public class BLEDeviceConnection implements MelodySmartListener {

    private static final String TAG = "BLEDeviceConnection";

    private MelodySmartDevice device;
    private int nextInfoType = 0;
    private Context context;
    private boolean connected = false;

    private static BLEDeviceConnection instance;

    public static BLEDeviceConnection getInstance() {
        if (instance == null) instance = new BLEDeviceConnection();
        return instance;
    }

    private BLEDeviceConnection() {
        /* Get the instance of the Melody Smart Android library and initialize it */
        device = MelodySmartDevice.getInstance();
        device.registerListener(this);
        device.getDataService().registerListener(dataServiceListener);
        device.getBatteryService().registerListener(batteryServiceListener);
        device.getDeviceInfoService().registerListener(deviceInfoListener);
    }

    public void connect(BLEDevice bleDevice, Context context) {
        this.context = context;
        try {
            device.connect(bleDevice.getDeviceAddress());
        } catch(Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public void disconnect() {
        if (!connected) return;
        device.disconnect();
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

    public void cmdEcho(String data) {
        device.getDataService().send("RQ_ECHO " + data);
    }

    public boolean cmdAck(DataService.Listener listener) {
        if (!device.getDataService().available()) {
            return false;
        }

        device.getDataService().registerListener(listener);
        device.getDataService().send("RQ_ACK");
        return true;
    }

    public boolean cmdAckUntilSuccess() {
        class BooleanHolder {
            public boolean value = false;
        }
        final BooleanHolder acknowledged = new BooleanHolder();
        final long start = System.currentTimeMillis();
        final long timeout = 2500;

        DataService.Listener listener = new DataService.Listener() {
            @Override
            public void onReceived(String s) {
                if (s.equals("RS_ACK")) {
                    Log.d(TAG, "Time until acknowledge: " + (System.currentTimeMillis() - start) + "ms");
                    acknowledged.value = true;
                }
            }

            @Override
            public void onConnected(boolean b) {

            }
        };

        while (!acknowledged.value || System.currentTimeMillis() - start > timeout) {
            device.getDataService().registerListener(listener);
            device.getDataService().send("RQ_ACK");
            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                device.getDataService().unregisterListener(listener);
            }
        }
        return acknowledged.value;
    }

    @Override
    public void onDeviceConnected() {
        connected = true;
        if (context != null)
            Toast.makeText(context, "Connected to device", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeviceDisconnected() {
        connected = false;
        if (context != null)
            Toast.makeText(context, "Disconnected from device", Toast.LENGTH_SHORT).show();
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

    public interface DataListener {
        public void onReceived(String data);
    }
}
