package com.mate.bence.udalosti.Activity.Navigacia;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.mate.bence.udalosti.R;
import com.mate.bence.udalosti.Udaje.Data.SQLiteDatabaza;
import com.mate.bence.udalosti.Udaje.Nastavenia.Status;
import com.mate.bence.udalosti.Udaje.Siet.Model.Autentifikator.Autentifikator;
import com.mate.bence.udalosti.Udaje.Siet.Model.KommunikaciaOdpoved;
import com.mate.bence.udalosti.Udaje.Siet.Requesty;
import com.mate.bence.udalosti.Udaje.Siet.UdalostiAdresa;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NavigaciaUdaje implements NavigaciaImplementacia {

    private static final String TAG = NavigaciaUdaje.class.getName();

    private KommunikaciaOdpoved odpovedeOdServera;
    private SQLiteDatabaza databaza;
    private Context context;

    public NavigaciaUdaje(KommunikaciaOdpoved odpovedeOdServera, Context context) {
        this.odpovedeOdServera = odpovedeOdServera;
        this.databaza = new SQLiteDatabaza(context);
        this.context = context;
    }

    @Override
    public void odhlasenie(String email) {
        Log.v(TAG, "Metoda odhlasenie bola vykonana");

        Requesty requesty = UdalostiAdresa.initAdresu();
        requesty.odhlasenie(email).enqueue(new Callback<Autentifikator>() {

            @Override
            public void onResponse(@NonNull Call<Autentifikator> call, @NonNull Response<Autentifikator> response) {
                if (!(response.body().getChyba())) {
                    odpovedeOdServera.odpovedServera(Status.VSETKO_V_PORIADKU, Status.AUTENTIFIKACIA_ODHLASENIE, null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Autentifikator> call, @NonNull Throwable t) {
                odpovedeOdServera.odpovedServera(context.getString(R.string.chyba_servera), Status.AUTENTIFIKACIA_ODHLASENIE, null);
            }
        });
    }

    @Override
    public HashMap miestoPrihlasenia() {
        Log.v(TAG, "Metoda miestoPrihlasenia bola vykonana");

        HashMap<String, String> miestoPrihlasenia = databaza.vratMiestoPrihlasenia();
        if (miestoPrihlasenia != null) {
            return miestoPrihlasenia;
        } else {
            return null;
        }
    }

    @Override
    public void automatickePrihlasenieVypnute(String email) {
        Log.v(TAG, "Metoda automatickePrihlasenieVypnute bola vykonana");

        databaza.odstranPouzivatelskeUdaje(email);
    }
}