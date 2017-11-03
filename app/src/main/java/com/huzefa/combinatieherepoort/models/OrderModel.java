package com.huzefa.combinatieherepoort.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Rashida on 19/10/17.
 */

public class OrderModel {

    public static final String NULL_VALUE = "onbekend";

    @SerializedName("partijnummer")
    public String lotNumber;

    @SerializedName("materiaaltype")
    public String materialType;

    public String cluster;

    @SerializedName("type_transactie")
    public String transactionType;

    public void avoidNullPointer() {
        if(lotNumber == null) {
            lotNumber = NULL_VALUE;
        }
        if(materialType == null) {
            materialType = NULL_VALUE;
        }

        if(cluster == null) {
            cluster = NULL_VALUE;
        }

        if (transactionType == null) {
            transactionType = NULL_VALUE;
        }
    }


}
