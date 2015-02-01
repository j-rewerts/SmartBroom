package ca.ualberta.smartbroom.conn;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.util.Log;

import com.bluecreation.melodysmart.MelodySmartDevice;

/**
 * Created by Stephen on 2015-01-31.
 */
public class BLEConnector {
    private MelodySmartDevice device;
    private boolean scanning;

    private static final String TAG = "BLEConnector";

    /**
     *
     * @param context Application context
     */
    public BLEConnector(Context context) {
        device = MelodySmartDevice.getInstance();
        device.init(context);

        scanLeDevice(true);
    }

    private synchronized void scanLeDevice(final boolean enable) {
        scanning = enable;
        if (enable) {
            device.startLeScan(mLeScanCallback);
        } else {
            device.stopLeScan(mLeScanCallback);
        }
    }

    // Device scan callback.
    private final BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
            Log.d(TAG, "Found device " + device.getAddress());
        }
    };
}
