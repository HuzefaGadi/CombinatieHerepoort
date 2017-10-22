package com.huzefa.combinatieherepoort.retrofit;

import com.google.gson.JsonObject;
import com.huzefa.combinatieherepoort.models.CredentialsModel;
import com.huzefa.combinatieherepoort.models.LoginModel;
import com.huzefa.combinatieherepoort.models.OrderDetailModel;
import com.huzefa.combinatieherepoort.models.OrderDetailModelList;
import com.huzefa.combinatieherepoort.models.OrderModelList;
import com.huzefa.combinatieherepoort.models.OutsourcedVehiclesModel;
import com.huzefa.combinatieherepoort.models.VehiclesModel;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by Rashida on 28/05/17.
 */

public interface RestApi {
    @POST("login")
    Observable<LoginModel> loginUser(@Body CredentialsModel body);

    @POST("selectkenteken")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Observable<VehiclesModel> getVehicles(@Body JsonObject body);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("storekenteken")
    Observable<JsonObject> saveVehicle(@Body JsonObject body);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("getorders")
    Observable<OrderModelList> getOrders(@Body JsonObject body);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("order")
    Observable<OrderDetailModel> getOrder(@Body JsonObject body);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("logout")
    Observable<JsonObject> logout(@Body JsonObject body);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("order/confirm")
    Observable<JsonObject> confirm(@Body JsonObject body);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("order/setweight")
    Observable<JsonObject> setweight(@Body JsonObject body);

    @POST("selectuitbesteedvervoerder")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Observable<OutsourcedVehiclesModel> getOutSourcedCarriers(@Body JsonObject body);

    @POST("createtransaction")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Observable<JsonObject> createTransaction(@Body JsonObject body);

    @POST("order/finish")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Observable<JsonObject> finish(@Body JsonObject body);

    @POST("order/destroy")
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    Observable<JsonObject> destroy(@Body JsonObject body);




}
