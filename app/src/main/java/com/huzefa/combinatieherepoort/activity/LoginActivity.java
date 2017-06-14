package com.huzefa.combinatieherepoort.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.huzefa.combinatieherepoort.AppManager;
import com.huzefa.combinatieherepoort.Constants;
import com.huzefa.combinatieherepoort.R;
import com.huzefa.combinatieherepoort.models.CredentialsModel;
import com.huzefa.combinatieherepoort.models.LoginModel;
import com.huzefa.combinatieherepoort.retrofit.RestApi;
import com.huzefa.combinatieherepoort.utility.Utility;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.username)
    EditText username;

    @BindView(R.id.password)
    EditText password;

    @BindView(R.id.footer)
    TextView footer;

    @BindView(R.id.loginText)
    TextView loginText;

    @BindView(R.id.loginButton)
    Button loginButton;

    RestApi mRestApi;
    private static String TAG;
    Typeface mTypeFace;

    SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        TAG = this.getClass().getSimpleName();
        ButterKnife.bind(this);
        Retrofit retrofit = ((AppManager) getApplicationContext()).getRetrofit();
        mSharedPreferences = Utility.getSharedPrefernce(this);

        if (mSharedPreferences.getString(Constants.PREF_USER, null) != null) {
            if (mSharedPreferences.getString(Constants.PREF_VEHICLE, null) != null) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            } else {
                startActivity(new Intent(getApplicationContext(), SelectVehicleActivity.class));
                finish();
            }
        }
        mTypeFace = Utility.getTypeFace(this);
        mRestApi = retrofit.create(RestApi.class);
        username.setTypeface(mTypeFace);
        password.setTypeface(mTypeFace);
        loginButton.setTypeface(mTypeFace);
        loginText.setTypeface(mTypeFace);
        footer.setTypeface(mTypeFace);
        username.setText("test@test");
        password.setText("test123");
    }

    @OnClick(R.id.loginButton)
    public void onClick(View v) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait..");
        progressDialog.setMessage("Logging in..");
        progressDialog.setCancelable(false);
        progressDialog.show();
        CredentialsModel credentialsModel = new CredentialsModel();
        credentialsModel.setEmail(username.getText().toString());
        credentialsModel.setPassword(password.getText().toString());
        mRestApi.loginUser(credentialsModel)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LoginModel>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull LoginModel loginModel) {
                        progressDialog.dismiss();
                        if (loginModel.getStatus().equalsIgnoreCase("success")) {
                            Log.d(TAG, "Login successful with " + new Gson().toJson(loginModel));
                            mSharedPreferences.edit().putString(Constants.PREF_USER, new Gson().toJson(loginModel)).commit();
                            startActivity(new Intent(getApplicationContext(), SelectVehicleActivity.class));
                            finish();
                        } else if (loginModel.getStatus().equals("failed")) {
                            Toast.makeText(getApplicationContext(), "Invalid Username and Password", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Login failed status--> " + loginModel.getStatus(), Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        progressDialog.dismiss();
                        Log.d(TAG, "Login failed " + e.getLocalizedMessage());
                        Toast.makeText(getApplicationContext(), "Login failed due to " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}
