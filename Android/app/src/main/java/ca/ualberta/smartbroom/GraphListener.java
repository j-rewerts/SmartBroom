package ca.ualberta.smartbroom;

import android.app.Fragment;
import android.view.View;

/**
 * Created by Jared on 2015-02-01.
 */
public interface GraphListener {
    public static final String frequencyGraph = "FREQUENCY";
    public static final String pressureGraph = "PRESSURE";

    public void callback(Fragment f, double value, String graph);
}
