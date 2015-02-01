package ca.ualberta.smartbroom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Jared on 2015-01-31.
 */
public class MyGamesListAdapter extends ArrayAdapter<GameDataModel> {

    private static final String weightAppend = " kg";
    private static final String frequencyAppend = " sweeps per second";

    public MyGamesListAdapter(Context context, int resource, List<GameDataModel> items) {
        super(context, resource, items);
    }

    public View getView (int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.my_game_list_item, null);
        }

        TextView gameDate = (TextView) v.findViewById(R.id.date);
        TextView gameDayOfTheWeek = (TextView) v.findViewById(R.id.dayOfTheWeek);
        TextView peakWeightApplied = (TextView) v.findViewById(R.id.weightApplied);
        TextView peakSweepFrequency = (TextView) v.findViewById(R.id.sweepFrequency);

        gameDate.setText(new SimpleDateFormat("MM dd, yyyy").format(getItem(position).getDate()));
        gameDayOfTheWeek.setText(new SimpleDateFormat("EE").format(getItem(position).getDate()));
        peakWeightApplied.setText(String.valueOf(getItem(position).getPeakWeight()) + weightAppend);
        peakSweepFrequency.setText(String.valueOf(getItem(position).getPeakFrequency()) + frequencyAppend);

        return v;
    }
}

