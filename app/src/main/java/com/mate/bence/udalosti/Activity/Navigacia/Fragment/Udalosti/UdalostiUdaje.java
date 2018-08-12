package com.mate.bence.udalosti.Activity.Navigacia.Fragment.Udalosti;

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

public class UdalostiUdaje implements UdalostiImplementacia {

    private static final String TAG = UdalostiUdaje.class.getName();

    private Context context;
    private KommunikaciaData udajeZoServera;
    private KommunikaciaOdpoved odpovedeOdServera;

    public UdalostiUdaje(KommunikaciaData udajeZoServera, KommunikaciaOdpoved odpovedeOdServera, Context context) {
        this.udajeZoServera = udajeZoServera;
        this.odpovedeOdServera = odpovedeOdServera;
        this.context = context;
    }

    @Override
    public void zoznamUdalosti(String email, String idUdalost, String datum, String stat, int od, int pocet, String token) {
        Log.v(TAG, "Metoda zoznamUdalosti bola vykonana");

        Requesty requesty = UdalostiAdresa.initAdresu();
        requesty.udalosti(email, idUdalost, datum, stat, od, pocet, token).enqueue(new Callback<Zoznam>() {
            @Override
            public void onResponse(@NonNull Call<Zoznam> call, @NonNull Response<Zoznam> response) {
                if (response.isSuccessful()) {
                    udajeZoServera.dataZoServera(Status.VSETKO_V_PORIADKU, Status.UDALOSTI_OBJAVUJ, response.body().getUdalosti());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Zoznam> call, @NonNull Throwable t) {
                udajeZoServera.dataZoServera(context.getString(R.string.chyba_servera), Status.UDALOSTI_OBJAVUJ, null);
            }
        });
    }

    @Override
    public void zoznamNaplanovanychUdalosti(String email, int od, int pocet, String token) {
        Log.v(TAG, "Metoda zoznamNaplanovanychUdalosti bola vykonana");

        Requesty requesty = UdalostiAdresa.initAdresu();
        requesty.zoznamNaplanovanychUdalosti(email, od, pocet, token).enqueue(new Callback<Zoznam>() {
            @Override
            public void onResponse(@NonNull Call<Zoznam> call, @NonNull Response<Zoznam> response) {
                if (response.isSuccessful()) {
                    udajeZoServera.dataZoServera(Status.VSETKO_V_PORIADKU, Status.UDALOSTI_KALENDAR, response.body().getNaplanovaneUdalosti());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Zoznam> call, @NonNull Throwable t) {
                udajeZoServera.dataZoServera(context.getString(R.string.chyba_servera), Status.UDALOSTI_KALENDAR, null);
            }
        });
    }

    @Override
    public void zoznamOznameniZiadosti(String email, int od, int pocet, String token) {
        Log.v(TAG, "Metoda zoznamOznameniZiadosti bola vykonana");

        Requesty requesty = UdalostiAdresa.initAdresu();
        requesty.oznameniaZiadosti(email, od, pocet, token).enqueue(new Callback<Zoznam>() {
            @Override
            public void onResponse(@NonNull Call<Zoznam> call, @NonNull Response<Zoznam> response) {
                if (response.isSuccessful()) {
                    if (response.body().getOznamenia() != null) {
                        udajeZoServera.dataZoServera(Status.VSETKO_V_PORIADKU, Status.UDALOSTI_OZNAMENIA, response.body().getOznamenia());
                    } else {
                        udajeZoServera.dataZoServera(Status.VSETKO_V_PORIADKU, Status.UDALOSTI_OZNAMENIA, null);
                    }
                    if (response.body().getZiadosti() != null) {
                        udajeZoServera.dataZoServera(Status.VSETKO_V_PORIADKU, Status.UDALOSTI_ZIADOSTI, response.body().getZiadosti());
                    } else {
                        udajeZoServera.dataZoServera(Status.VSETKO_V_PORIADKU, Status.UDALOSTI_ZIADOSTI, null);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Zoznam> call, @NonNull Throwable t) {
                udajeZoServera.dataZoServera(context.getString(R.string.chyba_servera), Status.UDALOSTI_OZNAMENIA, null);
            }
        });
    }

    @Override
    public void hladanaUdalost(String email, String nazov, String stat, String token) {
        Log.v(TAG, "Metoda hladanaUdalost bola vykonana");

        Requesty requesty = UdalostiAdresa.initAdresu();
        requesty.hladanaUdalost(email, nazov, stat, token).enqueue(new Callback<Zoznam>() {
            @Override
            public void onResponse(@NonNull Call<Zoznam> call, @NonNull Response<Zoznam> response) {
                if (response.isSuccessful()) {
                    udajeZoServera.dataZoServera(Status.VSETKO_V_PORIADKU, Status.VYHLADAVANIE_UDALOSTI, response.body().getUdalosti());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Zoznam> call, @NonNull Throwable t) {
                udajeZoServera.dataZoServera(context.getString(R.string.chyba_servera), Status.VYHLADAVANIE_UDALOSTI, null);
            }
        });
    }

    @Override
    public void zaujemOudalost(String email, int idUdalost, String token) {
        Log.v(TAG, "Metoda zaujemOudalost bola vykonana");

        Requesty requesty = UdalostiAdresa.initAdresu();
        requesty.zaujem(email, UUID.randomUUID().toString(), idUdalost, token).enqueue(new Callback<Odpoved>() {
            @Override
            public void onResponse(@NonNull Call<Odpoved> call, @NonNull Response<Odpoved> response) {
                if (response.isSuccessful()) {
                    HashMap<String, String> udaje = new HashMap<>();
                    if (response.body().getChyba() != null) {
                        udaje.put("chyba", response.body().getChyba());
                        odpovedeOdServera.odpovedServera(Status.VSETKO_V_PORIADKU, Status.UDALOSTI_KALENDAR, udaje);
                    } else if (response.body().getUspech() != null) {
                        udaje.put("uspech", response.body().getUspech());
                        odpovedeOdServera.odpovedServera(Status.VSETKO_V_PORIADKU, Status.UDALOSTI_KALENDAR, udaje);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Odpoved> call, @NonNull Throwable t) {
                odpovedeOdServera.odpovedServera(context.getString(R.string.chyba_servera), Status.UDALOSTI_KALENDAR, null);
            }
        });
    }

    @Override
    public void odstranenieUdalostiKalendara(String email, int idZaujemUdalosti, String token) {
        Log.v(TAG, "Metoda odstranenieUdalostiKalendara bola vykonana");

        Requesty requesty = UdalostiAdresa.initAdresu();
        requesty.odstranenieZoZaujmov(email, idZaujemUdalosti, UUID.randomUUID().toString(), token).enqueue(new Callback<Odpoved>() {
            @Override
            public void onResponse(@NonNull Call<Odpoved> call, @NonNull Response<Odpoved> response) {
                if (response.isSuccessful()) {
                    HashMap<String, String> udaje = new HashMap<>();
                    if (response.body().getChyba() != null) {
                        udaje.put("chyba", response.body().getChyba());
                        odpovedeOdServera.odpovedServera(Status.VSETKO_V_PORIADKU, Status.ODSTRANENIE_UDALOSTI_ZO_ZAUJMOV, udaje);
                    } else if (response.body().getUspech() != null) {
                        udaje.put("uspech", response.body().getUspech());
                        odpovedeOdServera.odpovedServera(Status.VSETKO_V_PORIADKU, Status.ODSTRANENIE_UDALOSTI_ZO_ZAUJMOV, udaje);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Odpoved> call, @NonNull Throwable t) {
                odpovedeOdServera.odpovedServera(context.getString(R.string.chyba_servera), Status.ODSTRANENIE_UDALOSTI_ZO_ZAUJMOV, null);
            }
        });
    }

    @Override
    public void odpovedNaZiadost(String email, int odpoved, int idZiadost, String token) {
        Log.v(TAG, "Metoda odpovedNaZiadost bola vykonana");

        Requesty requesty = UdalostiAdresa.initAdresu();
        requesty.odpovedNaZiadost(email, odpoved, idZiadost, token).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.v(TAG, "Odpoved na ziadost bola spracovana");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                Log.v(TAG, "Pri odpovedi na ziadost nastala chyba");
            }
        });
    }
}