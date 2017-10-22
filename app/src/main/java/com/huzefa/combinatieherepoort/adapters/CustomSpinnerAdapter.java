package com.huzefa.combinatieherepoort.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.huzefa.combinatieherepoort.beans.VehicleBean;

import java.util.List;

/**
 * Created by Rashida on 10/06/17.
 */

public class CustomSpinnerAdapter<T> extends ArrayAdapter<T> {
    // Initialise custom font, for example:
    Typeface font = Typeface.createFromAsset(getContext().getAssets(),
            "font.ttf");

    // (In reality I used a manager which caches the Typeface objects)
    // Typeface font = FontManager.getInstance().getFont(getContext(), BLAMBOT);

    public CustomSpinnerAdapter(Context context, int resource, List<T> items) {
        super(context, resource, items);
    }

    // Affects default (closed) state of the spinner
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getView(position, convertView, parent);
        view.setTypeface(font);
        return view;
    }

    // Affects opened state of the spinner
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getDropDownView(position, convertView, parent);
        view.setTypeface(font);
        return view;
    }
}
