package ca.ualberta.smartbroom;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jared on 2015-01-31.
 */
public class BLEDeviceDialog extends DialogFragment {

    List<BLEDevice> deviceList;
    BLEDeviceListAdapter adapter;
    ListView listView;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        deviceList = new ArrayList<BLEDevice>();
        adapter = new BLEDeviceListAdapter(getActivity(), R.layout.ble_device_list_item, deviceList);
        listView = new ListView(getActivity());

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setView(listView)
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        BLEDeviceDialog.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }

}
