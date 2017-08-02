package com.huzefa.combinatieherepoort.utility;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;

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
}
