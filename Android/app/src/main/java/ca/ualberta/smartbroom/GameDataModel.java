package ca.ualberta.smartbroom;

import java.util.Date;

/**
 * Created by Jared on 2015-01-31.
 */
public class GameDataModel {

    private Date date;
    private String notes;

    GameDataModel() {
        this.date = new Date();
    }

    public String getNotes() {
        return this.notes;
    }

    public void setNotes(String note) {
        this.notes = note;
    }
}
