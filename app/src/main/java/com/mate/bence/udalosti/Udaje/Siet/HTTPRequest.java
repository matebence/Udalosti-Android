package com.mate.bence.udalosti.Udaje.Siet;

import android.util.Log;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class HTTPRequest {

    private static final String TAG = HTTPRequest.class.getName();

    private static Retrofit request = null;

    static Retrofit posliRequest(String adresa) {
        Log.v(HTTPRequest.TAG, "Metoda posliRequest bola vykonana");

        if (HTTPRequest.request == null) {
            HTTPRequest.request = new Retrofit.Builder()
                    .baseUrl(adresa)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return HTTPRequest.request;
    }

    static void setRequest(Retrofit request) {
        HTTPRequest.request = request;
    }
}