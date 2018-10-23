package com.mate.bence.udalosti.Activity.Autentifikacia;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.mate.bence.udalosti.R;
import com.mate.bence.udalosti.Udaje.Data.SQLiteDatabaza;
import com.mate.bence.udalosti.Udaje.Data.Tabulky.Miesto;
import com.mate.bence.udalosti.Udaje.Data.Tabulky.Pouzivatel;
import com.mate.bence.udalosti.Udaje.Nastavenia.Nastavenia;
import com.mate.bence.udalosti.Udaje.Siet.GeoAdresa;
import com.mate.bence.udalosti.Udaje.Siet.Model.Autentifikator.Autentifikator;
import com.mate.bence.udalosti.Udaje.Siet.Model.KommunikaciaOdpoved;
import com.mate.bence.udalosti.Udaje.Siet.Model.Pozicia.LocationIQ;
import com.mate.bence.udalosti.Udaje.Siet.Model.Pozicia.Pozicia;
import com.mate.bence.udalosti.Udaje.Siet.Requesty;
import com.mate.bence.udalosti.Udaje.Siet.UdalostiAdresa;

import java.util.HashMap;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AutentifikaciaUdaje implements AutentifikaciaImplementacia {

    private static final String TAG = AutentifikaciaUdaje.class.getName();

    private KommunikaciaOdpoved odpovedeOdServera;
    private SQLiteDatabaza databaza;

    private Context context;

    public AutentifikaciaUdaje(KommunikaciaOdpoved odpovedeOdServera, Context context) {
        this.odpovedeOdServera = odpovedeOdServera;
        this.context = context;
        this.databaza = new SQLiteDatabaza(context);
    }

    @Override
    public void miestoPrihlasenia(final String email, final String heslo, double zemepisnaSirka, double zemepisnaDlzka, boolean geo, final boolean aktualizuj) {
        Log.v(AutentifikaciaUdaje.TAG, "Metoda miestoPrihlasenia bola vykonana");

        Requesty requesty = GeoAdresa.initAdresu(geo);
        requesty.pozicia(Nastavenia.POZICIA_TOKEN, zemepisnaSirka, zemepisnaDlzka, Nastavenia.POZICIA_FORMAT, Nastavenia.POZICIA_JAZYK).enqueue(new Callback<LocationIQ>() {

            @Override
            public void onResponse(@NonNull Call<LocationIQ> call, @NonNull Response<LocationIQ> response) {
                GeoAdresa.initNanovo();
                String pozicia, okres, kraj, psc, stat, znakStatu;
                pozicia = okres = kraj = psc = stat = znakStatu = "";

                if (response.body() != null) {
                    if (response.body().getPozicia().getPozicia() != null) {
                        pozicia = response.body().getPozicia().getPozicia();
                    }
                    if (response.body().getPozicia().getOkres() != null) {
                        okres = response.body().getPozicia().getOkres();
                    }
                    if (response.body().getPozicia().getKraj() != null) {
                        kraj = response.body().getPozicia().getKraj();
                    }
                    if (response.body().getPozicia().getPsc() != null) {
                        psc = response.body().getPozicia().getPsc();
                    }
                    if (response.body().getPozicia().getStat() != null) {
                        stat = response.body().getPozicia().getStat();
                    }
                    if (response.body().getPozicia().getZnakStatu() != null) {
                        znakStatu = response.body().getPozicia().getZnakStatu();
                    }

                    if (databaza.miestoPrihlasenia()) {
                        databaza.aktualizujMiestoPrihlasenia(new Miesto(pozicia, okres, kraj, psc, stat, znakStatu));
                    } else {
                        databaza.noveMiestoPrihlasenia(new Miesto(pozicia, okres, kraj, psc, stat, znakStatu));
                    }
                }

                if (aktualizuj) {
                    odpovedeOdServera.odpovedServera(Nastavenia.VSETKO_V_PORIADKU, Nastavenia.UDALOSTI_AKTUALIZUJ, null);
                } else {
                    prihlasenie(email, heslo);
                }
            }

            @Override
            public void onFailure(@NonNull Call<LocationIQ> call, @NonNull Throwable t) {
                odpovedeOdServera.odpovedServera(context.getString(R.string.chyba_servera), Nastavenia.AUTENTIFIKACIA_PRIHLASENIE, null);
            }
        });
    }

    @Override
    public void miestoPrihlasenia(final String email, final String heslo, boolean ip) {
        Log.v(AutentifikaciaUdaje.TAG, "Metoda miestoPrihlasenia bola vykonana");

        Requesty requesty = GeoAdresa.initAdresu(ip);
        requesty.pozicia().enqueue(new Callback<Pozicia>() {

            @Override
            public void onResponse(@NonNull Call<Pozicia> call, @NonNull Response<Pozicia> response) {
                GeoAdresa.initNanovo();
                String stat = "";

                if (response.body() != null) {
                    if (response.body().getStat() != null) {
                        if ((response.body().getStat().equals("Slovakia")) || (response.body().getStat().equals("Slovak Republic")))
                            stat = "Slovensko";
                    }
                    if (databaza.miestoPrihlasenia()) {
                        databaza.aktualizujMiestoPrihlasenia(new Miesto(null, null, null, null, stat, null));
                    } else {
                        databaza.noveMiestoPrihlasenia(new Miesto(null, null, null, null, stat, null));
                    }
                }

                prihlasenie(email, heslo);
            }

            @Override
            public void onFailure(@NonNull Call<Pozicia> call, @NonNull Throwable t) {
                odpovedeOdServera.odpovedServera(context.getString(R.string.chyba_servera), Nastavenia.AUTENTIFIKACIA_PRIHLASENIE, null);
            }
        });
    }

    @Override
    public void prihlasenie(final String email, final String heslo) {
        Log.v(AutentifikaciaUdaje.TAG, "Metoda prihlasenie bola vykonana");

        Requesty requesty = UdalostiAdresa.initAdresu();
        requesty.prihlasenie(email, heslo, UUID.randomUUID().toString()).enqueue(new Callback<Autentifikator>() {

            @Override
            public void onResponse(@NonNull Call<Autentifikator> call, @NonNull Response<Autentifikator> response) {
                HashMap<String, String> udaje = new HashMap<>();
                assert response.body() != null;
                if (response.body().getChyba()) {
                    udaje.put("email", email);
                    if (response.body().getValidacia().getEmail() != null) {
                        odpovedeOdServera.odpovedServera(response.body().getValidacia().getEmail(), Nastavenia.AUTENTIFIKACIA_PRIHLASENIE, udaje);
                    } else if (response.body().getValidacia().getHeslo() != null) {
                        odpovedeOdServera.odpovedServera(response.body().getValidacia().getHeslo(), Nastavenia.AUTENTIFIKACIA_PRIHLASENIE, udaje);
                    } else if (response.body().getValidacia().getOznam() != null) {
                        odpovedeOdServera.odpovedServera(response.body().getValidacia().getOznam(), Nastavenia.AUTENTIFIKACIA_PRIHLASENIE, udaje);
                    }
                } else {
                    udaje.put("email", email);
                    udaje.put("heslo", heslo);
                    udaje.put("token", response.body().getPouzivatel().getToken());

                    odpovedeOdServera.odpovedServera(Nastavenia.VSETKO_V_PORIADKU, Nastavenia.AUTENTIFIKACIA_PRIHLASENIE, udaje);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Autentifikator> call, @NonNull Throwable t) {
                odpovedeOdServera.odpovedServera(context.getString(R.string.chyba_servera), Nastavenia.AUTENTIFIKACIA_PRIHLASENIE, null);
            }
        });
    }

    @Override
    public void registracia(String meno, String email, String heslo, String potvrd) {
        Log.v(AutentifikaciaUdaje.TAG, "Metoda registracia bola vykonana");

        Requesty requesty = UdalostiAdresa.initAdresu();
        requesty.registracia(meno, email, heslo, potvrd, UUID.randomUUID().toString()).enqueue(new Callback<Autentifikator>() {
            @Override
            public void onResponse(@NonNull Call<Autentifikator> call, @NonNull Response<Autentifikator> response) {
                GeoAdresa.initNanovo();

                assert response.body() != null;
                if (response.body().getChyba()) {
                    if (response.body().getValidacia().getOznam() != null) {
                        odpovedeOdServera.odpovedServera(response.body().getValidacia().getOznam(), Nastavenia.AUTENTIFIKACIA_REGISTRACIA, null);
                    } else if (response.body().getValidacia().getMeno() != null) {
                        odpovedeOdServera.odpovedServera(response.body().getValidacia().getMeno(), Nastavenia.AUTENTIFIKACIA_REGISTRACIA, null);
                    } else if (response.body().getValidacia().getEmail() != null) {
                        odpovedeOdServera.odpovedServera(response.body().getValidacia().getEmail(), Nastavenia.AUTENTIFIKACIA_REGISTRACIA, null);
                    } else if (response.body().getValidacia().getHeslo() != null) {
                        odpovedeOdServera.odpovedServera(response.body().getValidacia().getHeslo(), Nastavenia.AUTENTIFIKACIA_REGISTRACIA, null);
                    } else if (response.body().getValidacia().getPotvrd() != null) {
                        odpovedeOdServera.odpovedServera(response.body().getValidacia().getPotvrd(), Nastavenia.AUTENTIFIKACIA_REGISTRACIA, null);
                    }
                } else {
                    odpovedeOdServera.odpovedServera(Nastavenia.VSETKO_V_PORIADKU, Nastavenia.AUTENTIFIKACIA_REGISTRACIA, null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Autentifikator> call, @NonNull Throwable t) {
                odpovedeOdServera.odpovedServera(context.getString(R.string.chyba_servera), Nastavenia.AUTENTIFIKACIA_REGISTRACIA, null);
            }
        });
    }

    @Override
    public void ulozPrihlasovacieUdajeDoDatabazy(String email, String heslo) {
        Log.v(AutentifikaciaUdaje.TAG, "Metoda ulozPrihlasovacieUdajeDoDatabazy bola vykonana");

        if (this.databaza.pouzivatelskeUdaje()) {
            this.databaza.aktualizujPouzivatelskeUdaje(new Pouzivatel(email, heslo));
        } else {
            this.databaza.novePouzivatelskeUdaje(new Pouzivatel(email, heslo));
        }
    }

    @Override
    public void ucetJeNePristupny(String email) {
        Log.v(AutentifikaciaUdaje.TAG, "Metoda ucetJeNePristupny bola vykonana");

        this.databaza.odstranPouzivatelskeUdaje(email);
    }
}