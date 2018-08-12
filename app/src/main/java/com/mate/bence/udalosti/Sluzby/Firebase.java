package com.mate.bence.udalosti.Sluzby;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.mate.bence.udalosti.Activity.UvodnaObrazovka.UvodnaObrazovka;
import com.mate.bence.udalosti.Nastroje.Notifikacie;
import com.mate.bence.udalosti.Udaje.Data.Preferencie;
import com.mate.bence.udalosti.Udaje.Nastavenia.Spravy;

import org.json.JSONException;
import org.json.JSONObject;

public class Firebase extends FirebaseMessagingService {

    private static final String TAG = Firebase.class.getSimpleName();
    private Notifikacie notifikacie;

    @Override
    public void onMessageReceived(RemoteMessage spravy) {
        if (spravy == null) {
            return;
        }

        if (spravy.getNotification() != null) {
            spracujNotifikaciu(spravy.getNotification().getBody());
        }

        if (spravy.getData().size() > 0) {
            try {
                JSONObject udaje = new JSONObject(spravy.getData().toString());
                spracujSpavyZoServera(udaje);
            } catch (Exception e) {
                Log.e(TAG, "Firebase chyba " + e.getMessage());
            }
        }
    }

    @Override
    public void onNewToken(String kluc) {
        super.onNewToken(kluc);

        new Preferencie(getApplicationContext()).ulozRegistracneCislo(kluc);

        Intent uspesna_Registracia = new Intent(Spravy.USPESNA_REGISTRACIA);
        uspesna_Registracia.putExtra("token", kluc);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(uspesna_Registracia);
    }

    private void spracujNotifikaciu(String sprava) {
        if (!Notifikacie.aplikacieJePozadi(getApplicationContext())) {
            Intent posliNotifikaciu = new Intent(Spravy.POSLI_NOTIFIKACIU);
            posliNotifikaciu.putExtra("notifikacna_sprava", sprava);
            LocalBroadcastManager.getInstance(this).sendBroadcast(posliNotifikaciu);

            Notifikacie notifikacie = new Notifikacie(getApplicationContext());
            notifikacie.prehrajZvuk();
        }
    }

    private void spracujSpavyZoServera(JSONObject json) {
        try {
            JSONObject oznamenie = json.getJSONObject("oznamenie");

            String idVztah = oznamenie.getString("id_vztah");

            String meno = oznamenie.getString("meno");
            String obrazok = oznamenie.getString("obrazok");

            String idUdalost = oznamenie.getString("id_udalost");
            String udalost = oznamenie.getString("udalost");
            String nazov = oznamenie.getString("nazov");
            String mesto = oznamenie.getString("mesto");

            String precitana = oznamenie.getString("precitana");
            String cas = oznamenie.getString("timestamp");


            if (!Notifikacie.aplikacieJePozadi(getApplicationContext())) {
                Intent posliNotifikaciu = new Intent(Spravy.POSLI_NOTIFIKACIU);

                posliNotifikaciu.putExtra("idVztah", idVztah);

                posliNotifikaciu.putExtra("meno", meno);
                posliNotifikaciu.putExtra("obrazok", obrazok);

                posliNotifikaciu.putExtra("idUdalost", idUdalost);
                posliNotifikaciu.putExtra("nazov", nazov);
                posliNotifikaciu.putExtra("mesto", mesto);

                posliNotifikaciu.putExtra("precitana", precitana);

                LocalBroadcastManager.getInstance(this).sendBroadcast(posliNotifikaciu);
                Notifikacie notifikacie = new Notifikacie(getApplicationContext());
                notifikacie.prehrajZvuk();
            } else {
                Intent otvorAplikaciu = new Intent(getApplicationContext(), UvodnaObrazovka.class);

                if (TextUtils.isEmpty(udalost)) {
                    if (TextUtils.isEmpty(obrazok)) {
                        notifikacia(getApplicationContext(), meno, "potvrdil priatelstvo", cas, otvorAplikaciu);
                    } else {
                        notifikacia(getApplicationContext(), meno, "Vás požiadal o pritelstvo", cas, otvorAplikaciu);
                    }
                } else {
                    notifikaciaObrazkom(getApplicationContext(), meno, "Vás pozval na udalosť " + nazov + " - " + mesto, cas, otvorAplikaciu, udalost);
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, "Firebase JSON Chyba " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Firebase chyba: " + e.getMessage());
        }
    }

    private void notifikacia(Context context, String titul, String sprava, String cas, Intent akcia) {
        notifikacie = new Notifikacie(context);
        akcia.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notifikacie.spravaNotifikacie(titul, sprava, cas, akcia);
    }

    private void notifikaciaObrazkom(Context context, String titul, String sprava, String cas, Intent akcia, String obrazok) {
        notifikacie = new Notifikacie(context);
        akcia.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notifikacie.spravaNotifikacie(titul, sprava, cas, akcia, obrazok);
    }
}