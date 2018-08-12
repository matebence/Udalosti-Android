package com.mate.bence.udalosti.Dialog.PodrobnostiUdalosti;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.mate.bence.udalosti.R;
import com.mate.bence.udalosti.Udaje.Nastavenia.Status;
import com.mate.bence.udalosti.Udaje.Siet.Model.KommunikaciaData;
import com.mate.bence.udalosti.Udaje.Siet.Model.Zoznam.Zoznam;
import com.mate.bence.udalosti.Udaje.Siet.Requesty;
import com.mate.bence.udalosti.Udaje.Siet.UdalostiAdresa;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DialogPodrobnostiUdalostiUdaje implements DialogPodrobnostiUdalostiImplementacia {

    private static final String TAG = DialogPodrobnostiUdalostiUdaje.class.getName();

    private Context context;
    private KommunikaciaData udajeZoServera;

    DialogPodrobnostiUdalostiUdaje(Context context, KommunikaciaData udajeZoServera) {
        this.context = context;
        this.udajeZoServera = udajeZoServera;
    }

    @Override
    public void podrobnostiUdalosti(String email, int idUdalost, String token) {
        Log.v(TAG, "Metoda podrobnostiUdalosti bola vykonana");

        Requesty requesty = UdalostiAdresa.initAdresu();
        requesty.podrobnostiUdalosti(email, idUdalost, token).enqueue(new Callback<Zoznam>() {
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
}