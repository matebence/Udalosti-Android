package com.mate.bence.udalosti.Activity.Udalosti;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.mate.bence.udalosti.R;
import com.mate.bence.udalosti.Udaje.Data.SQLiteDatabaza;
import com.mate.bence.udalosti.Udaje.Nastavenia.Nastavenia;
import com.mate.bence.udalosti.Udaje.Siet.Model.Akcia.Akcia;
import com.mate.bence.udalosti.Udaje.Siet.Model.Autentifikator.Autentifikator;
import com.mate.bence.udalosti.Udaje.Siet.Model.KommunikaciaData;
import com.mate.bence.udalosti.Udaje.Siet.Model.KommunikaciaOdpoved;
import com.mate.bence.udalosti.Udaje.Siet.Model.Obsah.Obsah;
import com.mate.bence.udalosti.Udaje.Siet.Requesty;
import com.mate.bence.udalosti.Udaje.Siet.UdalostiAdresa;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UdalostiUdaje implements UdalostiImplementacia {

    private static final String TAG = UdalostiUdaje.class.getName();

    private KommunikaciaOdpoved odpovedeOdServera;
    private KommunikaciaData udajeZoServera;
    private SQLiteDatabaza databaza;
    private Context context;

    public UdalostiUdaje(KommunikaciaOdpoved odpovedeOdServera, KommunikaciaData udajeZoServera, Context context) {
        this.odpovedeOdServera = odpovedeOdServera;
        this.udajeZoServera = udajeZoServera;
        this.databaza = new SQLiteDatabaza(context);
        this.context = context;
    }

    @Override
    public void zoznamUdalosti(String email, String stat, String token) {
        Log.v(TAG, "Metoda zoznamUdalosti bola vykonana");

        Requesty requesty = UdalostiAdresa.initAdresu();
        requesty.udalosti(email, stat, token).enqueue(new Callback<Obsah>() {
            @Override
            public void onResponse(@NonNull Call<Obsah> call, @NonNull Response<Obsah> response) {
                if (response.isSuccessful()) {
                    udajeZoServera.dataZoServera(Nastavenia.VSETKO_V_PORIADKU, Nastavenia.UDALOSTI_OBJAVUJ, response.body().getUdalosti());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Obsah> call, @NonNull Throwable t) {
                udajeZoServera.dataZoServera(context.getString(R.string.chyba_servera), Nastavenia.UDALOSTI_OBJAVUJ, null);
            }
        });
    }

    @Override
    public void zoznamUdalostiPodlaPozicie(String email, String stat, String okres, String mesto, String token) {
        Log.v(TAG, "Metoda zoznamUdalostiPodlaPozicie bola vykonana");

        Requesty requesty = UdalostiAdresa.initAdresu();
        requesty.udalostiPodlaPozicie(email, stat, okres, mesto, token).enqueue(new Callback<Obsah>() {
            @Override
            public void onResponse(@NonNull Call<Obsah> call, @NonNull Response<Obsah> response) {
                if (response.isSuccessful()) {
                    udajeZoServera.dataZoServera(Nastavenia.VSETKO_V_PORIADKU, Nastavenia.UDALOSTI_PODLA_POZICIE, response.body().getUdalosti());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Obsah> call, @NonNull Throwable t) {
                udajeZoServera.dataZoServera(context.getString(R.string.chyba_servera), Nastavenia.UDALOSTI_PODLA_POZICIE, null);
            }
        });
    }

    @Override
    public void automatickePrihlasenieVypnute(String email) {
        Log.v(TAG, "Metoda automatickePrihlasenieVypnute bola vykonana");

        databaza.odstranPouzivatelskeUdaje(email);
    }

    @Override
    public void odhlasenie(String email) {
        Log.v(TAG, "Metoda odhlasenie bola vykonana");

        Requesty requesty = UdalostiAdresa.initAdresu();
        requesty.odhlasenie(email).enqueue(new Callback<Autentifikator>() {

            @Override
            public void onResponse(@NonNull Call<Autentifikator> call, @NonNull Response<Autentifikator> response) {
                if (!(response.body().getChyba())) {
                    odpovedeOdServera.odpovedServera(Nastavenia.VSETKO_V_PORIADKU, Nastavenia.AUTENTIFIKACIA_ODHLASENIE, null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Autentifikator> call, @NonNull Throwable t) {
                odpovedeOdServera.odpovedServera(context.getString(R.string.chyba_servera), Nastavenia.AUTENTIFIKACIA_ODHLASENIE, null);
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
    public void zoznamZaujmov(String token, String email) {
        Log.v(TAG, "Metoda zoznamZaujmov bola vykonana");

        Requesty requesty = UdalostiAdresa.initAdresu();
        requesty.zaujmy(token, email).enqueue(new Callback<Obsah>() {
            @Override
            public void onResponse(Call<Obsah> call, Response<Obsah> response) {
                if (response.isSuccessful()) {
                }
            }

            @Override
            public void onFailure(Call<Obsah> call, Throwable t) {
                odpovedeOdServera.odpovedServera(context.getString(R.string.chyba_servera), Nastavenia.ZAUJEM_ZOZNAM, null);
            }
        });
    }

    @Override
    public void zaujemUdalost(String token, String email, int idUdalost) {
        Log.v(TAG, "Metoda zaujemUdalost bola vykonana");

        Requesty requesty = UdalostiAdresa.initAdresu();
        requesty.zaujem(token, email, idUdalost).enqueue(new Callback<Akcia>() {
            @Override
            public void onResponse(Call<Akcia> call, Response<Akcia> response) {
                if (response.isSuccessful()) {
                    HashMap<String, String> udaje = new HashMap<>();

                    if(response.body().getUspech() != null){
                        udaje.put("uspech", response.body().getUspech());
                        odpovedeOdServera.odpovedServera(Nastavenia.VSETKO_V_PORIADKU, Nastavenia.ZAUJEM, udaje);
                    }
                    if(response.body().getChyba() != null){
                        udaje.put("chyba", response.body().getChyba());
                        odpovedeOdServera.odpovedServera(Nastavenia.VSETKO_V_PORIADKU, Nastavenia.ZAUJEM, udaje);
                    }
                }
            }

            @Override
            public void onFailure(Call<Akcia> call, Throwable t) {
                odpovedeOdServera.odpovedServera(context.getString(R.string.chyba_servera), Nastavenia.ZAUJEM, null);
            }
        });
    }

    @Override
    public void potvrdZaujem(String token, String email, int idUdalost) {
        Log.v(TAG, "Metoda odstranZaujem bola vykonana");

        Requesty requesty = UdalostiAdresa.initAdresu();
        requesty.potvrd(token, email, idUdalost).enqueue(new Callback<Obsah>() {
            @Override
            public void onResponse(Call<Obsah> call, Response<Obsah> response) {
                if (response.isSuccessful()) {
                    udajeZoServera.dataZoServera(Nastavenia.VSETKO_V_PORIADKU, Nastavenia.ZAUJEM_POTVRD, response.body().getUdalosti());
                }
            }

            @Override
            public void onFailure(Call<Obsah> call, Throwable t) {
                odpovedeOdServera.odpovedServera(context.getString(R.string.chyba_servera), Nastavenia.ZAUJEM_POTVRD, null);
            }
        });
    }

    @Override
    public void odstranZaujem(String token, String email, int idUdalost) {
        Log.v(TAG, "Metoda potvrdZaujem bola vykonana");

        Requesty requesty = UdalostiAdresa.initAdresu();
        requesty.odstranZaujem(token, email, idUdalost).enqueue(new Callback<Akcia>() {
            @Override
            public void onResponse(Call<Akcia> call, Response<Akcia> response) {
                if (response.isSuccessful()) {
                    HashMap<String, String> udaje = new HashMap<>();

                    if(response.body().getUspech() != null){
                        udaje.put("uspech", response.body().getUspech());
                        odpovedeOdServera.odpovedServera(Nastavenia.VSETKO_V_PORIADKU, Nastavenia.ZAUJEM_ODSTRANENIE, udaje);
                    }
                    if(response.body().getChyba() != null){
                        udaje.put("chyba", response.body().getChyba());
                        odpovedeOdServera.odpovedServera(Nastavenia.VSETKO_V_PORIADKU, Nastavenia.ZAUJEM_ODSTRANENIE, udaje);
                    }
                }
            }

            @Override
            public void onFailure(Call<Akcia> call, Throwable t) {
                odpovedeOdServera.odpovedServera(context.getString(R.string.chyba_servera), Nastavenia.ZAUJEM_ODSTRANENIE, null);
            }
        });
    }
}