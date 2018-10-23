package com.mate.bence.udalosti.Udaje.Siet;

import android.util.Log;

public class GeoAdresa {

    private static final String TAG = GeoAdresa.class.getName();

    private GeoAdresa() {
    }

    private static final String ADRESA_GEOCODE = "https://eu1.locationiq.com/v1/";
    private static final String ADRESA_IPGEO = "http://ip-api.com/";

    public static Requesty initAdresu(boolean pozicia) {
        Log.v(GeoAdresa.TAG, "Metoda GeoAdresa initAdresu bola vykonana");

        if(pozicia){
            HTTPRequest.setRequest(null);
            return HTTPRequest.posliRequest(GeoAdresa.ADRESA_GEOCODE).create(Requesty.class);
        }else{
            return HTTPRequest.posliRequest(GeoAdresa.ADRESA_IPGEO).create(Requesty.class);
        }
    }

    public static void initNanovo() {
        HTTPRequest.setRequest(null);
    }
}