package com.mate.bence.udalosti.Dialog.Zoznam;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.mate.bence.udalosti.R;
import com.mate.bence.udalosti.Udaje.Nastavenia.Status;
import com.mate.bence.udalosti.Udaje.Siet.Model.KommunikaciaOdpoved;
import com.mate.bence.udalosti.Udaje.Siet.Model.Odpoved.Odpoved;
import com.mate.bence.udalosti.Udaje.Siet.Requesty;
import com.mate.bence.udalosti.Udaje.Siet.UdalostiAdresa;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DialogZoznamUdaje implements DialogZoznamImplementacia {

    private static final String TAG = DialogZoznamUdaje.class.getName();

    private KommunikaciaOdpoved odpovedeOdServera;
    private Context context;

    DialogZoznamUdaje(KommunikaciaOdpoved odpovedeOdServera, Context context) {
        this.odpovedeOdServera = odpovedeOdServera;
        this.context = context;
    }

    @Override
    public void novaPozvanka(String email, int idPouzivatel, int idUdalost, String token, String pozvanka) {
        Log.v(TAG, "Metoda novaPozvanka bola vykonana");

        Requesty requesty = UdalostiAdresa.initAdresu();
        requesty.pozvat(email, idPouzivatel, idUdalost, token, pozvanka).enqueue(new Callback<Odpoved>() {
            @Override
            public void onResponse(@NonNull Call<Odpoved> call, @NonNull Response<Odpoved> response) {
                if (response.isSuccessful()) {
                    HashMap<String, String> udaje = new HashMap<>();
                    if (response.body().getChyba() != null) {
                        udaje.put("chyba", response.body().getChyba());
                        odpovedeOdServera.odpovedServera(Status.VSETKO_V_PORIADKU, Status.UDALOSTI_OZNAMENIA, udaje);
                    } else if (response.body().getUspech() != null) {
                        udaje.put("uspech", response.body().getUspech());
                        odpovedeOdServera.odpovedServera(Status.VSETKO_V_PORIADKU, Status.UDALOSTI_OZNAMENIA, udaje);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<Odpoved> call, @NonNull Throwable t) {
                odpovedeOdServera.odpovedServera(context.getString(R.string.chyba_servera), Status.UDALOSTI_OZNAMENIA, null);
            }
        });
    }
}