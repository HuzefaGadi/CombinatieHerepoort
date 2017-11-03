package com.huzefa.combinatieherepoort.fragments;


import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.huzefa.combinatieherepoort.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecieptFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecieptFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_ID = "id";

    // TODO: Rename and change types of parameters
    private String mRecieptId;

    private WebView mWebView;
    //private PDFView mWebView;
    private ProgressDialog mProgressDialog;

    public RecieptFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment RecieptFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecieptFragment newInstance(String param1) {
        RecieptFragment fragment = new RecieptFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ID, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mRecieptId = getArguments().getString(ARG_ID);
        }
        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setMessage("Please wait..");
        mProgressDialog.setTitle("Loading..");
        mProgressDialog.setCancelable(false);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reciept, container, false);
        mWebView = (WebView) view.findViewById(R.id.pdfView);
       // mWebView = (PDFView) view.findViewById(R.id.pdfView);
        String pdf = "http://128.199.41.181/transactie/" + mRecieptId + "/pdf";
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setDisplayZoomControls(false);
        mWebView.setWebViewClient(new CustomWebViewClient());
        mWebView.loadUrl(pdf);
        return view;
    }


    class CustomWebViewClient extends WebViewClient {

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            mProgressDialog.dismiss();
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            mProgressDialog.show();
        }
    }

}
