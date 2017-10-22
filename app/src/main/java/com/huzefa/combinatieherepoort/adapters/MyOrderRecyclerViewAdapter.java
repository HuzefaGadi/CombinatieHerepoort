package com.huzefa.combinatieherepoort.adapters;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.huzefa.combinatieherepoort.R;
import com.huzefa.combinatieherepoort.interfaces.OnListFragmentInteractionListener;
import com.huzefa.combinatieherepoort.models.OrderDetailModel;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link OrderDetailModel} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class MyOrderRecyclerViewAdapter extends RecyclerView.Adapter<MyOrderRecyclerViewAdapter.ViewHolder> {

    private final List<OrderDetailModel> mValues;
    private final OnListFragmentInteractionListener mListener;
    private Context mContext;
    private Typeface mTypeFace;

    public MyOrderRecyclerViewAdapter(Context context, List<OrderDetailModel> items, OnListFragmentInteractionListener listener) {
        mContext = context;
        mValues = items;
        mListener = listener;
        AssetManager am = mContext.getAssets();
        mTypeFace = Typeface.createFromAsset(am, "font.ttf");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mPartNumberText.setText(mValues.get(position).lotNumber);
        holder.mContentView.setText(mValues.get(position).drainageLocation);
        holder.mStatusIcon.setImageResource(mValues.get(position).status == 1 ? R.drawable.icon_pending : R.drawable.icon_accepted);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                   // mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView mPartNumberText;
        final TextView mContentView;
        final ImageView mStatusIcon;
        OrderDetailModel mItem;

        ViewHolder(View view) {
            super(view);
            mView = view;
            mPartNumberText = (TextView) view.findViewById(R.id.partyNumber);
            mPartNumberText.setTypeface(mTypeFace);
            mContentView = (TextView) view.findViewById(R.id.content);
            mContentView.setTypeface(mTypeFace);
            mStatusIcon = (ImageView) view.findViewById(R.id.statusIcon);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
