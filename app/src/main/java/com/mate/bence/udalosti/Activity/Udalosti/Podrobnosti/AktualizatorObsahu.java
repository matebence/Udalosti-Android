package com.mate.bence.udalosti.Activity.Udalosti.Podrobnosti;

import android.util.Log;

public class AktualizatorObsahu {

    private static final String TAG = AktualizatorObsahu.class.getName();
    private static AktualizatorObsahu aktualizatorObsahu;
    private Aktualizator aktualizator;

    private AktualizatorObsahu() {}

    public static AktualizatorObsahu zaujmy() {
        Log.v(AktualizatorObsahu.TAG, "Metoda zaujmy bola vykonana");

        if(AktualizatorObsahu.aktualizatorObsahu == null) {
            AktualizatorObsahu.aktualizatorObsahu = new AktualizatorObsahu();
        }
        return AktualizatorObsahu.aktualizatorObsahu;
    }

    public void nastav(Aktualizator aktualizator) {
        Log.v(AktualizatorObsahu.TAG, "Metoda nastav bola vykonana");

        this.aktualizator = aktualizator;
    }

    private void aktualizuj() {
        Log.v(AktualizatorObsahu.TAG, "Metoda aktualizuj bola vykonana");

        this.aktualizator.aktualizujObsahZaujmov();
    }

    public void hodnota() {
        Log.v(AktualizatorObsahu.TAG, "Metoda hodnota bola vykonana");

        if(this.aktualizator != null) {
            aktualizuj();
        }
    }
}