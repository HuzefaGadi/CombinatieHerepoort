package com.huzefa.combinatieherepoort.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.huzefa.combinatieherepoort.AppManager;
import com.huzefa.combinatieherepoort.Constants;
import com.huzefa.combinatieherepoort.R;
import com.huzefa.combinatieherepoort.adapters.CustomSpinnerAdapter;
import com.huzefa.combinatieherepoort.interfaces.OnListFragmentInteractionListener;
import com.huzefa.combinatieherepoort.models.LoginModel;
import com.huzefa.combinatieherepoort.models.OrderModel;
import com.huzefa.combinatieherepoort.models.OrderModelList;
import com.huzefa.combinatieherepoort.retrofit.RestApi;
import com.huzefa.combinatieherepoort.utility.Utility;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.HttpException;
import retrofit2.Retrofit;


/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class OrderFragment extends Fragment {

    private OnListFragmentInteractionListener mListener;
    private Typeface mTypeFace;
    private RestApi mRestApi;
    private ProgressDialog mProgressDialog;
    private SharedPreferences mSharedPreferences;

    @BindView(R.id.transactionNumberAutoTextView)
    Spinner mTransactionNumberAutoTextView;

    @BindView(R.id.materialTypeAutoTextView)
    Spinner mMaterialTypeAutoTextView;

    @BindView(R.id.clusterAutoTextView)
    Spinner mClusterAutoTextView;

    @BindView(R.id.lotNumberAutoTextView)
    Spinner mLotNumberAutoTextView;

    OrderModel mSelectedOrderModel;
    LoginModel mLoginModel;

    List<String> mTransactionNumberList, mMaterialTypeList, mClusterList, mLotNumberList;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public OrderFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Retrofit retrofit = ((AppManager) getActivity().getApplicationContext()).getRetrofit();
        mRestApi = retrofit.create(RestApi.class);
        mSharedPreferences = Utility.getSharedPrefernce(getContext());
        mTransactionNumberList = new ArrayList<>();
        mSelectedOrderModel = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_list, container, false);
        mListener.setTitle(getString(R.string.app_name));
        ButterKnife.bind(this, view);
        mProgressDialog = Utility.getProgressDialog(getContext(), "Please wait..", "Loading Orders..");
        mProgressDialog.show();
        mLoginModel = new Gson().fromJson(mSharedPreferences.getString(Constants.PREF_USER, null), LoginModel.class);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("api_token", mLoginModel.getToken());
        Typeface font = Typeface.createFromAsset(getContext().getAssets(),
                "font.ttf");
        //mLotNumberAutoTextView.setTypeface(font);
        mRestApi.getOrders(jsonObject)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<OrderModelList>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull final OrderModelList orderModels) {
                        mProgressDialog.dismiss();
                        if (orderModels.orders != null) {
                            if (orderModels.orders.isEmpty()) {
                                Toast.makeText(getContext(), "Currently no orders are available", Toast.LENGTH_LONG).show();
                            } else {
                                for (int i = 0; i < orderModels.orders.size(); i++) {
                                    orderModels.orders.get(i).avoidNullPointer();
                                }
                                mTransactionNumberList = new ArrayList<String>();

                                for (OrderModel orderModel : orderModels.orders) {
                                    mTransactionNumberList.add(orderModel.transactionType);
                                }
                                if (!mTransactionNumberList.isEmpty()) {
                                    Set<String> set = new HashSet<String>(mTransactionNumberList);
                                    mTransactionNumberList.clear();
                                    mTransactionNumberList.add(getString(R.string.select_transaction_hint));
                                    mTransactionNumberList.addAll(set);
                                    CustomSpinnerAdapter<String> adapterForTransaction = new CustomSpinnerAdapter<String>(
                                            getContext(), R.layout.custom_auto_complete_dropdown, mTransactionNumberList);

                                    adapterForTransaction.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    mTransactionNumberAutoTextView.setAdapter(adapterForTransaction);
                                    mMaterialTypeAutoTextView.setEnabled(true);
                                    mTransactionNumberAutoTextView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                            setupMaterialType(orderModels, position);
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> parent) {

                                        }
                                    });

                                    if (mTransactionNumberList.size() == 2) { // only 1 entry select it
                                        mTransactionNumberAutoTextView.setSelection(1);
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        try {
                            if (e.getLocalizedMessage().contains("401 Unauthorized")) {
                                Toast.makeText(getContext(), "Error Occured " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                mListener.logOutUser();
                            } else {
                                ResponseBody responseBody = ((HttpException) e).response().errorBody();
                                if (responseBody != null) {
                                    JSONObject responseJson = new JSONObject(responseBody.string());
                                    if ("failed".equals(responseJson.get("status"))) {
                                        if ("pending order - redirect".equals(responseJson.get("message"))) {
                                            mListener.onListFragmentInteraction(String.valueOf(responseJson.get("id")));
                                        } else {
                                            Toast.makeText(getContext(), "Error Occured " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        Toast.makeText(getContext(), "Error Occured " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    Toast.makeText(getContext(), "Error Occured " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        } catch (Exception exception) {
                            exception.printStackTrace();
                            Toast.makeText(getContext(), "Error Occured " + exception.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        }

                        mProgressDialog.dismiss();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
        return view;
    }


    private void setupMaterialType(final OrderModelList orderModels, int position) {
        mSelectedOrderModel = null;
        mMaterialTypeList = new ArrayList<String>();

        for (OrderModel orderModel : orderModels.orders) {
            if (orderModel.transactionType.equals(mTransactionNumberList.get(position))) {
                mMaterialTypeList.add(orderModel.materialType);
            }
        }

        if (!mMaterialTypeList.isEmpty() || position == 0) {
            Set<String> set = new HashSet<String>(mMaterialTypeList);
            mMaterialTypeList.clear();
            mMaterialTypeList.add(getString(R.string.select_material_type_hint));
            mMaterialTypeList.addAll(set);

            if (position == 0) {
                mMaterialTypeAutoTextView.setSelection(0);
                mMaterialTypeAutoTextView.setEnabled(false);
            } else {
                mMaterialTypeAutoTextView.setEnabled(true);
            }

            CustomSpinnerAdapter<String> adapterForMaterialType = new CustomSpinnerAdapter<String>(
                    getContext(), R.layout.custom_auto_complete_dropdown, mMaterialTypeList);

            adapterForMaterialType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mMaterialTypeAutoTextView.setAdapter(adapterForMaterialType);
            mClusterAutoTextView.setEnabled(true);

            mMaterialTypeAutoTextView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    setupCluster(orderModels, position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            if (mMaterialTypeList.size() == 2) { // only 1 entry select it
                mMaterialTypeAutoTextView.setSelection(1);
            }
        }
    }

    private void setupCluster(final OrderModelList orderModels, int position) {
        mSelectedOrderModel = null;
        mClusterList = new ArrayList<String>();
        for (OrderModel orderModel : orderModels.orders) {
            if (orderModel.materialType.equals(mMaterialTypeList.get(position))
                    && orderModel.transactionType.equals(mTransactionNumberAutoTextView.getSelectedItem())) {
                mClusterList.add(orderModel.cluster);
            }
        }

        if (!mClusterList.isEmpty() || position == 0) {
            Set<String> set = new HashSet<String>(mClusterList);
            mClusterList.clear();
            mClusterList.add(getString(R.string.select_cluster_hint));
            mClusterList.addAll(set);

            if (position == 0) {
                mClusterAutoTextView.setSelection(0);
                mClusterAutoTextView.setEnabled(false);
            } else {
                mClusterAutoTextView.setEnabled(true);
            }
            CustomSpinnerAdapter<String> adapter = new CustomSpinnerAdapter<String>(
                    getContext(), R.layout.custom_auto_complete_dropdown, mClusterList);

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mClusterAutoTextView.setAdapter(adapter);
            mLotNumberAutoTextView.setEnabled(true);
            mClusterAutoTextView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    setupLotNumber(orderModels, position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            if (mClusterList.size() == 2) {// only 1 value then select it
                mClusterAutoTextView.setSelection(1);
            }

        }
    }

    private void setupLotNumber(final OrderModelList orderModels, int position) {
        mSelectedOrderModel = null;
        mLotNumberList = new ArrayList<>();
        for (OrderModel orderModel : orderModels.orders) {
            if (orderModel.transactionType.equals(mTransactionNumberAutoTextView.getSelectedItem().toString()) &&
                    orderModel.materialType.equals(mMaterialTypeAutoTextView.getSelectedItem().toString()) &&
                    orderModel.cluster.equals(mClusterAutoTextView.getSelectedItem().toString())) {
                mLotNumberList.add(orderModel.lotNumber);
            }
        }
        if(!mLotNumberList.isEmpty() || position == 0) {

            Set<String> set = new LinkedHashSet<String>(mLotNumberList);
            mLotNumberList.clear();
            mLotNumberList.add(getString(R.string.select_lot_number_hint));
            mLotNumberList.addAll(set);


            if (position == 0) {
                mLotNumberAutoTextView.setSelection(0);
                mLotNumberAutoTextView.setEnabled(false);
            } else {
                mLotNumberAutoTextView.setEnabled(true);
            }

            CustomSpinnerAdapter<String> adapter = new CustomSpinnerAdapter<String>(
                    getContext(), R.layout.custom_auto_complete_dropdown, mLotNumberList);

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            mLotNumberAutoTextView.setAdapter(adapter);
            mLotNumberAutoTextView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(position != 0) {
                        mSelectedOrderModel = orderModels.orders.get(position-1);
                    } else {
                        mSelectedOrderModel = null;
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            if (mLotNumberList.size() == 2) {// only 1 value then select it
                mLotNumberAutoTextView.setSelection(1);
            }
        }
    }

    @OnClick(R.id.saveOrderButton)
    public void saveTransaction(View v) {
        if (mSelectedOrderModel != null) {
            mProgressDialog.show();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("api_token", mLoginModel.getToken());
            jsonObject.addProperty("partijnummer", mSelectedOrderModel.lotNumber);
            mRestApi.createTransaction(jsonObject)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<JsonObject>() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {

                        }

                        @Override
                        public void onNext(@NonNull final JsonObject response) {
                            mProgressDialog.dismiss();
                            if (response.get("status").getAsString().equals("success")) {
                                mListener.onListFragmentInteraction(response.get("id").toString());
                            } else {
                                Toast.makeText(getContext(), "Failed: " + response.get("status"), Toast.LENGTH_LONG).show();
                            }

                        }

                        @Override
                        public void onError(@NonNull Throwable e) {
                            try {
                                if (e.getLocalizedMessage().contains("401 Unauthorized")) {
                                    Toast.makeText(getContext(), "Error Occured " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                    mListener.logOutUser();
                                } else {
                                    ResponseBody responseBody = ((HttpException) e).response().errorBody();
                                    if (responseBody != null) {
                                        JSONObject responseJson = new JSONObject(responseBody.string());
                                        if ("failed".equals(responseJson.get("status"))) {
                                            if ("pending order - redirect".equals(responseJson.get("message"))) {
                                                mListener.onListFragmentInteraction(String.valueOf(responseJson.get("id")));
                                            } else {
                                                Toast.makeText(getContext(), "Error Occured " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        } else {
                                            Toast.makeText(getContext(), "Error Occured " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    } else {
                                        Toast.makeText(getContext(), "Error Occured " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            } catch (Exception exception) {
                                exception.printStackTrace();
                                Toast.makeText(getContext(), "Error Occured " + exception.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                            }
                            mProgressDialog.dismiss();
                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        } else {
            Toast.makeText(getContext(), "All Values are Mandatory", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
