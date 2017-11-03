package com.huzefa.combinatieherepoort.interfaces;

import com.huzefa.combinatieherepoort.models.OrderDetailModel;

/**
 * Created by Rashida on 01/08/17.
 */

public interface OnListFragmentInteractionListener {

    void onListFragmentInteraction(String id);
    void setTitle(String title);
    void showPdf(String id);
    void logOutUser();
    void goToOrderPage();
}
