package com.mate.bence.udalosti.Activity.Navigacia.Fragment.Nastavenia;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.mate.bence.udalosti.R;
import com.mate.bence.udalosti.Udaje.Data.SQLiteDatabaza;
import com.mate.bence.udalosti.Udaje.Data.Tabulky.Prihlasenie;
import com.mate.bence.udalosti.Udaje.Nastavenia.Status;
import com.mate.bence.udalosti.Udaje.Siet.Model.Autentifikator.Autentifikator;
import com.mate.bence.udalosti.Udaje.Siet.Model.KommunikaciaOdpoved;
import com.mate.bence.udalosti.Udaje.Siet.Model.Odpoved.Odpoved;
import com.mate.bence.udalosti.Udaje.Siet.Requesty;
import com.mate.bence.udalosti.Udaje.Siet.UdalostiAdresa;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NastaveniaUdaje implements NastaveniaImplementacia {

    private static final String TAG = NastaveniaUdaje.class.getName();

    private KommunikaciaOdpoved odpovedeOdServera;
    private SQLiteDatabaza databaza;
    private Context context;

    NastaveniaUdaje(KommunikaciaOdpoved odpovedeOdServera, Context context) {
        this.odpovedeOdServera = odpovedeOdServera;
        this.databaza = new SQLiteDatabaza(context);
        this.context = context;
    }

    @Override
    public void aktualizujPouzivatelskeKonto(String email, final String meno, String heslo, String potvrdenie, final Uri obrazok, String token, String nahodnyRetazec) {
        Log.v(TAG, "Metoda aktualizujPouzivatelskeKonto bola vykonana");

        final HashMap<String, String> udaje = new HashMap<>();
        String vstup[] = {email, meno, heslo, potvrdenie, token, nahodnyRetazec};
        RequestBody udajeBody[] = new RequestBody[vstup.length];
        MultipartBody.Part obrazokProfilu;

        for (int i = 0; i < vstup.length; i++) {
            if (vstup[i] != null) {
                udajeBody[i] = RequestBody.create(MediaType.parse("text/plain"), vstup[i]);
            } else {
                udajeBody[i] = null;
            }
        }

        if (obrazok != null) {
            RequestBody typSuboru = RequestBody.create(MediaType.parse("image/*"), new File(obrazok.getPath()));
            obrazokProfilu = MultipartBody.Part.createFormData("profil", new File(obrazok.getPath()).getName(), typSuboru);
        } else {
            obrazokProfilu = null;
        }

        Requesty requesty = UdalostiAdresa.initAdresu();
        requesty.aktualizacia(udajeBody[0], udajeBody[1], udajeBody[2], udajeBody[3], obrazokProfilu, udajeBody[4], udajeBody[5]).enqueue(new Callback<Autentifikator>() {

            @Override
            public void onResponse(@NonNull Call<Autentifikator> call, @NonNull Response<Autentifikator> response) {
                if (response.body().getChyba()) {
                    if (response.body().getValidacia().getOznam() != null) {
                        odpovedeOdServera.odpovedServera(response.body().getValidacia().getOznam(), Status.NASTAVENIA, null);
                    } else if (response.body().getValidacia().getMeno() != null) {
                        odpovedeOdServera.odpovedServera(response.body().getValidacia().getMeno(), Status.NASTAVENIA, null);
                    } else if (response.body().getValidacia().getHeslo() != null) {
                        odpovedeOdServera.odpovedServera(response.body().getValidacia().getHeslo(), Status.NASTAVENIA, null);
                    } else if (response.body().getValidacia().getPotvrd() != null) {
                        odpovedeOdServera.odpovedServera(response.body().getValidacia().getPotvrd(), Status.NASTAVENIA, null);
                    }
                } else {
                    if (obrazok != null) {
                        new File(context.getCacheDir(), fotkaPouzivatela()).delete();
                    }
                    if (obrazok != null) {
                        HashMap<String, String> pouzivatel = databaza.vratAktualnehoPouzivatela();
                        databaza.aktualizujPouzivatelskeUdaje(new Prihlasenie(pouzivatel.get("email"), pouzivatel.get("heslo"), pouzivatel.get("meno"), obrazok.toString().substring(obrazok.toString().lastIndexOf("/") + 1)));
                    }

                    udaje.put("meno", meno);
                    odpovedeOdServera.odpovedServera(Status.VSETKO_V_PORIADKU, Status.NASTAVENIA, udaje);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Autentifikator> call, @NonNull Throwable t) {
                odpovedeOdServera.odpovedServera(context.getString(R.string.chyba_servera), Status.NASTAVENIA, null);
            }
        });
    }

    @Override
    public String fotkaPouzivatela() {
        Log.v(TAG, "Metoda fotkaPouzivatela bola vykonana");

        return databaza.vratAktualnehoPouzivatela().get("obrazok");
    }

    @Override
    public void odstranPouzivatelskeKonto(final String email, String token) {
        Log.v(TAG, "Metoda odstranPouzivatelskeKonto bola vykonana");

        Requesty requesty = UdalostiAdresa.initAdresu();
        requesty.odstranenieUctu(email, UUID.randomUUID().toString(), token).enqueue(new Callback<Odpoved>() {
            @Override
            public void onResponse(@NonNull Call<Odpoved> call, @NonNull Response<Odpoved> response) {
                if (response.isSuccessful()) {
                    databaza.odstranPouzivatelskeUdaje(email);
                    if (response.body().getChyba() != null) {
                        odpovedeOdServera.odpovedServera(response.body().getChyba(), Status.NASTALA_CHYBA, null);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Odpoved> call, @NonNull Throwable t) {
                odpovedeOdServera.odpovedServera(context.getString(R.string.chyba_servera), Status.ODSTRANENIE_UCTU, null);
            }
        });
    }

    @Override
    public String hesloPrePotvrdenie() {
        Log.v(TAG, "Metoda hesloPrePotvrdenie bola vykonana");

        HashMap<String, String> hesloPouzivatela = databaza.vratAktualnehoPouzivatela();
        return hesloPouzivatela.get("heslo");
    }
}