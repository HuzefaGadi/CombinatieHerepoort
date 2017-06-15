package com.huzefa.combinatieherepoort.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.huzefa.combinatieherepoort.AppManager;
import com.huzefa.combinatieherepoort.Constants;
import com.huzefa.combinatieherepoort.R;
import com.huzefa.combinatieherepoort.adapters.CustomSpinnerAdapter;
import com.huzefa.combinatieherepoort.beans.VehicleBean;
import com.huzefa.combinatieherepoort.models.LoginModel;
import com.huzefa.combinatieherepoort.models.VehiclesModel;
import com.huzefa.combinatieherepoort.retrofit.RestApi;
import com.huzefa.combinatieherepoort.utility.Utility;

import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

public class SelectVehicleActivity extends AppCompatActivity {

    @BindView(R.id.vehicleNumberAutoTextView)
    AutoCompleteTextView mVehicleNumberAutoTextView;

    @BindView(R.id.saveVehicleButton)
    Button mSaveVehicleButton;

    private static String TAG;
    private RestApi mRestApi;
    SharedPreferences mSharedPreferences;
    private VehiclesModel mVehicleModel;
    private LoginModel mLoginModel;
    private ProgressDialog mProgressDialog;
    private Typeface mTypeFace;

    ArrayList<VehicleBean> mVehicleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_vehicle);
        TAG = this.getClass().getSimpleName();
        ButterKnife.bind(this);
        mVehicleList = new ArrayList<VehicleBean>();
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("Please wait..");
        mProgressDialog.setMessage("Loading vehicles");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        mVehicleNumberAutoTextView.setThreshold(1);
        Retrofit retrofit = ((AppManager) getApplicationContext()).getRetrofit();

        mRestApi = retrofit.create(RestApi.class);
        mSharedPreferences = Utility.getSharedPrefernce(this);
        mTypeFace = Utility.getTypeFace(this);
        mSaveVehicleButton.setTypeface(mTypeFace);
        mLoginModel = new Gson().fromJson(mSharedPreferences.getString(Constants.PREF_USER, null), LoginModel.class);
        mRestApi.getVehicles(mLoginModel.getToken())
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<VehiclesModel>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull VehiclesModel vehiclesModel) {
                        mVehicleModel = vehiclesModel;
                        for (Map.Entry<String, String> entry : vehiclesModel.vehicleNumber.entrySet()) {
                            mVehicleList.add(new VehicleBean(entry.getKey(), entry.getValue()));
                        }
                        CustomSpinnerAdapter<VehicleBean> adapter = new CustomSpinnerAdapter<VehicleBean>(
                                getApplicationContext(), R.layout.custom_auto_complete_dropdown, mVehicleList);

                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        mVehicleNumberAutoTextView.setAdapter(adapter);
                        mProgressDialog.dismiss();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Toast.makeText(getApplicationContext(), "Some error happened " + e.getMessage(), Toast.LENGTH_LONG).show();
                        mProgressDialog.dismiss();
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    @OnClick(R.id.saveVehicleButton)
    public void onClick(View v) {
        if (mVehicleModel != null) {
            mProgressDialog.setMessage("Saving Vehicle");
            mProgressDialog.show();
            String vehicleName = mVehicleNumberAutoTextView.getText().toString();
            VehicleBean vehicleTemp = null;
            for (VehicleBean vehicleBean : mVehicleList) {
                if (vehicleBean.toString().equalsIgnoreCase(vehicleName)) {
                    vehicleTemp = vehicleBean;
                    break;
                }
            }
            final VehicleBean vehicle = vehicleTemp;
            if (vehicle != null) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("api_token", mLoginModel.getToken());
                jsonObject.addProperty("selectkenteken", Integer.parseInt(vehicle.id));
                mRestApi.saveVehicle("application/json", jsonObject)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<JsonObject>() {
                            @Override
                            public void onSubscribe(@NonNull Disposable d) {

                            }

                            @Override
                            public void onNext(@NonNull JsonObject s) {
                                mProgressDialog.dismiss();
                                if (s != null && s.has("status") && s.get("status").getAsString().equalsIgnoreCase("success")) {
                                    mSharedPreferences.edit().putString(Constants.PREF_VEHICLE, vehicle.id).commit();
                                    startActivity(new Intent(SelectVehicleActivity.this, MainActivity.class));
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Error occured " + s, Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                mProgressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Error occured " + e.getMessage(), Toast.LENGTH_LONG).show();
                            }

                            @Override
                            public void onComplete() {

                            }
                        });
            } else {
                mProgressDialog.dismiss();
                Toast.makeText(this,"Vehicle not found please try some other name",Toast.LENGTH_LONG).show();
            }

        }
    }
}
