package com.huzefa.combinatieherepoort.retrofit;

import com.google.gson.JsonObject;
import com.huzefa.combinatieherepoort.models.CredentialsModel;
import com.huzefa.combinatieherepoort.models.LoginModel;
import com.huzefa.combinatieherepoort.models.OrderModel;
import com.huzefa.combinatieherepoort.models.OrderModelList;
import com.huzefa.combinatieherepoort.models.VehiclesModel;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Rashida on 28/05/17.
 */

public interface RestApi {
    @POST("login")
    Observable<LoginModel> loginUser(@Body CredentialsModel body);

    @POST("selectkenteken")
    Observable<VehiclesModel> getVehicles(@Header("Content-Type") String contentType, @Body JsonObject body);

    @POST("storekenteken")
    Observable<JsonObject> saveVehicle(@Header("Content-Type") String contentType, @Body JsonObject body);

    @POST("orders")
    Observable<OrderModelList> getOrders(@Header("Content-Type") String contentType, @Body JsonObject body);

    @POST("order")
    Observable<OrderModel> getOrder(@Header("Content-Type") String contentType, @Body JsonObject body);

    @POST("logout")
    Observable<JsonObject> logout(@Header("Content-Type") String contentType, @Body JsonObject body);

    @POST("order/confirm")
    Observable<JsonObject> confirm (@Header("Content-Type") String contentType, @Body JsonObject body);


}
