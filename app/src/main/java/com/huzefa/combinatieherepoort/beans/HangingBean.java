package com.huzefa.combinatieherepoort.beans;

/**
 * Created by Rashida on 19/10/17.
 */

public class HangingBean {

    public String id;
    public String kenteken;
    public String leeggewicht;

    public HangingBean(String id, String kenteken, String leeggewicht) {
        this.id = id;
        this.kenteken = kenteken;
        this.leeggewicht = leeggewicht;
    }

    //to display object as a string in spinner
    @Override
    public String toString() {
        return kenteken;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof VehicleBean) {
            VehicleBean c = (VehicleBean) obj;
            if (c.kenteken.equals(kenteken) && c.id == id && c.leeggewicht.equals(leeggewicht)) return true;
        }

        return false;
    }
}
