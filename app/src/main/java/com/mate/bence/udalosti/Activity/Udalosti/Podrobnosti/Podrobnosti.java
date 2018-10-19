package com.mate.bence.udalosti.Activity.Udalosti.Podrobnosti;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mate.bence.udalosti.Activity.Udalosti.UdalostiUdaje;
import com.mate.bence.udalosti.Dialog.DialogOznameni;
import com.mate.bence.udalosti.Nastroje.Pripojenie;
import com.mate.bence.udalosti.Nastroje.Stream;
import com.mate.bence.udalosti.R;
import com.mate.bence.udalosti.Udaje.Nastavenia.Nastavenia;
import com.mate.bence.udalosti.Udaje.Siet.Model.KommunikaciaData;
import com.mate.bence.udalosti.Udaje.Siet.Model.KommunikaciaOdpoved;
import com.mate.bence.udalosti.Zoznam.Udalost;

import java.util.ArrayList;
import java.util.HashMap;

public class Podrobnosti extends AppCompatActivity implements View.OnClickListener, KommunikaciaOdpoved, KommunikaciaData {

    private TextView den, mesiac, nazov, miesto, pocetZaujemcov, vstupenka, cas;
    private LinearLayout spracovanieZaujmu;
    private ProgressBar nacitavanie;
    private ImageView obrazok;
    private Button zaujem;

    private UdalostiUdaje udalostiUdaje;
    private int stavTlacidla, pozicia;

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
                    if (stavTlacidla == 1) {
                        stavTlacidla = 0;
                        udalostiUdaje.odstranZaujem(udaje.getString("token"), udaje.getString("email"), udaje.getInt("idUdalost"));

                    } else {
                        stavTlacidla = 1;
                        udalostiUdaje.zaujem(udaje.getString("token"), udaje.getString("email"), udaje.getInt("idUdalost"));
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
                        if (udaje.size() == 1) {

                            Udalost udalost = (Udalost) udaje.get(0);
                            new Stream(obrazok, nacitavanie, this).execute(udalost.getObrazok());

                            stavTlacidla = udalost.getZaujem();
                            pocetZaujemcov.setText(Integer.toString(udalost.getZaujemcovia()));

                            den.setText(udalost.getDen());
                            mesiac.setText(udalost.getMesiac().substring(0, 3) + ".");
                            nazov.setText(udalost.getNazov());
                            miesto.setText(udalost.getMesto() + ", " + udalost.getUlica());
                            vstupenka.setText(Float.toString(udalost.getVstupenka()) + "€");
                            cas.setText(udalost.getCas());

                            nastavTlacdloPodrobnosti(stavTlacidla);
                        }
                    }
                } else {
                    new DialogOznameni(this, "Chyba", odpoved);
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

                        int zaujemcovia = Integer.parseInt(pocetZaujemcov.getText().toString());
                        zaujemcovia--;
                        pocetZaujemcov.setText(Integer.toString(zaujemcovia));
                        AktualizatorObsahu.udalosti().hodnota(pozicia, stavTlacidla, zaujemcovia);

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

                        int zaujemcovia = Integer.parseInt(pocetZaujemcov.getText().toString());
                        zaujemcovia++;
                        pocetZaujemcov.setText(Integer.toString(zaujemcovia));
                        AktualizatorObsahu.udalosti().hodnota(pozicia, stavTlacidla, zaujemcovia);

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
        this.udalostiUdaje = new UdalostiUdaje(this, this, getApplicationContext());
        this.spracovanieZaujmu = findViewById(R.id.spracovanie_zaujmu);

        this.den = findViewById(R.id.den_zvolenej_udalosti);
        this.mesiac = findViewById(R.id.mesiac_zvolenej_udalosti);
        this.nazov = findViewById(R.id.nazov_zvolenej_udalosti);
        this.miesto = findViewById(R.id.miesto_zvolenej_udalosti);
        this.pocetZaujemcov = findViewById(R.id.pocet_zaujemcov_zvolenej_udalosti);
        this.vstupenka = findViewById(R.id.vstupenka_zvolenej_udalosti);
        this.cas = findViewById(R.id.cas_zvolenej_udalosti);

        this.nacitavanie = findViewById(R.id.nacitavenie_zvolenej_udalosti);
        this.obrazok = findViewById(R.id.obrazok_zvolenej_udalosti);

        this.zaujem = findViewById(R.id.tlacidlo_zvolenej_udalosti);
        this.zaujem.setOnClickListener(this);

       nacitajUdajeZvolenejUdalosti();
    }

    private void nacitajUdajeZvolenejUdalosti(){
        Bundle udaje = getIntent().getExtras();
        if (udaje != null) {
            pozicia = udaje.getInt("pozicia");
            stavTlacidla = udaje.getInt("zaujemUdalosti");

            if (Pripojenie.pripojenieExistuje(this)) {
                udalostiUdaje.potvrdZaujem(udaje.getString("token"), udaje.getString("email"), udaje.getInt("idUdalost"));
                spracovanieZaujmu.setVisibility(View.VISIBLE);
            } else {
                new Stream(obrazok, nacitavanie, this).execute(udaje.getString("obrazok"));
                pocetZaujemcov.setText(Integer.toString(udaje.getInt("zaujemcovia")));

                den.setText(udaje.getString("den"));
                mesiac.setText(udaje.getString("mesiac").substring(0, 3) + ".");
                nazov.setText(udaje.getString("nazov"));
                miesto.setText(udaje.getString("mesto") + ", " + udaje.getString("ulica"));
                vstupenka.setText(Float.toString(udaje.getFloat("vstupenka")) + "€");
                cas.setText(udaje.getString("cas"));

                nastavTlacdloPodrobnosti(stavTlacidla);
            }
        }
    }

    private void nastavTlacdloPodrobnosti(int stavTlacidla) {
        if (stavTlacidla == 1) {
            zaujem.setText(getResources().getString(R.string.podrobnosti_tlacidlo_odstranit));
            zaujem.setBackgroundColor(getResources().getColor(R.color.zaujem_tlacidlo));
        } else {
            zaujem.setText(getResources().getString(R.string.podrobnosti_tlacidlo_zaujem));
            zaujem.setBackgroundColor(getResources().getColor(R.color.farba_sekundarna));
        }
    }
}