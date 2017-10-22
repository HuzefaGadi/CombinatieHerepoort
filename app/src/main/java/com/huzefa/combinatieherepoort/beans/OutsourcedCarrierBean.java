package com.huzefa.combinatieherepoort.beans;

/**
 * Created by Rashida on 19/10/17.
 */

public class OutsourcedCarrierBean {

    public String id;
    public String name;

    public OutsourcedCarrierBean(String id, String name) {
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
        if (obj instanceof OutsourcedCarrierBean) {
            OutsourcedCarrierBean c = (OutsourcedCarrierBean) obj;
            if (c.name.equals(name) && c.id == id) return true;
        }

        return false;
    }
}
