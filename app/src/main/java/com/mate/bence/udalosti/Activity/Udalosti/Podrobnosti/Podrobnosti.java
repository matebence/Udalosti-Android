package com.mate.bence.udalosti.Activity.Udalosti.Podrobnosti;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mate.bence.udalosti.Activity.Udalosti.UdalostiUdaje;
import com.mate.bence.udalosti.Dialog.DialogOznameni;
import com.mate.bence.udalosti.Nastroje.Stream;
import com.mate.bence.udalosti.R;
import com.mate.bence.udalosti.Udaje.Nastavenia.Nastavenia;
import com.mate.bence.udalosti.Udaje.Siet.Model.KommunikaciaData;
import com.mate.bence.udalosti.Udaje.Siet.Model.KommunikaciaOdpoved;

import java.util.ArrayList;
import java.util.HashMap;

public class Podrobnosti extends AppCompatActivity implements View.OnClickListener, KommunikaciaOdpoved, KommunikaciaData {

    private UdalostiUdaje udalostiUdaje;
    private Button zaujem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_podrobnosti);
        init();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.ukoncit_vchod_activity, R.anim.ukoncit_vychod_activity);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tlacidlo_zvolenej_udalosti:
                Bundle udaje = getIntent().getExtras();
                if (udaje != null) {
                    if (udaje.getString("zaujemUdalosti").equals("1")) {
                        udalostiUdaje.odstranZaujem(udaje.getString("token"), udaje.getString("email"), udaje.getString("idUdalost"));
                    } else {
                        udalostiUdaje.zaujemUdalost(udaje.getString("token"), udaje.getString("email"), udaje.getString("idUdalost"));
                    }
                }
                break;
        }
    }

    @Override
    public void dataZoServera(String odpoved, String od, ArrayList udaje) {

    }

    @Override
    public void odpovedServera(String odpoved, String od, HashMap<String, String> udaje) {
        switch (od) {
            case Nastavenia.ZAUJEM_ODSTRANENIE:
                if (odpoved.equals(Nastavenia.VSETKO_V_PORIADKU)) {
                    if (udaje.get("uspech") != null) {
                        Toast.makeText(this, udaje.get("uspech"), Toast.LENGTH_SHORT).show();

                        zaujem.setText(getResources().getString(R.string.podrobnosti_tlacidlo_zaujem));
                        zaujem.setBackgroundColor(getResources().getColor(R.color.farba_sekundarna));
                    } else {
                        Toast.makeText(this, udaje.get("chyba"), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    new DialogOznameni(this, "Chyba", odpoved);
                }
                break;
            case Nastavenia.ZAUJEM:
                if (odpoved.equals(Nastavenia.VSETKO_V_PORIADKU)) {
                    if (udaje.get("uspech") != null) {
                        Toast.makeText(this, udaje.get("uspech"), Toast.LENGTH_SHORT).show();

                        zaujem.setText(getResources().getString(R.string.podrobnosti_tlacidlo_odstranit));
                        zaujem.setBackgroundColor(getResources().getColor(R.color.zaujem_tlacidlo));
                    } else {
                        Toast.makeText(this, udaje.get("chyba"), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    new DialogOznameni(this, "Chyba", odpoved);
                }
                break;
        }
    }

    private void init() {
        Bundle udaje = getIntent().getExtras();
        nacitajUdajeZvolenejUdalosti(udaje);

        udalostiUdaje = new UdalostiUdaje(this, this, getApplicationContext());
        zaujem.setOnClickListener(this);
    }

    private void nacitajUdajeZvolenejUdalosti(Bundle bundle) {
        if (bundle != null) {
            ImageView obrazok = findViewById(R.id.obrazok_zvolenej_udalosti);
            ProgressBar nacitavanie = findViewById(R.id.nacitavenie_zvolenej_udalosti);

            new Stream(obrazok, nacitavanie, this).execute(bundle.getString("obrazok"));

            TextView den = findViewById(R.id.den_zvolenej_udalosti);
            den.setText(bundle.getString("den"));

            TextView mesiac = findViewById(R.id.mesiac_zvolenej_udalosti);
            mesiac.setText(bundle.getString("mesiac").substring(0, 3) + ".");

            TextView nazov = findViewById(R.id.nazov_zvolenej_udalosti);
            nazov.setText(bundle.getString("nazov"));

            TextView miesto = findViewById(R.id.miesto_zvolenej_udalosti);
            miesto.setText(bundle.getString("mesto") + ", " + bundle.getString("ulica"));

            TextView pocetZaujemcov = findViewById(R.id.pocet_zaujemcov_zvolenej_udalosti);
            pocetZaujemcov.setText(bundle.getString("zaujemcovia"));

            TextView vstupenka = findViewById(R.id.vstupenka_zvolenej_udalosti);
            vstupenka.setText(bundle.getString("vstupenka") + "€");

            TextView cas = findViewById(R.id.cas_zvolenej_udalosti);
            cas.setText(bundle.getString("cas"));

            zaujem = findViewById(R.id.tlacidlo_zvolenej_udalosti);
            if (bundle.getString("zaujemUdalosti").equals("1")) {
                zaujem.setText(getResources().getString(R.string.podrobnosti_tlacidlo_odstranit));
                zaujem.setBackgroundColor(getResources().getColor(R.color.zaujem_tlacidlo));
            } else {
                zaujem.setText(getResources().getString(R.string.podrobnosti_tlacidlo_zaujem));
                zaujem.setBackgroundColor(getResources().getColor(R.color.farba_sekundarna));
            }
        }
    }
}