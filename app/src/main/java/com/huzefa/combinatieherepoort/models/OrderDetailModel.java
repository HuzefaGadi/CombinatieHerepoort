package com.huzefa.combinatieherepoort.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Huzefa Gadi on 6/15/2017.
 */

public class OrderDetailModel {

    public String id;

    @SerializedName("partijnummer")
    public String lotNumber;

    public String weight;

    public int status;

    public String bonnummer;

    @SerializedName("herkomst")
    public String drainageLocation;

    public String afvalstroomnummer;

    @SerializedName("materiaaltype")
    public String materialType;

    @SerializedName("aantal_verpakkingen")
    public String noOfPackages;

    @SerializedName("euralcode")
    public String euralCode;

    public SenderModel afvoerlocatie;
    public SenderModel verwerkingslocatie;
    public SenderModel afzender;
    public SenderModel ontdoener;
    public SenderModel bemiddelaar;
    public SenderModel vervoerder;

}
