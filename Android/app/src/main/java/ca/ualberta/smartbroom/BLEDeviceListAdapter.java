package ca.ualberta.smartbroom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Stephen on 2015-01-31.
 */
public class BLEDeviceListAdapter extends ArrayAdapter<BLEDevice> {
    public BLEDeviceListAdapter(Context context, int resource, List<BLEDevice> items) {
        super(context, resource, items);
    }

    public View getView (int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.ble_device_list_item, null);
        }

        TextView deviceName = (TextView) v.findViewById(R.id.tDeviceName);
        TextView deviceAddress = (TextView) v.findViewById(R.id.tDeviceAddress);

        deviceName.setText(getItem(position).getDeviceName());
        deviceAddress.setText(getItem(position).getDeviceAddress());

        return v;
    }
}
