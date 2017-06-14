package com.huzefa.combinatieherepoort.utility;

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
}
