package com.huzefa.combinatieherepoort.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.huzefa.combinatieherepoort.AppManager;
import com.huzefa.combinatieherepoort.Constants;
import com.huzefa.combinatieherepoort.R;
import com.huzefa.combinatieherepoort.adapters.CustomSpinnerAdapter;
import com.huzefa.combinatieherepoort.beans.HangingBean;
import com.huzefa.combinatieherepoort.beans.OutsourcedCarrierBean;
import com.huzefa.combinatieherepoort.beans.VehicleBean;
import com.huzefa.combinatieherepoort.models.LoginModel;
import com.huzefa.combinatieherepoort.models.OutsourcedVehiclesModel;
import com.huzefa.combinatieherepoort.models.VehiclesModel;
import com.huzefa.combinatieherepoort.retrofit.RestApi;
import com.huzefa.combinatieherepoort.utility.Utility;

import org.json.JSONObject;

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
import okhttp3.ResponseBody;
import retrofit2.HttpException;
import retrofit2.Retrofit;

import static java.security.AccessController.getContext;

public class SelectVehicleActivity extends AppCompatActivity {

    @BindView(R.id.vehicleNumberAutoTextView)
    AutoCompleteTextView mVehicleNumberAutoTextView;

    @BindView(R.id.hangingNumberAutoTextView)
    AutoCompleteTextView mHangingNumberAutoTextView;

    @BindView(R.id.outsourcedCarrierNumberAutoTextView)
    AutoCompleteTextView mOutsourcedCarrierNumberAutoTextView;


    @BindView(R.id.selectHangingHeading)
    TextView mSelectHangingHeading;
    @BindView(R.id.selectOutsourcedCarrierHeading)
    TextView mSelectOutsourcedCarrierHeading;

    @BindView(R.id.saveVehicleButton)
    Button mSaveVehicleButton;

    private static String TAG;
    private RestApi mRestApi;
    SharedPreferences mSharedPreferences;
    private VehiclesModel mVehicleModel;
    private OutsourcedVehiclesModel mOutsourcedVehicleModel;
    private LoginModel mLoginModel;
    private ProgressDialog mProgressDialog;
    private Typeface mTypeFace;

    ArrayList<VehicleBean> mVehicleList;
    ArrayList<HangingBean> mHangingList;
    ArrayList<OutsourcedCarrierBean> mOutsourcedCarrierList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_vehicle);
        TAG = this.getClass().getSimpleName();
        ButterKnife.bind(this);
        mVehicleList = new ArrayList<VehicleBean>();
        mHangingList = new ArrayList<HangingBean>();
        mOutsourcedCarrierList = new ArrayList<OutsourcedCarrierBean>();
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("Please wait..");
        mProgressDialog.setMessage("Loading vehicles");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
        mVehicleNumberAutoTextView.setThreshold(1);
        mHangingNumberAutoTextView.setThreshold(1);
        mOutsourcedCarrierNumberAutoTextView.setThreshold(1);
        Retrofit retrofit = ((AppManager) getApplicationContext()).getRetrofit();

        mRestApi = retrofit.create(RestApi.class);
        mSharedPreferences = Utility.getSharedPrefernce(this);
        mTypeFace = Utility.getTypeFace(this);
        mSaveVehicleButton.setTypeface(mTypeFace);
        mLoginModel = new Gson().fromJson(mSharedPreferences.getString(Constants.PREF_USER, null), LoginModel.class);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("api_token", mLoginModel.getToken());
        mRestApi.getVehicles(jsonObject)
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

                        if (!vehiclesModel.hangingNumber.isEmpty()) {
                            for (Map.Entry<String, String> entry : vehiclesModel.hangingNumber.entrySet()) {
                                mHangingList.add(new HangingBean(entry.getKey(), entry.getValue()));
                            }
                            CustomSpinnerAdapter<HangingBean> adapterForHanging = new CustomSpinnerAdapter<HangingBean>(
                                    getApplicationContext(), R.layout.custom_auto_complete_dropdown, mHangingList);

                            adapterForHanging.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            mHangingNumberAutoTextView.setAdapter(adapterForHanging);
                            mHangingNumberAutoTextView.setVisibility(View.VISIBLE);
                            mSelectHangingHeading.setVisibility(View.VISIBLE);

                        } else {
                            mHangingNumberAutoTextView.setVisibility(View.GONE);
                            mSelectHangingHeading.setVisibility(View.GONE);
                        }


                        mProgressDialog.dismiss();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        handleGenericError(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });

        mRestApi.getOutSourcedCarriers(jsonObject)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<OutsourcedVehiclesModel>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull OutsourcedVehiclesModel outsourcedVehiclesModel) {
                        mOutsourcedVehicleModel = outsourcedVehiclesModel;

                        for (Map.Entry<String, String> entry : outsourcedVehiclesModel.outSourceVehicleNumbers.entrySet()) {
                            mOutsourcedCarrierList.add(new OutsourcedCarrierBean(entry.getKey(), entry.getValue()));
                        }

                        if (!mOutsourcedCarrierList.isEmpty()) {
                            CustomSpinnerAdapter<OutsourcedCarrierBean> adapter = new CustomSpinnerAdapter<OutsourcedCarrierBean>(
                                    getApplicationContext(), R.layout.custom_auto_complete_dropdown, mOutsourcedCarrierList);

                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            mOutsourcedCarrierNumberAutoTextView.setAdapter(adapter);
                            mOutsourcedCarrierNumberAutoTextView.setVisibility(View.VISIBLE);
                            mSelectOutsourcedCarrierHeading.setVisibility(View.VISIBLE);
                        } else {
                            mOutsourcedCarrierNumberAutoTextView.setVisibility(View.GONE);
                            mSelectOutsourcedCarrierHeading.setVisibility(View.GONE);
                        }


                        Log.d("OutsourcedResponse", outsourcedVehiclesModel.toString());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        handleGenericError(e);
                        mOutsourcedCarrierNumberAutoTextView.setVisibility(View.GONE);
                        mSelectOutsourcedCarrierHeading.setVisibility(View.GONE);
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
                }
            }
            String hangingVehicleName = mHangingNumberAutoTextView.getText().toString();
            HangingBean hangingBeanTemp = null;
            if (!mHangingList.isEmpty()) {
                for (HangingBean hangingBean : mHangingList) {
                    if (hangingBean.toString().equalsIgnoreCase(hangingVehicleName)) {
                        hangingBeanTemp = hangingBean;
                    }
                }
            }

            String outsourcedCarrierName = mOutsourcedCarrierNumberAutoTextView.getText().toString();
            OutsourcedCarrierBean outsourcedCarrierBeanTemp = null;
            if (!mOutsourcedCarrierList.isEmpty()) {
                for (OutsourcedCarrierBean outsourcedCarrierBean : mOutsourcedCarrierList) {
                    if (outsourcedCarrierBean.toString().equalsIgnoreCase(outsourcedCarrierName)) {
                        outsourcedCarrierBeanTemp = outsourcedCarrierBean;
                    }
                }
            }
            final VehicleBean vehicle = vehicleTemp;
            final HangingBean hanging = hangingBeanTemp;
            final OutsourcedCarrierBean outsourcedCarrier = outsourcedCarrierBeanTemp;
            if (vehicle != null) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("api_token", mLoginModel.getToken());
                jsonObject.addProperty("selectkenteken", Integer.parseInt(vehicle.id));
                jsonObject.addProperty("selectaanhangwagen", hanging != null ? Integer.parseInt(hanging.id) : null);
                jsonObject.addProperty("uitbesteed_id", outsourcedCarrier != null ? Integer.parseInt(outsourcedCarrier.id) : null);
                mRestApi.saveVehicle(jsonObject)
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
                                handleGenericError(e);
                            }

                            @Override
                            public void onComplete() {

                            }
                        });
            } else {
                mProgressDialog.dismiss();
                Toast.makeText(this, "Vehicle not found please try some other name", Toast.LENGTH_LONG).show();
            }

        }
    }

    private void handleGenericError(Throwable e) {
        try {
            if (e.getLocalizedMessage().contains("401 Unauthorized")) {
                Toast.makeText(getApplicationContext(), "Error Occured " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                Utility.logoutUser(SelectVehicleActivity.this);
            } else {
                ResponseBody responseBody = ((HttpException) e).response().errorBody();
                if (responseBody != null) {
                    JSONObject responseJson = new JSONObject(responseBody.string());
                    if ("failed".equals(responseJson.get("status"))) {
                        if ("pending order - redirect".equals(responseJson.get("message"))) {
                            startActivity(new Intent(SelectVehicleActivity.this, MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Error Occured " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Error Occured " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Error Occured " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error Occured " + exception.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
        mProgressDialog.dismiss();
    }
}
