package com.mate.bence.udalosti.Activity.Udalosti.Podrobnosti;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mate.bence.udalosti.Activity.Udalosti.Udalosti;
import com.mate.bence.udalosti.Activity.Udalosti.UdalostiUdaje;
import com.mate.bence.udalosti.Dialog.DialogOznameni;
import com.mate.bence.udalosti.Nastroje.Stream;
import com.mate.bence.udalosti.R;
import com.mate.bence.udalosti.Udaje.Nastavenia.Nastavenia;
import com.mate.bence.udalosti.Udaje.Siet.Model.KommunikaciaData;
import com.mate.bence.udalosti.Udaje.Siet.Model.KommunikaciaOdpoved;
import com.mate.bence.udalosti.Zoznam.Udalost;

import java.util.ArrayList;
import java.util.HashMap;

public class Podrobnosti extends AppCompatActivity implements View.OnClickListener, KommunikaciaOdpoved, KommunikaciaData {

    private Button zaujem;
    private ImageView obrazok;
    private ProgressBar nacitavanie;
    private TextView den, mesiac, nazov, miesto, pocetZaujemcov, vstupenka, cas;

    private UdalostiUdaje udalostiUdaje;

    private LinearLayout spracovanieZaujmu;
    private int zaujemUdalosti;

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
                this.spracovanieZaujmu.setVisibility(View.VISIBLE);

                if (udaje != null) {
                    if (zaujemUdalosti == 1) {
                        zaujemUdalosti = 0;

                        udalostiUdaje.odstranZaujem(udaje.getString("token"), udaje.getString("email"), udaje.getInt("idUdalost"));
                        AktualizatorObsahu.stav().hodnota(udaje.getInt("pozicia"), zaujemUdalosti, udaje.getInt("idUdalost"), udaje.getString("karta"));
                    } else {
                        zaujemUdalosti = 1;

                        udalostiUdaje.zaujemUdalost(udaje.getString("token"), udaje.getString("email"), udaje.getInt("idUdalost"));
                        AktualizatorObsahu.stav().hodnota(udaje.getInt("pozicia"), zaujemUdalosti, udaje.getInt("idUdalost"), udaje.getString("karta"));
                    }
                }
                break;
        }
    }

    @Override
    public void dataZoServera(String odpoved, String od, ArrayList udaje) {
        switch (od) {
            case Nastavenia.ZAUJEM_POTVRD:
                if (odpoved.equals(Nastavenia.VSETKO_V_PORIADKU)) {

                    if (udaje != null) {
                        if(udaje.size() == 1){
                            Udalost udalost = (Udalost) udaje.get(0);
                            new Stream(obrazok, nacitavanie, this).execute(udalost.getObrazok());

                            den.setText(udalost.getDen());
                            mesiac.setText(udalost.getMesiac().substring(0, 3) + ".");
                            nazov.setText(udalost.getNazov());
                            miesto.setText(udalost.getMesto() + ", " + udalost.getUlica());
                            pocetZaujemcov.setText(udalost.getZaujemcovia());
                            vstupenka.setText(udalost.getVstupenka() + "€");
                            cas.setText(udalost.getCas());

                            nastavTlacidlo(zaujemUdalosti);
                        }
                    }
                }
                break;
        }
        this.spracovanieZaujmu.setVisibility(View.GONE);
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
        this.spracovanieZaujmu.setVisibility(View.GONE);
    }

    private void init() {
        this.obrazok = findViewById(R.id.obrazok_zvolenej_udalosti);
        this.nacitavanie = findViewById(R.id.nacitavenie_zvolenej_udalosti);
        this.den = findViewById(R.id.den_zvolenej_udalosti);
        this.mesiac = findViewById(R.id.mesiac_zvolenej_udalosti);
        this.nazov = findViewById(R.id.nazov_zvolenej_udalosti);
        this.miesto = findViewById(R.id.miesto_zvolenej_udalosti);
        this.pocetZaujemcov = findViewById(R.id.pocet_zaujemcov_zvolenej_udalosti);
        this.vstupenka = findViewById(R.id.vstupenka_zvolenej_udalosti);
        this.cas = findViewById(R.id.cas_zvolenej_udalosti);

        this.spracovanieZaujmu = findViewById(R.id.spracovanie_zaujmu);

        this.zaujem = findViewById(R.id.tlacidlo_zvolenej_udalosti);
        this.zaujem.setOnClickListener(this);

        this.udalostiUdaje = new UdalostiUdaje(this, this, getApplicationContext());

        Bundle udaje = getIntent().getExtras();
        nacitajUdajeZvolenejUdalosti(udaje);
    }

    private void nacitajUdajeZvolenejUdalosti(Bundle bundle) {
        if (bundle != null) {
            if (bundle.getString("obrazok") != null) {
                zaujemUdalosti = bundle.getInt("zaujemUdalosti");

                new Stream(obrazok, nacitavanie, this).execute(bundle.getString("obrazok"));

                den.setText(bundle.getString("den"));
                mesiac.setText(bundle.getString("mesiac").substring(0, 3) + ".");
                nazov.setText(bundle.getString("nazov"));
                miesto.setText(bundle.getString("mesto") + ", " + bundle.getString("ulica"));
                pocetZaujemcov.setText(bundle.getString("zaujemcovia"));
                vstupenka.setText(bundle.getString("vstupenka") + "€");
                cas.setText(bundle.getString("cas"));

                nastavTlacidlo(zaujemUdalosti);
            }else{
                this.spracovanieZaujmu.setVisibility(View.VISIBLE);
                //udalostiUdaje.potvrdZaujem(bundle.getString("token"), bundle.getString("email"), bundle.getInt("idUdalost"));
            }
        }
    }

    private void nastavTlacidlo(int tlacidlo){
        if (tlacidlo == 1) {
            zaujem.setText(getResources().getString(R.string.podrobnosti_tlacidlo_odstranit));
            zaujem.setBackgroundColor(getResources().getColor(R.color.zaujem_tlacidlo));
        } else {
            zaujem.setText(getResources().getString(R.string.podrobnosti_tlacidlo_zaujem));
            zaujem.setBackgroundColor(getResources().getColor(R.color.farba_sekundarna));
        }
    }
}