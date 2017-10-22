package com.huzefa.combinatieherepoort.models;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

/**
 * Created by Rashida on 08/06/17.
 */

public class VehiclesModel {
    @SerializedName("kenteken")
    public Map<String,String> vehicleNumber;

    @SerializedName("aanhangwagen")
    public Map<String,String> hangingNumber;
}
