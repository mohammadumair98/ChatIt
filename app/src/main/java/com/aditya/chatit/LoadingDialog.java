package com.aditya.chatit;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

public class LoadingDialog {

    Activity activity;
    AlertDialog alertDialog;

    LoadingDialog(Activity myActivity) {
        activity = myActivity;
    }

    void startLoadDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.custom_loading_dialog, null));
        builder.setCancelable(false);


        alertDialog = builder.create();
        alertDialog.show();
    }
    void dismissDialog(){
        alertDialog.dismiss();
    }
}
