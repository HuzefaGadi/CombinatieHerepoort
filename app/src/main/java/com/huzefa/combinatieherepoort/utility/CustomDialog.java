package com.huzefa.combinatieherepoort.utility;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.huzefa.combinatieherepoort.R;
import com.huzefa.combinatieherepoort.fragments.OrderDetailsFragment;

import static android.R.attr.order;

/**
 * Created by Rashida on 02/08/17.
 */

public class CustomDialog extends Dialog implements View.OnClickListener{

    private Activity mActivity;
    private Dialog mDialog;
    private Button mConfirmButton, mDummyButton;
    private EditText mInputWeight;
    private View.OnClickListener mOnClickListener;
    private OnDialogCommunicationListener mDialogCommunicationListener;

    public CustomDialog(Activity a, OnDialogCommunicationListener onDialogCommunicationListener) {
        super(a);
        // TODO Auto-generated constructor stub
        this.mActivity = a;
        mDialogCommunicationListener = onDialogCommunicationListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog);
        mConfirmButton = (Button) findViewById(R.id.confirm);
        mInputWeight = (EditText) findViewById(R.id.weightInput);
        mConfirmButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.confirm) {
            String weight = mInputWeight.getText().toString();
            if(!weight.isEmpty() && !weight.contains(",")) {
                String decimals[] = weight.split("\\.");
                if(decimals.length == 2) {
                    if(decimals[1].length() == 3) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                        builder.setTitle("Please confirm");
                        builder.setMessage("Are you sure you want to continue ?");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // do api calls
                                mDialogCommunicationListener.setWeight(CustomDialog.this, mInputWeight.getText().toString());
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
                        showError("There should be 3 decimal points");
                        return;
                    }
                } else {
                    showError("There should be 3 decimal points");
                    return;
                }
            } else {
                showError("Invalid input");
                return;
            }
        }
    }

    public void showError(String message) {
        Toast.makeText(mActivity.getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }


    public interface OnDialogCommunicationListener {
        void setWeight(CustomDialog customDialog, String weight);
    }
}
