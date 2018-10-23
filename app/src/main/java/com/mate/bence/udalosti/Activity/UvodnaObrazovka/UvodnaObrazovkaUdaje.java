package com.mate.bence.udalosti.Activity.UvodnaObrazovka;

import android.content.Context;
import android.util.Log;

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
        Log.v(UvodnaObrazovkaUdaje.TAG, "Metoda zistiCiPouzivatelskoKontoExistuje bola vykonana");

        return this.databaza.pouzivatelskeUdaje();
    }

    @Override
    public HashMap prihlasPouzivatela() {
        Log.v(UvodnaObrazovkaUdaje.TAG, "Metoda prihlasPouzivatela bola vykonana");

        return this.databaza.vratAktualnehoPouzivatela();
    }
}