package com.huzefa.combinatieherepoort.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.huzefa.combinatieherepoort.AppManager;
import com.huzefa.combinatieherepoort.Constants;
import com.huzefa.combinatieherepoort.R;
import com.huzefa.combinatieherepoort.adapters.OrderDetailsAdapter;
import com.huzefa.combinatieherepoort.interfaces.OnListFragmentInteractionListener;
import com.huzefa.combinatieherepoort.models.LoginModel;
import com.huzefa.combinatieherepoort.models.OrderModel;
import com.huzefa.combinatieherepoort.models.SenderModel;
import com.huzefa.combinatieherepoort.retrofit.RestApi;
import com.huzefa.combinatieherepoort.utility.CustomDialog;
import com.huzefa.combinatieherepoort.utility.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

import static android.R.attr.order;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnListFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OrderDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderDetailsFragment extends Fragment implements CustomDialog.OnDialogCommunicationListener {
    // the fragment initialization parameters,
    private static final String ARG_ORDER_ID = "orderId";
    private String mOrderId;
    private RestApi mRestApi;
    private ProgressDialog mProgressDialog;
    private SharedPreferences mSharedPreferences;

    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    List<Integer> expandableListIcon;
    HashMap<String, SenderModel> expandableListDetail;

    ImageView mStatusIcon;
    TextView mAlfalstroomNumber, mMaterialType, mAntal, mEuralCode;
    Typeface font;
    Button mOrderActionButton;
    View mHeaderView;

    private OnListFragmentInteractionListener mListener;

    public OrderDetailsFragment() {
        // Required empty public constructor
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param orderId OrderId.
     * @return A new instance of fragment OrderDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrderDetailsFragment newInstance(String orderId) {
        OrderDetailsFragment fragment = new OrderDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ORDER_ID, orderId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Retrofit retrofit = ((AppManager) getActivity().getApplicationContext()).getRetrofit();
        mRestApi = retrofit.create(RestApi.class);
        mSharedPreferences = Utility.getSharedPrefernce(getContext());
        if (getArguments() != null) {
            mOrderId = getArguments().getString(ARG_ORDER_ID);
        }
        font = Typeface.createFromAsset(getContext().getAssets(),
                "font.ttf");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_details, container, false);
        expandableListView = (ExpandableListView) view.findViewById(R.id.expandableListView);

        mProgressDialog = Utility.getProgressDialog(getContext(), "Please wait..", "Loading Orders..");
        mProgressDialog.show();
        LoginModel loginModel = new Gson().fromJson(mSharedPreferences.getString(Constants.PREF_USER, null), LoginModel.class);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("api_token", loginModel.getToken());
        jsonObject.addProperty("id", mOrderId);
        mRestApi.getOrder("application/json", jsonObject)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<OrderModel>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull OrderModel orderModel) {
                        mProgressDialog.dismiss();
                        if (orderModel != null) {
                            showOrderDetails(orderModel);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Toast.makeText(getContext(), "Error Occured " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        mProgressDialog.dismiss();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        return view;
    }

    private void showOrderDetails(final OrderModel order) {
        mListener.setTitle(order.lotNumber);
        mHeaderView = getActivity().getLayoutInflater().inflate(R.layout.order_list_header, null);
        mStatusIcon = (ImageView) mHeaderView.findViewById(R.id.statusIcon);
        mAlfalstroomNumber = (TextView) mHeaderView.findViewById(R.id.afvalstroomnummer);
        mMaterialType = (TextView) mHeaderView.findViewById(R.id.material_type);
        mAntal = (TextView) mHeaderView.findViewById(R.id.antal);
        mEuralCode = (TextView) mHeaderView.findViewById(R.id.eural_code);

        mAlfalstroomNumber.setText(order.afvalstroomnummer);
        mAlfalstroomNumber.setTypeface(font);

        mAntal.setText(order.noOfPackages);
        mAntal.setTypeface(font);

        mMaterialType.setText(order.materialType);
        mMaterialType.setTypeface(font);

        mEuralCode.setText(order.euralCode);
        mEuralCode.setTypeface(font);

        mOrderActionButton = (Button) getView().findViewById(R.id.order_action_button);
        mOrderActionButton.setBackgroundColor(getResources().getColor(order.status == 1 ? R.color.order_pending_button_color : R.color.order_accepted_button_color));
        mOrderActionButton.setText(getString(order.status == 1 ? R.string.order_pending_button_text : R.string.order_accepted_button_text));
        mOrderActionButton.setTypeface(font);

        mOrderActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (order.status == 1) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Please confirm");
                    builder.setMessage("Are you sure you want to continue ?");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // do api calls
                            confirmOrder(order);
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                } else {
                    final CustomDialog customDialog = new CustomDialog(getActivity(), OrderDetailsFragment.this);
                    customDialog.show();
                }
            }
        });
        mStatusIcon.setImageResource(order.status == 1 ? R.drawable.icon_pending : R.drawable.icon_accepted);

        expandableListDetail = new LinkedHashMap<>();
        expandableListDetail.put("Afvoerlocatie", order.afvoerlocatie);
        expandableListDetail.put("Verwerkingslocatie", order.verwerkingslocatie);
        expandableListDetail.put("Afzender", order.afzender);
        expandableListDetail.put("Ontdoener", order.ontdoener);
        expandableListDetail.put("Vervoerder", order.vervoerder);


        expandableListIcon = new ArrayList<>();
        expandableListIcon.add(R.drawable.truck);
        expandableListIcon.add(R.drawable.factory);
        expandableListIcon.add(R.drawable.user);
        expandableListIcon.add(R.drawable.user);
        expandableListIcon.add(R.drawable.construction_worker);

        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            expandableListView.setIndicatorBounds(width - GetPixelFromDips(50), width - GetPixelFromDips(10));
        } else {
            expandableListView.setIndicatorBoundsRelative(width - GetPixelFromDips(50), width - GetPixelFromDips(10));
        }

        expandableListView.addHeaderView(mHeaderView);
        expandableListTitle = new ArrayList<>(expandableListDetail.keySet());
        expandableListAdapter = new OrderDetailsAdapter(getContext(), expandableListTitle, expandableListIcon, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);

        expandableListView.expandGroup(0);
        expandableListView.expandGroup(1);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public int GetPixelFromDips(float pixels) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }

    public void confirmOrder(final OrderModel order) {
        final ProgressDialog progressDialog = Utility.getProgressDialog(getActivity(), "Please wait..", "Confirming");
        progressDialog.show();
        LoginModel loginModel = new Gson().fromJson(mSharedPreferences.getString(Constants.PREF_USER, null), LoginModel.class);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("api_token", loginModel.getToken());
        jsonObject.addProperty("id", mOrderId);
        mRestApi.confirm("application/json", jsonObject)
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
                            order.status = 2;
                            mStatusIcon.setImageResource(R.drawable.icon_accepted);
                            mOrderActionButton.setBackgroundColor(getResources().getColor(R.color.order_accepted_button_color));
                            mOrderActionButton.setText(getString(R.string.order_accepted_button_text));
                        } else {
                            Toast.makeText(getContext(), "Error occured " + s, Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "Error occured " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    @Override
    public void setWeight(final CustomDialog customDialog, String weight) {

        final ProgressDialog progressDialog = Utility.getProgressDialog(getActivity(), "Please wait..", "Confirming");
        progressDialog.show();
        LoginModel loginModel = new Gson().fromJson(mSharedPreferences.getString(Constants.PREF_USER, null), LoginModel.class);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("api_token", loginModel.getToken());
        jsonObject.addProperty("id", mOrderId);
        jsonObject.addProperty("hoeveelheid", weight);
        mRestApi.setweight("application/json", jsonObject)
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
                            customDialog.dismiss();
                            getActivity().getSupportFragmentManager().popBackStack();
                        } else {
                            Toast.makeText(getContext(), "Error occured " + s, Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "Error occured " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}
