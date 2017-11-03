package com.huzefa.combinatieherepoort.models;

import com.google.gson.annotations.SerializedName;
import com.huzefa.combinatieherepoort.beans.HangingBean;
import com.huzefa.combinatieherepoort.beans.VehicleBean;

import java.util.List;

/**
 * Created by Rashida on 08/06/17.
 */

public class VehiclesModel {
    @SerializedName("voertuig")
    public List<VehicleBean> vehicleNumber;

    @SerializedName("aanhangwagen")
    public List<HangingBean> hangingNumber;
}
