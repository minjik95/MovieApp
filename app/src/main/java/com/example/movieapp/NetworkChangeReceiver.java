package com.example.movieapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class NetworkChangeReceiver extends BroadcastReceiver {
    private String text;

    @Override
    public void onReceive(Context context, Intent intent) {
        int status = NetworkStatus.getConnectivityStatus(context);

        if(status == 0) text = "lte가 연결되었음.";
        else if(status == 1) text = "wifi가 연결되었음.";
        else if(status == 2) text = "연결 안됨.";

        Toast.makeText(context, text, Toast.LENGTH_LONG).show();


    }
}
