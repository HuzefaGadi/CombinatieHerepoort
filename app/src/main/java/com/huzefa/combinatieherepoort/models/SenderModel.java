package com.huzefa.combinatieherepoort.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Rashida on 01/08/17.
 */

public class SenderModel {

    @SerializedName("naam")
    public String name;
    @SerializedName("adres")
    public String address;
    public String postcode;
    @SerializedName("woonplaats")
    public String city;
}
