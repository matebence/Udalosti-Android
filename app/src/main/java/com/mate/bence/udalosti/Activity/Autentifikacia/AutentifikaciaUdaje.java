package com.mate.bence.udalosti.Activity.Autentifikacia;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.mate.bence.udalosti.Udaje.Data.Preferencie;
import com.mate.bence.udalosti.Udaje.Data.SQLiteDatabaza;
import com.mate.bence.udalosti.Udaje.Data.Tabulky.Miesto;
import com.mate.bence.udalosti.Udaje.Data.Tabulky.Prihlasenie;
import com.mate.bence.udalosti.Udaje.Nastavenia.Status;
import com.mate.bence.udalosti.Udaje.Siet.GeoAdresa;
import com.mate.bence.udalosti.Udaje.Siet.Model.Autentifikator.Autentifikator;
import com.mate.bence.udalosti.Udaje.Siet.Model.KommunikaciaOdpoved;
import com.mate.bence.udalosti.Udaje.Siet.Model.Pozicia.Pozicia;
import com.mate.bence.udalosti.Udaje.Siet.UdalostiAdresa;
import com.mate.bence.udalosti.Udaje.Siet.Requesty;
import com.mate.bence.udalosti.R;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
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
    public void miestoPrihlasenia(final String email, final String heslo) {
        Log.v(TAG, "Metoda miestoPrihlasenia bola vykonana");

        Requesty requesty = GeoAdresa.initAdresu();
        requesty.pozicia().enqueue(new Callback<Pozicia>() {

            @Override
            public void onResponse(@NonNull Call<Pozicia> call, @NonNull Response<Pozicia> response) {
                GeoAdresa.initNanovo();
                String stat, okres, mesto;
                stat = okres = mesto = "";

                if (response.body() != null) {
                    if (response.body().getStat() != null) {
                        stat = response.body().getStat();
                    }
                    if (response.body().getOkres() != null) {
                        okres = response.body().getOkres();
                    }
                    if (response.body().getMesto() != null) {
                        mesto = response.body().getMesto();
                    }

                    if (databaza.miestoPrihlasenia()) {
                        databaza.aktualizujMiestoPrihlasenie(new Miesto(stat, okres, mesto));
                    } else {
                        databaza.noveMiestoPrihlasenia(new Miesto(stat, okres, mesto));
                    }
                }
                prihlasenie(email, heslo, stat, okres, mesto);
            }

            @Override
            public void onFailure(@NonNull Call<Pozicia> call, @NonNull Throwable t) {
                odpovedeOdServera.odpovedServera(context.getString(R.string.chyba_servera), Status.AUTENTIFIKACIA_PRIHLASENIE, null);
            }
        });
    }

    @Override
    public void prihlasenie(final String email, final String heslo, String stat, String okres, String mesto) {
        Log.v(TAG, "Metoda prihlasenie bola vykonana");

        Requesty requesty = UdalostiAdresa.initAdresu();
        requesty.prihlasenie(email, heslo, UUID.randomUUID().toString()).enqueue(new Callback<Autentifikator>() {

            @Override
            public void onResponse(@NonNull Call<Autentifikator> call, @NonNull Response<Autentifikator> response) {
                HashMap<String, String> udaje = new HashMap<>();
                if (response.body().getChyba()) {
                    udaje.put("email", email);
                    if (response.body().getValidacia().getEmail() != null) {
                        odpovedeOdServera.odpovedServera(response.body().getValidacia().getEmail(), Status.AUTENTIFIKACIA_PRIHLASENIE, udaje);
                    } else if (response.body().getValidacia().getHeslo() != null) {
                        odpovedeOdServera.odpovedServera(response.body().getValidacia().getHeslo(), Status.AUTENTIFIKACIA_PRIHLASENIE, udaje);
                    } else if (response.body().getValidacia().getOznam() != null) {
                        odpovedeOdServera.odpovedServera(response.body().getValidacia().getOznam(), Status.AUTENTIFIKACIA_PRIHLASENIE, udaje);
                    }
                } else {
                    if (response.body().getPouzivatel().getObrazok() != null) {
                        if (!(response.body().getPouzivatel().getObrazok().equals("default")) && zistiCiObrazokJeUlozeny(response.body().getPouzivatel().getObrazok())) {
                            ulozObrazok(response.body().getPouzivatel().getObrazok());
                        }

                        String obrazok = response.body().getPouzivatel().getObrazok();
                        obrazok = obrazok.substring(obrazok.lastIndexOf("/") + 1);

                        udaje.put("email", email);
                        udaje.put("meno", response.body().getPouzivatel().getMeno());
                        udaje.put("obrazok", obrazok);
                        udaje.put("heslo", heslo);
                        udaje.put("token", response.body().getPouzivatel().getToken());

                        odpovedeOdServera.odpovedServera(Status.VSETKO_V_PORIADKU, Status.AUTENTIFIKACIA_PRIHLASENIE, udaje);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Autentifikator> call, @NonNull Throwable t) {
                odpovedeOdServera.odpovedServera(context.getString(R.string.chyba_servera), Status.AUTENTIFIKACIA_PRIHLASENIE, null);
            }
        });
    }

    @Override
    public void registracia(final Uri obrazok, String meno, String email, String heslo, String potvrd, String pohlavie, String kluc) {
        Log.v(TAG, "Metoda registracia bola vykonana");

        final MultipartBody.Part obrazokProfilu;
        if (obrazok == null) {
            obrazokProfilu = null;
        } else {
            RequestBody typSuboru = RequestBody.create(MediaType.parse("image/*"), new File(obrazok.getPath()));
            obrazokProfilu = MultipartBody.Part.createFormData("profil", new File(obrazok.getPath()).getName(), typSuboru);
        }

        RequestBody nahodnyRetazecBody = RequestBody.create(MediaType.parse("text/plain"), UUID.randomUUID().toString());
        RequestBody menoBody = RequestBody.create(MediaType.parse("text/plain"), meno);
        RequestBody emailBody = RequestBody.create(MediaType.parse("text/plain"), email);
        RequestBody hesloBody = RequestBody.create(MediaType.parse("text/plain"), heslo);
        RequestBody potvrdBody = RequestBody.create(MediaType.parse("text/plain"), potvrd);
        RequestBody pohlavieBody = RequestBody.create(MediaType.parse("text/plain"), pohlavie);
        RequestBody klucBody = RequestBody.create(MediaType.parse("text/plain"), kluc);

        Requesty requesty = UdalostiAdresa.initAdresu();
        requesty.registracia(menoBody, emailBody, hesloBody, potvrdBody, pohlavieBody, klucBody, obrazokProfilu, nahodnyRetazecBody).enqueue(new Callback<Autentifikator>() {

            @Override
            public void onResponse(@NonNull Call<Autentifikator> call, @NonNull Response<Autentifikator> response) {
                if (response.body().getChyba()) {
                    if (response.body().getValidacia().getOznam() != null) {
                        odpovedeOdServera.odpovedServera(response.body().getValidacia().getOznam(), Status.AUTENTIFIKACIA_REGISTRACIA, null);
                    } else if (response.body().getValidacia().getMeno() != null) {
                        odpovedeOdServera.odpovedServera(response.body().getValidacia().getMeno(), Status.AUTENTIFIKACIA_REGISTRACIA, null);
                    } else if (response.body().getValidacia().getEmail() != null) {
                        odpovedeOdServera.odpovedServera(response.body().getValidacia().getEmail(), Status.AUTENTIFIKACIA_REGISTRACIA, null);
                    } else if (response.body().getValidacia().getHeslo() != null) {
                        odpovedeOdServera.odpovedServera(response.body().getValidacia().getHeslo(), Status.AUTENTIFIKACIA_REGISTRACIA, null);
                    } else if (response.body().getValidacia().getPotvrd() != null) {
                        odpovedeOdServera.odpovedServera(response.body().getValidacia().getPotvrd(), Status.AUTENTIFIKACIA_REGISTRACIA, null);
                    } else if (response.body().getValidacia().getPohlavie() != null) {
                        odpovedeOdServera.odpovedServera(response.body().getValidacia().getPohlavie(), Status.AUTENTIFIKACIA_REGISTRACIA, null);
                    } else if (response.body().getValidacia().getIdTelefonu() != null) {
                        odpovedeOdServera.odpovedServera(response.body().getValidacia().getIdTelefonu(), Status.AUTENTIFIKACIA_REGISTRACIA, null);
                    }
                } else {
                    if (obrazok != null) {
                        new File(obrazok.getPath()).delete();
                    }
                    odpovedeOdServera.odpovedServera(Status.VSETKO_V_PORIADKU, Status.AUTENTIFIKACIA_REGISTRACIA, null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Autentifikator> call, @NonNull Throwable t) {
                odpovedeOdServera.odpovedServera(context.getString(R.string.chyba_servera), Status.AUTENTIFIKACIA_REGISTRACIA, null);
            }
        });
    }

    @Override
    public void ulozObrazok(final String nazovObrazka) {
        Log.v(TAG, "Metoda ulozObrazok bola vykonana");

        Requesty requesty = UdalostiAdresa.initAdresu();
        requesty.obrazok(UdalostiAdresa.getAdresa() + "udalosti" + "/" + nazovObrazka).enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Bitmap obrazok = BitmapFactory.decodeStream(response.body().byteStream());
                    try {
                        FileOutputStream ulozenieObrazka =
                                new FileOutputStream(new File(context.getCacheDir(), nazovObrazka.substring(nazovObrazka.lastIndexOf("/") + 1)));
                        obrazok.compress(Bitmap.CompressFormat.JPEG, 75, ulozenieObrazka);
                        ulozenieObrazka.close();
                    } catch (Exception e) {
                        Log.v(TAG, "Pri uloženie obrázka nastala chyba!");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Log.v(TAG, "Adresa obrázka je nedostupná");
            }
        });
    }

    @Override
    public boolean zistiCiObrazokJeUlozeny(String nazovObrazka) {
        Log.v(TAG, "Metoda zistiCiObrazokJeUlozeny bola vykonana");

        File obrazok = new File(context.getCacheDir(), nazovObrazka.substring(nazovObrazka.lastIndexOf("/") + 1));
        return !obrazok.exists();
    }

    @Override
    public void zabudnuteHeslo(final String email) {
        Log.v(TAG, "Metoda zabudnuteHeslo bola vykonana");

        Requesty requesty = UdalostiAdresa.initAdresu();
        requesty.zabudnuteHeslo(email, UUID.randomUUID().toString()).enqueue(new Callback<Autentifikator>() {

            @Override
            public void onResponse(@NonNull Call<Autentifikator> call, @NonNull Response<Autentifikator> response) {
                if (response.body().getChyba()) {
                    if (response.body().getValidacia().getEmail() != null) {
                        odpovedeOdServera.odpovedServera(response.body().getValidacia().getEmail(), Status.AUTENTIFIKACIA_ZABUDNUTE_HESLO, null);
                    } else if (response.body().getValidacia().getOznam() != null) {
                        odpovedeOdServera.odpovedServera(response.body().getValidacia().getOznam(), Status.AUTENTIFIKACIA_ZABUDNUTE_HESLO, null);
                    }
                } else {
                    HashMap<String, String> udaje = new HashMap<>();
                    udaje.put("email", email);
                    odpovedeOdServera.odpovedServera(Status.VSETKO_V_PORIADKU, Status.AUTENTIFIKACIA_ZABUDNUTE_HESLO, udaje);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Autentifikator> call, @NonNull Throwable t) {
                odpovedeOdServera.odpovedServera(context.getString(R.string.chyba_servera), Status.AUTENTIFIKACIA_ZABUDNUTE_HESLO, null);
            }
        });
    }

    @Override
    public void ulozPrihlasovacieUdajeDoDatabazy(String email, String heslo, String meno, String obrazok) {
        Log.v(TAG, "Metoda ulozPrihlasovacieUdajeDoDatabazy bola vykonana");

        if (databaza.pouzivatelskeUdaje()) {
            databaza.aktualizujPouzivatelskeUdaje(new Prihlasenie(email, heslo, meno, obrazok));
        } else {
            databaza.novePouzivatelskeUdaje(new Prihlasenie(email, heslo, meno, obrazok));
        }
    }

    @Override
    public void ucetJeNePristupny(String email) {
        Log.v(TAG, "Metoda ucetJeNePristupny bola vykonana");

        databaza.odstranPouzivatelskeUdaje(email);
    }

    @Override
    public String registracneCisloZariadeniu() {
        Log.v(TAG, "Metoda registracneCisloZariadeniu bola vykonana");

        String kluc = new Preferencie(context).vratRegistracneCislo();
        if (TextUtils.isEmpty(kluc)) {
            return null;
        } else {
            return kluc;
        }
    }
}