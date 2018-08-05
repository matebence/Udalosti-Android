package com.mate.bence.udalosti.Nastroje;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Pripojenie {
    public static boolean pripojenieExistuje(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo sietWifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobilnaSiet = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        return (sietWifi != null && sietWifi.isConnected()) || (mobilnaSiet != null && mobilnaSiet.isConnected());
    }
}
