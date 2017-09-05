package com.huzefa.combinatieherepoort.utility;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;

import com.huzefa.combinatieherepoort.activity.LoginActivity;

/**
 * Created by Rashida on 04/06/17.
 */

public class Utility {

    public static SharedPreferences getSharedPrefernce(Context context) {
        return context.getSharedPreferences("combinatie",Context.MODE_PRIVATE);
    }

    public static Typeface getTypeFace(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "font.ttf");
    }

    public static ProgressDialog getProgressDialog(Context context, String title, String message) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        return progressDialog;
    }

    public static void logoutUser(Activity activity) {
        getSharedPrefernce(activity).edit().clear().commit();
        Intent intent = new Intent(activity, LoginActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }
}
