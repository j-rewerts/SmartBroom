package ca.ualberta.smartbroom;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Jared on 2015-01-31.
 */
public class BLEDeviceDialog extends AlertDialog {

    BLEDeviceListAdapter adapter;

    public BLEDeviceDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        adapter = new BLEDeviceListAdapter(getContext(), R.layout.ble_device_list_item, new ArrayList<BLEDevice>());
        LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.ble_device_list, null);

        //this.setView(convertView);

        //ListView lv = (ListView) convertView.findViewById(R.id.deviceListView);
        //lv.setAdapter(adapter);

        super.onCreate(savedInstanceState);
    }


}
