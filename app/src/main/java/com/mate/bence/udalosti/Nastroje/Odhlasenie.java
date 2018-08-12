package com.mate.bence.udalosti.Nastroje;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.mate.bence.udalosti.Activity.Navigacia.NavigaciaUdaje;
import com.mate.bence.udalosti.Udaje.Nastavenia.Status;
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
        NavigaciaUdaje navigaciaUdaje = new NavigaciaUdaje(this, getApplicationContext());
        navigaciaUdaje.odhlasenie(email);
        stopSelf();
    }

    @Override
    public void odpovedServera(String odpoved, String od, HashMap<String, String> udaje) {
        switch (od) {
            case Status.AUTENTIFIKACIA_ODHLASENIE:
                if (odpoved.equals(Status.VSETKO_V_PORIADKU)) {
                    Log.v(TAG, "Systém odhlásil");
                }
        }
    }
}