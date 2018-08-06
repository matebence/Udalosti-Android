package com.mate.bence.udalosti.Nastroje;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.mate.bence.udalosti.Activity.Udalosti.UdalostiUdaje;
import com.mate.bence.udalosti.Udaje.Nastavenia.Nastavenia;
import com.mate.bence.udalosti.Udaje.Siet.Model.KommunikaciaOdpoved;

import java.util.HashMap;

public class Odhlasenie extends Service implements KommunikaciaOdpoved {

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
        UdalostiUdaje udalostiUdaje = new UdalostiUdaje(this, getApplicationContext());
        udalostiUdaje.odhlasenie(email);
        stopSelf();
    }

    @Override
    public void odpovedServera(String odpoved, String od, HashMap<String, String> udaje) {
        switch (od) {
            case Nastavenia.AUTENTIFIKACIA_ODHLASENIE:
                if (odpoved.equals(Nastavenia.VSETKO_V_PORIADKU)) {
                    Log.v(TAG, "Systém odhlásil");
                }
        }
    }
}