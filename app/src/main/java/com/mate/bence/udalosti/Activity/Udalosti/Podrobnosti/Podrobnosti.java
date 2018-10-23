package com.mate.bence.udalosti.Activity.Udalosti.Podrobnosti;

import android.annotation.SuppressLint;
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

    private static final String TAG = Podrobnosti.class.getName();

    private TextView den, mesiac, nazov, miesto, pocetZaujemcov, vstupenka, cas;
    private LinearLayout spracovanieZaujmu;
    private ProgressBar nacitavanie;
    private ImageView obrazok;
    private Button zaujem;

    private UdalostiUdaje udalostiUdaje;

    private int stavTlacidla;

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
                    if (this.stavTlacidla == 1) {
                        this.stavTlacidla = 0;
                        this.udalostiUdaje.odstranZaujem(udaje.getString("email"), udaje.getString("token"), udaje.getInt("idUdalost"));

                    } else {
                        this.stavTlacidla = 1;
                        this.udalostiUdaje.zaujem(udaje.getString("email"), udaje.getString("token"), udaje.getInt("idUdalost"));
                    }
                }
                break;
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void dataZoServera(String odpoved, String od, ArrayList udaje) {
        Log.v(Podrobnosti.TAG, "Metoda dataZoServera - Podrobnosti bola vykonana");

        switch (od) {
            case Nastavenia.ZAUJEM_POTVRD:
                if (odpoved.equals(Nastavenia.VSETKO_V_PORIADKU)) {
                    if (udaje != null) {
                        if (udaje.size() == 1) {

                            Udalost udalost = (Udalost) udaje.get(0);
                            new Stream(this.obrazok, this.nacitavanie, this).execute(udalost.getObrazok());

                            this.stavTlacidla = udalost.getZaujem();
                            this.pocetZaujemcov.setText(Integer.toString(udalost.getZaujemcovia()));

                            this.den.setText(udalost.getDen());
                            this.mesiac.setText(udalost.getMesiac().substring(0, 3) + ".");
                            this.nazov.setText(udalost.getNazov());
                            this.miesto.setText(udalost.getMesto() + ", " + udalost.getUlica());
                            this.vstupenka.setText(Float.toString(udalost.getVstupenka()) + "€");
                            this.cas.setText(udalost.getCas());

                            nastavTlacdloPodrobnosti(this.stavTlacidla);
                        }
                    }
                } else {
                    new DialogOznameni(this, "Chyba", odpoved);
                }
                break;
        }
        this.spracovanieZaujmu.setVisibility(View.GONE);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void odpovedServera(String odpoved, String od, HashMap<String, String> udaje) {
        Log.v(Podrobnosti.TAG, "Metoda odpovedServera - Podrobnosti bola vykonana");

        switch (od) {
            case Nastavenia.ZAUJEM_ODSTRANENIE:
                if (odpoved.equals(Nastavenia.VSETKO_V_PORIADKU)) {
                    if (udaje.get("uspech") != null) {

                        int zaujemcovia = Integer.parseInt(this.pocetZaujemcov.getText().toString());
                        zaujemcovia--;
                        this.pocetZaujemcov.setText(Integer.toString(zaujemcovia));
                        AktualizatorObsahu.zaujmy().hodnota();

                        this.zaujem.setText(getResources().getString(R.string.podrobnosti_tlacidlo_zaujem));
                        this.zaujem.setBackgroundColor(getResources().getColor(R.color.farba_sekundarna));
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

                        int zaujemcovia = Integer.parseInt(this.pocetZaujemcov.getText().toString());
                        zaujemcovia++;
                        this.pocetZaujemcov.setText(Integer.toString(zaujemcovia));
                        AktualizatorObsahu.zaujmy().hodnota();

                        this.zaujem.setText(getResources().getString(R.string.podrobnosti_tlacidlo_odstranit));
                        this.zaujem.setBackgroundColor(getResources().getColor(R.color.zaujem_tlacidlo));
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
        Log.v(Podrobnosti.TAG, "Metoda init - Podrobnosti bola vykonana");

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

    @SuppressLint("SetTextI18n")
    private void nacitajUdajeZvolenejUdalosti(){
        Log.v(Podrobnosti.TAG, "Metoda nacitajUdajeZvolenejUdalosti bola vykonana");

        Bundle udaje = getIntent().getExtras();
        if (udaje != null) {
            this.stavTlacidla = udaje.getInt("zaujemUdalosti");

            if (Pripojenie.pripojenieExistuje(this)) {
                this.udalostiUdaje.potvrdZaujem(udaje.getString("email"), udaje.getString("token"), udaje.getInt("idUdalost"));
                this.spracovanieZaujmu.setVisibility(View.VISIBLE);
            } else {
                new Stream(this.obrazok, this.nacitavanie, this).execute(udaje.getString("obrazok"));
                this.pocetZaujemcov.setText(Integer.toString(udaje.getInt("zaujemcovia")));

                this.den.setText(udaje.getString("den"));
                this.mesiac.setText(udaje.getString("mesiac").substring(0, 3) + ".");
                this.nazov.setText(udaje.getString("nazov"));
                this.miesto.setText(udaje.getString("mesto") + ", " + udaje.getString("ulica"));
                this.vstupenka.setText(Float.toString(udaje.getFloat("vstupenka")) + "€");
                this.cas.setText(udaje.getString("cas"));

                nastavTlacdloPodrobnosti(this.stavTlacidla);
            }
        }else{
            new DialogOznameni(this, "Chyba", getString(R.string.chyba_ziadne_spojenie));
        }
    }

    private void nastavTlacdloPodrobnosti(int stavTlacidla) {
        Log.v(Podrobnosti.TAG, "Metoda nastavTlacdloPodrobnosti bola vykonana");

        if (stavTlacidla == 1) {
            this.zaujem.setText(getResources().getString(R.string.podrobnosti_tlacidlo_odstranit));
            this.zaujem.setBackgroundColor(getResources().getColor(R.color.zaujem_tlacidlo));
        } else {
            this.zaujem.setText(getResources().getString(R.string.podrobnosti_tlacidlo_zaujem));
            this.zaujem.setBackgroundColor(getResources().getColor(R.color.farba_sekundarna));
        }
    }
}