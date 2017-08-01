package com.huzefa.combinatieherepoort.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.huzefa.combinatieherepoort.AppManager;
import com.huzefa.combinatieherepoort.Constants;
import com.huzefa.combinatieherepoort.R;
import com.huzefa.combinatieherepoort.adapters.MyOrderRecyclerViewAdapter;
import com.huzefa.combinatieherepoort.fragments.OrderDetailsFragment;
import com.huzefa.combinatieherepoort.fragments.OrderFragment;
import com.huzefa.combinatieherepoort.interfaces.OnListFragmentInteractionListener;
import com.huzefa.combinatieherepoort.models.LoginModel;
import com.huzefa.combinatieherepoort.models.OrderModel;
import com.huzefa.combinatieherepoort.models.OrderModelList;
import com.huzefa.combinatieherepoort.models.UserModel;
import com.huzefa.combinatieherepoort.retrofit.RestApi;
import com.huzefa.combinatieherepoort.utility.Utility;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

import static java.security.AccessController.getContext;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnListFragmentInteractionListener {

    private LoginModel mLoginModel;
    private Typeface mTypeFace;
    private RestApi mRestApi;
    SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mLoginModel = new Gson().fromJson(Utility.getSharedPrefernce(this).getString(Constants.PREF_USER,null), LoginModel.class);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerLayout = navigationView.getHeaderView(0);
        mTypeFace = Utility.getTypeFace(this);
        TextView name = (TextView) headerLayout.findViewById(R.id.name);
        TextView username = (TextView) headerLayout.findViewById(R.id.username);
        name.setTypeface(mTypeFace);
        username.setTypeface(mTypeFace);
        UserModel user = mLoginModel.getUser();
        if(user!=null) {
            name.setText(user.getName());
            username.setText(user.getEmail());
        }
        username.setText(mLoginModel.getEmail());
        Retrofit retrofit = ((AppManager) getApplicationContext()).getRetrofit();
        mRestApi = retrofit.create(RestApi.class);
        mSharedPreferences = Utility.getSharedPrefernce(this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_orders) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_content, new OrderFragment())
                    .addToBackStack("order").commit();
        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_logout) {
            logOut();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onListFragmentInteraction(OrderModel item) {
        OrderDetailsFragment orderDetailsFragment = OrderDetailsFragment.newInstance(item.id);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_content,orderDetailsFragment)
                .addToBackStack("order_details")
                .commit();
    }

    @Override
    public void setTitle(String title) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(title);
    }

    private void logOut() {
        final ProgressDialog progressDialog = Utility.getProgressDialog(this,"Please wait..","Logging out..");
        progressDialog.show();
        LoginModel loginModel = new Gson().fromJson(mSharedPreferences.getString(Constants.PREF_USER, null), LoginModel.class);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("api_token", loginModel.getToken());
        mRestApi.logout("application/json", jsonObject)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<JsonObject>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull JsonObject s) {
                        progressDialog.dismiss();
                        if (s != null && s.has("status") && s.get("status").getAsString().equalsIgnoreCase("success")) {
                            mSharedPreferences.edit().clear().commit();
                            startActivity(new Intent(MainActivity.this, LoginActivity.class));
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Error occured " + s, Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Error occured " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
