package ca.ualberta.smartbroom.conn;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.bluecreation.melodysmart.BatteryService;
import com.bluecreation.melodysmart.DataService;
import com.bluecreation.melodysmart.DeviceInfoService;
import com.bluecreation.melodysmart.MelodySmartDevice;
import com.bluecreation.melodysmart.MelodySmartListener;

import ca.ualberta.smartbroom.BLEDevice;
import ca.ualberta.smartbroom.DataManager;

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

    private DataService.Listener dataServiceListener = new DataService.Listener() {

        @Override
        public void onConnected(final boolean found) {
            if (found) {
                device.getDataService().enableNotifications(true);
            }
        }

        @Override
        public void onReceived(final String data) {
            if (data == null) {
                return;
            }

            Log.d(TAG, "Data received: " + data);

            DataManager dataManager = DataManager.getInstance();
            dataManager.setValue(data);

            if (data.startsWith("RS_ACK")) {
                Log.d(TAG, "Acknowledged by device.");
            } else if (data.startsWith("RS_ECHO")) {
                Log.d(TAG, "Device echoed " + data.substring("RS_ECHO ".length()));
            } else if (data.startsWith("RS_A")) {
                Log.d(TAG, "Acceleration: " + data.substring("RS_A ".length()));
            } else if (data.startsWith("RS_P_1")) {
                Log.d(TAG, "Pressure 1: " + data.substring("RS_P_1 ".length()));
            } else if (data.startsWith("RS_P")) {
                Log.d(TAG, "Pressure Average: " + data.substring("RS_P ".length()));
            }else if (data.startsWith("RS_P_2")) {
                Log.d(TAG, "Pressure 2: " + data.substring("RS_P_2 ".length()));
            } else if (data.startsWith("RS_P_3")) {
                Log.d(TAG, "Pressure 3: " + data.substring("RS_P_3 ".length()));
            } else if (data.startsWith("RS_P_4")) {
                Log.d(TAG, "Pressure 4: " + data.substring("RS_P_4 ".length()));
            }
        }
    };

    public void cmdEcho(String data) {
        device.getDataService().send("RQ_ECHO " + data);
    }

    public boolean cmdPressure() {
        if (!device.getDataService().available()) {
            return false;
        }

        /*device.getDataService().send("RQ_P_1");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //device.getDataService().send("RQ_P_2");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        device.getDataService().send("RQ_P_3");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //device.getDataService().send("RQ_P_4");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

        device.getDataService().send("RQ_P");
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return true;
    }

    public boolean cmdAccel() {
        if (!device.getDataService().available()) {
            return false;
        }
        device.getDataService().send("RQ_A");
        return true;
    }

    public boolean cmdAck() {
        if (!device.getDataService().available()) {
            return false;
        }
        device.getDataService().send("RQ_ACK");
        return true;
    }

    @Override
    public void onDeviceConnected() {
        connected = true;
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            public void run() {
                Toast.makeText(context, "Connected to device", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDeviceDisconnected() {
        connected = false;
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            public void run() {
                Toast.makeText(context, "Disconnected from device", Toast.LENGTH_SHORT).show();
            }
        });
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
