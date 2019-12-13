package com.example.movieapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkStatus {

    public static int TYPE_MOBILE = 0;
    public static int TYPE_WIFI = 1;
    public static int TYPE_NOT_CONNECTED = 2;

    public static int getConnectivityStatus(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        if(networkInfo != null) {
            int type = networkInfo.getType();

            if(type == ConnectivityManager.TYPE_MOBILE) {
                return TYPE_MOBILE;
            } else if(type == ConnectivityManager.TYPE_WIFI) {
                return TYPE_WIFI;
            }
        }

        return TYPE_NOT_CONNECTED;
    }
}
