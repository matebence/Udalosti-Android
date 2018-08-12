package com.mate.bence.udalosti.Activity.Navigacia.Fragment.PodlaPozicie;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.mate.bence.udalosti.Activity.Navigacia.Fragment.Udalosti.UdalostiUdaje;
import com.mate.bence.udalosti.R;
import com.mate.bence.udalosti.Udaje.Nastavenia.Status;
import com.mate.bence.udalosti.Udaje.Siet.Model.KommunikaciaData;
import com.mate.bence.udalosti.Udaje.Siet.Model.KommunikaciaOdpoved;
import com.mate.bence.udalosti.Udaje.Siet.Model.Zoznam.Zoznam;
import com.mate.bence.udalosti.Udaje.Siet.Requesty;
import com.mate.bence.udalosti.Udaje.Siet.UdalostiAdresa;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PodlaPozicieUdaje extends UdalostiUdaje implements PodlaPozicieImplementacia {

    private static final String TAG = PodlaPozicieUdaje.class.getName();

    private Context context;
    private KommunikaciaData udajeZoServera;

    PodlaPozicieUdaje(KommunikaciaData udajeZoServera, KommunikaciaOdpoved odpovedeOdServera, Context context) {
        super(udajeZoServera, odpovedeOdServera, context);
        this.context = context;
        this.udajeZoServera = udajeZoServera;
    }

    @Override
    public void zoznamUdalostiPodlaPozicie(String email, String idUdalost, String datum, String stat, String okres, String mesto, String token) {
        Log.v(TAG, "Metoda zoznamUdalostiPodlaPozicie bola vykonana");

        Requesty requesty = UdalostiAdresa.initAdresu();
        requesty.udalostiPodlaPozicie(email, idUdalost, datum, stat, okres, mesto, token).enqueue(new Callback<Zoznam>() {
            @Override
            public void onResponse(@NonNull Call<Zoznam> call, @NonNull Response<Zoznam> response) {
                if (response.isSuccessful()) {
                    udajeZoServera.dataZoServera(Status.VSETKO_V_PORIADKU, Status.PODLA_POZICIE, response.body().getUdalosti());
                }
            }

            @Override
            public void onFailure(@NonNull Call<Zoznam> call, @NonNull Throwable t) {
                udajeZoServera.dataZoServera(context.getString(R.string.chyba_servera), Status.PODLA_POZICIE, null);
            }
        });
    }
}