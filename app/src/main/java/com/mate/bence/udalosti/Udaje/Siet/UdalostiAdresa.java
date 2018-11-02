package com.mate.bence.udalosti.Udaje.Siet;

import android.util.Log;

public class UdalostiAdresa {

    private static final String TAG = UdalostiAdresa.class.getName();

    private UdalostiAdresa() {
    }

    private static final String ADRESA = "http://app-udalosti.8u.cz/index.php/";

    public static Requesty initAdresu() {
        Log.v(UdalostiAdresa.TAG, "Metoda UdalostiAdresa initAdresu bola vykonana");

        return HTTPRequest.posliRequest(UdalostiAdresa.ADRESA).create(Requesty.class);
    }

    public static String getAdresa() {
        return UdalostiAdresa.ADRESA;
    }
}