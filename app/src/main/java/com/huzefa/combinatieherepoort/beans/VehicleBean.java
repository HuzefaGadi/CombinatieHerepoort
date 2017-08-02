package com.huzefa.combinatieherepoort.beans;

/**
 * Created by Rashida on 10/06/17.
 */

public class VehicleBean {
    public String id;
    public String name;

    public VehicleBean(String id, String name) {
        this.id = id;
        this.name = name;
    }

    //to display object as a string in spinner
    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof VehicleBean) {
            VehicleBean c = (VehicleBean) obj;
            if (c.name.equals(name) && c.id == id) return true;
        }

        return false;
    }

}

