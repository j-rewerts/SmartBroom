package ca.ualberta.smartbroom;

import android.app.Fragment;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import ca.ualberta.smartbroom.conn.BLEDeviceConnection;

/**
 * Created by Jared on 2015-02-01.
 */
public class DataManager {

    private static DataManager instance;
    private double currentFreq;
    // pressure quadrants
    private double pressure1;
    private double pressure2;
    private double pressure3;
    private double pressure4;

    private double avgPressure;

    private List<Fragment> subscriptions;

    public static DataManager getInstance() {
        if (instance == null) instance = new DataManager();
        return instance;
    }

    private DataManager() {
        subscriptions = new ArrayList<Fragment>();
    }

    public void subscribe(Fragment frag) {
        if (frag instanceof GraphListener) {
            subscriptions.add(frag);
        }
    }

    private void notifySubs(double value, String graph) {
        for (Fragment f : subscriptions) {
            GraphListener listener = (GraphListener) f;
            listener.callback(f, value, graph);
        }
    }

    // Setters / getters

    public void setValue(String input) {
        // First, parse the first word for the function name
        // The second block should be an integer
        String[] parts = input.split(" ");

        if (parts.length < 2) {
            return;
        }

        int tempData;
        switch (parts[0]) {
            case "RS_A":
                //Accelerometer data
                tempData = Integer.parseInt(parts[1]);
                currentFreq = tempData / 10.0f;
                notifySubs(currentFreq, GraphListener.frequencyGraph);
                break;

            case "RS_P_1":
                // Pressure circuit 1
                tempData = Integer.parseInt(parts[1]);
                pressure1 = tempData;
                notifySubs(pressure1, GraphListener.pressureGraph);
                break;

            case "RS_P_2":
                // Pressure circuit 2
                tempData = Integer.parseInt(parts[1]);
                pressure2 = tempData;
                notifySubs(pressure2, GraphListener.pressureGraph);
                break;

            case "RS_P_3":
                // Pressure circuit 3
                tempData = Integer.parseInt(parts[1]);
                pressure3 = tempData;
                notifySubs(pressure3, GraphListener.pressureGraph);
                break;

            case "RS_P_4":
                // Pressure circuit 4
                tempData = Integer.parseInt(parts[1]);
                pressure4 = tempData;
                notifySubs(pressure4, GraphListener.pressureGraph);
                break;

            case "RS_P":
                // Average Pressure
                tempData = Integer.parseInt(parts[1]);
                avgPressure = tempData;
                notifySubs(avgPressure, GraphListener.pressureGraph);
                break;
        }
    }

    public void setPressure1(double p1) {
        this.pressure1 = p1;
        notifySubs(pressure1, GraphListener.pressureGraph);
    }

    public void setPressure2(double p2) {
        this.pressure2 = p2;
        notifySubs(pressure2, GraphListener.pressureGraph);
    }

    public void setPressure3(double p3) {
        this.pressure3 = p3;
        notifySubs(pressure3, GraphListener.pressureGraph);
    }

    public void setPressure4(double p4) {
        this.pressure4 = p4;
        notifySubs(pressure4, GraphListener.pressureGraph);
    }

    public double getPressure() {
        return (pressure1 + pressure2 + pressure3 + pressure4) / 4;
    }

    public void setFrequency(double frequency) {
        this.currentFreq = frequency;
        notifySubs(currentFreq, GraphListener.frequencyGraph);
    }

    public double getFrequency() {
        return this.currentFreq;
    }
}
