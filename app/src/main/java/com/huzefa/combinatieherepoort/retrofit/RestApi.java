package com.huzefa.combinatieherepoort.retrofit;

import com.google.gson.JsonObject;
import com.huzefa.combinatieherepoort.models.CredentialsModel;
import com.huzefa.combinatieherepoort.models.LoginModel;
import com.huzefa.combinatieherepoort.models.VehiclesModel;


import io.reactivex.Observable;
import retrofit2.Call;
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

    @GET("selectkenteken")
    Observable<VehiclesModel> getVehicles(@Query("api_token") String apiToken);

    @POST("storekenteken")
    Observable<JsonObject> saveVehicle(@Header("Content-Type") String contentType, @Body JsonObject body);


}
