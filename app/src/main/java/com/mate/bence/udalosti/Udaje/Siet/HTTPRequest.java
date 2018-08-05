package com.mate.bence.udalosti.Udaje.Siet;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

class HTTPRequest {
    private static Retrofit request = null;

    static Retrofit posliRequest(String adresa) {
        if (request == null) {
            request = new Retrofit.Builder()
                    .baseUrl(adresa)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return request;
    }

    public static void setRequest(Retrofit request) {
        HTTPRequest.request = request;
    }
}