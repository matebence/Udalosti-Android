package com.mate.bence.udalosti.Activity.Navigacia.Fragment.Ludia;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.mate.bence.udalosti.R;
import com.mate.bence.udalosti.Udaje.Nastavenia.Status;
import com.mate.bence.udalosti.Udaje.Siet.Model.KommunikaciaData;
import com.mate.bence.udalosti.Udaje.Siet.Model.KommunikaciaOdpoved;
import com.mate.bence.udalosti.Udaje.Siet.Model.Odpoved.Odpoved;
import com.mate.bence.udalosti.Udaje.Siet.Model.Zoznam.Zoznam;
import com.mate.bence.udalosti.Udaje.Siet.Requesty;
import com.mate.bence.udalosti.Udaje.Siet.UdalostiAdresa;

import java.util.HashMap;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LudiaUdaje implements LudiaImplementacia {

    private static final String TAG = LudiaUdaje.class.getName();

    private KommunikaciaData udajeZoServera;
    private KommunikaciaOdpoved odpovedeOdServera;
    private Context context;

    public LudiaUdaje(KommunikaciaData udajeZoServera, KommunikaciaOdpoved odpovedeOdServera, Context context) {
        this.udajeZoServera = udajeZoServera;
        this.odpovedeOdServera = odpovedeOdServera;
        this.context = context;
    }

    @Override
    public void zoznamPouzivatelov(String email, int od, int pocet, String token) {
        Log.v(TAG, "Metoda zoznamPouzivatelov bola vykonana");

        Requesty requesty = UdalostiAdresa.initAdresu();
        requesty.vsetci(email, od, pocet, token).enqueue(new Callback<Zoznam>() {
            @Override
            public void onResponse(@NonNull Call<Zoznam> call, @NonNull Response<Zoznam> response) {
                if (response.isSuccessful()) {
                    udajeZoServera.dataZoServera(Status.VSETKO_V_PORIADKU, Status.LUDIA_VSETCI, response.body().getLudia());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Zoznam> call, @NonNull Throwable t) {
                udajeZoServera.dataZoServera(context.getString(R.string.chyba_servera), Status.LUDIA_VSETCI, null);
            }
        });
    }

    @Override
    public void zoznamPriatelov(String email, int od, int pocet, String token) {
        Log.v(TAG, "Metoda zoznamPriatelov bola vykonana");

        Requesty requesty = UdalostiAdresa.initAdresu();
        requesty.priatelia(email, od, pocet, token).enqueue(new Callback<Zoznam>() {
            @Override
            public void onResponse(@NonNull Call<Zoznam> call, @NonNull Response<Zoznam> response) {
                if (response.isSuccessful()) {
                    udajeZoServera.dataZoServera(Status.VSETKO_V_PORIADKU, Status.LUDIA_PRIATELIA, response.body().getPriatelia());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Zoznam> call, @NonNull Throwable t) {
                udajeZoServera.dataZoServera(context.getString(R.string.chyba_servera), Status.LUDIA_PRIATELIA, null);
            }
        });
    }

    @Override
    public void hladanyPouzivatel(String email, String meno, String token) {
        Log.v(TAG, "Metoda hladanyPouzivatel bola vykonana");

        Requesty requesty = UdalostiAdresa.initAdresu();
        requesty.hladanyPouzivatel(email, meno, token).enqueue(new Callback<Zoznam>() {
            @Override
            public void onResponse(@NonNull Call<Zoznam> call, @NonNull Response<Zoznam> response) {
                if (response.isSuccessful()) {
                    udajeZoServera.dataZoServera(Status.VSETKO_V_PORIADKU, Status.VYHLADAVANIE_LUDI, response.body().getLudia());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Zoznam> call, @NonNull Throwable t) {
                udajeZoServera.dataZoServera(context.getString(R.string.chyba_servera), Status.VYHLADAVANIE_LUDI, null);
            }
        });
    }

    @Override
    public void novaZiadost(String email, int pouzivatel, String token) {
        Log.v(TAG, "Metoda novaZiadost bola vykonana");

        Requesty requesty = UdalostiAdresa.initAdresu();
        requesty.novaZiadost(email, pouzivatel, token, UUID.randomUUID().toString()).enqueue(new Callback<Odpoved>() {
            @Override
            public void onResponse(@NonNull Call<Odpoved> call, @NonNull Response<Odpoved> response) {
                if (response.isSuccessful()) {
                    HashMap<String, String> udaje = new HashMap<>();
                    if (response.body().getChyba() != null) {
                        udaje.put("chyba", response.body().getChyba());
                        odpovedeOdServera.odpovedServera(Status.VSETKO_V_PORIADKU, Status.UDALOSTI_ZIADOSTI, udaje);
                    } else if (response.body().getUspech() != null) {
                        udaje.put("uspech", response.body().getUspech());
                        odpovedeOdServera.odpovedServera(Status.VSETKO_V_PORIADKU, Status.UDALOSTI_ZIADOSTI, udaje);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Odpoved> call, @NonNull Throwable t) {
                odpovedeOdServera.odpovedServera(context.getString(R.string.chyba_servera), Status.UDALOSTI_ZIADOSTI, null);
            }
        });
    }

    @Override
    public void odstraneniePriatelstva(String email, int pouzivatel, String token) {
        Log.v(TAG, "Metoda odstraneniePriatelstva bola vykonana");

        Requesty requesty = UdalostiAdresa.initAdresu();
        requesty.odstraneniePriatelstva(email, pouzivatel, UUID.randomUUID().toString(), token).enqueue(new Callback<Odpoved>() {
            @Override
            public void onResponse(@NonNull Call<Odpoved> call, @NonNull Response<Odpoved> response) {
                if (response.isSuccessful()) {
                    HashMap<String, String> udaje = new HashMap<>();
                    if (response.body().getChyba() != null) {
                        udaje.put("chyba", response.body().getChyba());
                        odpovedeOdServera.odpovedServera(Status.VSETKO_V_PORIADKU, Status.ODSTRANENIE_PRIATELA, udaje);
                    } else if (response.body().getUspech() != null) {
                        udaje.put("uspech", response.body().getUspech());
                        odpovedeOdServera.odpovedServera(Status.VSETKO_V_PORIADKU, Status.ODSTRANENIE_PRIATELA, udaje);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Odpoved> call, @NonNull Throwable t) {
                odpovedeOdServera.odpovedServera(context.getString(R.string.chyba_servera), Status.ODSTRANENIE_PRIATELA, null);
            }
        });
    }
}