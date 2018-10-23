package com.mate.bence.udalosti.Nastroje;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.mate.bence.udalosti.Activity.Udalosti.UdalostiUdaje;
import com.mate.bence.udalosti.Udaje.Nastavenia.Nastavenia;
import com.mate.bence.udalosti.Udaje.Siet.Model.KommunikaciaData;
import com.mate.bence.udalosti.Udaje.Siet.Model.KommunikaciaOdpoved;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class Odhlasenie extends Service implements KommunikaciaOdpoved, KommunikaciaData {

    private static final String TAG = Odhlasenie.class.getName();
    private String email;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        this.email = intent.getStringExtra("email");
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        UdalostiUdaje udalostiUdaje = new UdalostiUdaje(this, this, getApplicationContext());
        udalostiUdaje.odhlasenie(this.email);

        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            Log.v(Odhlasenie.TAG, "Pri odhlasovaní pomocou systému nastala chyba");
            e.printStackTrace();
        }
    }

    @Override
    public void odpovedServera(String odpoved, String od, HashMap<String, String> udaje) {
        Log.v(Odhlasenie.TAG, "Metoda odpovedServera - Odhlasenie bola vykonana");

        switch (od) {
            case Nastavenia.AUTENTIFIKACIA_ODHLASENIE:
                if (odpoved.equals(Nastavenia.VSETKO_V_PORIADKU)) {
                    stopSelf();
                    Log.v(Odhlasenie.TAG, "Systém odhlásil");
                }
                break;
        }
    }

    @Override
    public void dataZoServera(String odpoved, String od, ArrayList udaje) {
    }
}