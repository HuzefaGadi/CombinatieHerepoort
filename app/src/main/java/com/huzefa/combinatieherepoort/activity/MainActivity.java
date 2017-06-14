package com.huzefa.combinatieherepoort.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.huzefa.combinatieherepoort.Constants;
import com.huzefa.combinatieherepoort.R;
import com.huzefa.combinatieherepoort.fragments.OrderFragment;
import com.huzefa.combinatieherepoort.beans.OrderBean;
import com.huzefa.combinatieherepoort.models.LoginModel;
import com.huzefa.combinatieherepoort.models.UserModel;
import com.huzefa.combinatieherepoort.utility.Utility;

import okhttp3.internal.Util;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OrderFragment.OnListFragmentInteractionListener {

    private LoginModel mLoginModel;
    private Typeface mTypeFace;
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

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onListFragmentInteraction(OrderBean item) {

    }
}
