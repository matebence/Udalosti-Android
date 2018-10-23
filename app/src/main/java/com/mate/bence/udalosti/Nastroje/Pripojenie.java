package com.mate.bence.udalosti.Nastroje;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class Pripojenie {

    private static final String TAG = Pripojenie.class.getName();

    public static boolean pripojenieExistuje(Context context) {
        Log.v(Pripojenie.TAG, "Metoda pripojenieExistuje bola vykonana");

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        assert connectivityManager != null;
        NetworkInfo sietWifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobilnaSiet = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        return (sietWifi != null && sietWifi.isConnected()) || (mobilnaSiet != null && mobilnaSiet.isConnected());
    }
}