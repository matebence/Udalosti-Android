package com.mate.bence.udalosti.Activity.UvodnaObrazovka;

import android.content.Context;
import android.util.Log;

import com.mate.bence.udalosti.Activity.Autentifikacia.Fragment.Prihlasenie;
import com.mate.bence.udalosti.Udaje.Data.SQLiteDatabaza;

import java.util.HashMap;

public class UvodnaObrazovkaUdaje implements ImplementaciaUvodnaObrazovka {

    private static final String TAG = UvodnaObrazovkaUdaje.class.getName();
    private SQLiteDatabaza databaza;

    UvodnaObrazovkaUdaje(Context context) {
        this.databaza = new SQLiteDatabaza(context);
    }

    @Override
    public boolean zistiCiPouzivatelskoKontoExistuje() {
        Log.v(TAG, "Metoda zistiCiPouzivatelskoKontoExistuje bola vykonana");

        return databaza.pouzivatelskeUdaje();
    }

    @Override
    public HashMap prihlasPouzivatela() {
        Log.v(TAG, "Metoda prihlasPouzivatela bola vykonana");

        return databaza.vratAktualnehoPouzivatela();
    }
}