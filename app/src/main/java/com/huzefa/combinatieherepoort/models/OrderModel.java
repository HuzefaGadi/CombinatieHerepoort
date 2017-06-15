package com.huzefa.combinatieherepoort.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Huzefa Gadi on 6/15/2017.
 */

public class OrderModel {

    @SerializedName("partijnummer")
    public String lotNumber;

    @SerializedName("afvoerlocatie")
    public String drainageLocation;
}
