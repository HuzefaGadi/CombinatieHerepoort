package com.huzefa.combinatieherepoort.adapters;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.huzefa.combinatieherepoort.R;
import com.huzefa.combinatieherepoort.fragments.OrderFragment.OnListFragmentInteractionListener;
import com.huzefa.combinatieherepoort.models.OrderModel;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link OrderModel} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class MyOrderRecyclerViewAdapter extends RecyclerView.Adapter<MyOrderRecyclerViewAdapter.ViewHolder> {

    private final List<OrderModel> mValues;
    private final OnListFragmentInteractionListener mListener;
    private Context mContext;
    private Typeface mTypeFace;

    public MyOrderRecyclerViewAdapter(Context context, List<OrderModel> items, OnListFragmentInteractionListener listener) {
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

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
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
        OrderModel mItem;

        ViewHolder(View view) {
            super(view);
            mView = view;
            mPartNumberText = (TextView) view.findViewById(R.id.partyNumber);
            mPartNumberText.setTypeface(mTypeFace);
            mContentView = (TextView) view.findViewById(R.id.content);
            mContentView.setTypeface(mTypeFace);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
