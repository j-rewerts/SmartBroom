package ca.ualberta.smartbroom;

import java.util.Date;

/**
 * Created by Jared on 2015-01-31.
 */
public class GameDataModel {

    private Date date;
    private String notes;
    private double peakWeight;
    private double peakFrequency;

    GameDataModel() {
        this.date = new Date();
    }

    public void calculatePeakWeight() {

    }

    public void calculatePeakFrequency() {

    }

    // Getters and Setters

    public String getNotes() {
        return this.notes;
    }

    public void setNotes(String note) {
        this.notes = note;
    }

    public Date getDate() {
        return this.date;
    }

    public double getPeakWeight() {
        return this.peakWeight;
    }

    public double getPeakFrequency() {
        return this.peakFrequency;
    }

}
