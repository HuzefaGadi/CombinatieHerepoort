package com.huzefa.combinatieherepoort.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huzefa.combinatieherepoort.R;
import com.huzefa.combinatieherepoort.models.SenderModel;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Rashida on 01/08/17.
 */

public class OrderDetailsAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private List<String> mExpandableListTitle;
    private List<Integer> mExpadableListIcon;
    private HashMap<String, SenderModel> mExpandableListDetail;
    Typeface font;
    boolean mExpandedFirst, mExpandedSecond;

    public OrderDetailsAdapter(Context context, List<String> expandableListTitle,
                               List<Integer> expadableListIcon,
                               HashMap<String, SenderModel> expandableListDetail) {
        mContext = context;
        mExpandableListTitle = expandableListTitle;
        mExpandableListDetail = expandableListDetail;
        mExpadableListIcon = expadableListIcon;
        font = Typeface.createFromAsset(context.getAssets(),
                "font.ttf");
    }

    @Override
    public SenderModel getChild(int listPosition, int expandedListPosition) {
        return this.mExpandableListDetail.get(this.mExpandableListTitle.get(listPosition));
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final SenderModel senderModel = getChild(listPosition, expandedListPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_item, null);
        }
        if(listPosition == 3 && senderModel.name == null) {
            TextView name = (TextView) convertView.findViewById(R.id.name);
            name.setText("Geen bemiddelaar");
            name.setTypeface(font);
            TextView address = (TextView) convertView.findViewById(R.id.address);
            address.setText("");
            address.setTypeface(font);
            TextView postalCodeAndCity = (TextView) convertView.findViewById(R.id.postcode_city);
            postalCodeAndCity.setText("");
            postalCodeAndCity.setTypeface(font);
        } else {
            TextView name = (TextView) convertView.findViewById(R.id.name);
            name.setText(senderModel.name);
            name.setTypeface(font);
            TextView address = (TextView) convertView.findViewById(R.id.address);
            address.setText(senderModel.address);
            address.setTypeface(font);
            TextView postalCodeAndCity = (TextView) convertView.findViewById(R.id.postcode_city);
            postalCodeAndCity.setText(senderModel.postcode + " " + senderModel.city);
            postalCodeAndCity.setTypeface(font);
        }
        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.mExpandableListTitle.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return this.mExpandableListTitle.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String listTitle = (String) getGroup(listPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) mContext.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_group, null);
        }
        TextView listTitleTextView = (TextView) convertView.findViewById(R.id.listTitle);
        listTitleTextView.setTypeface(font, Typeface.BOLD);
        listTitleTextView.setText(listTitle);

        ((ImageView) convertView.findViewById(R.id.listIcon)).setImageResource(mExpadableListIcon.get(listPosition));
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }
}
