package com.mate.bence.udalosti.Activity.UvodnaObrazovka;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.mate.bence.udalosti.Activity.Autentifikacia.Autentifikacia;
import com.mate.bence.udalosti.Activity.Autentifikacia.AutentifikaciaUdaje;
import com.mate.bence.udalosti.Activity.Navigacia.Navigacia;
import com.mate.bence.udalosti.Activity.RychlaUkazkaAplikacie.RychlaUkazkaAplikacie;
import com.mate.bence.udalosti.Udaje.Data.Preferencie;
import com.mate.bence.udalosti.Udaje.Nastavenia.Status;
import com.mate.bence.udalosti.Udaje.Siet.Model.KommunikaciaOdpoved;
import com.mate.bence.udalosti.Nastroje.Pripojenie;
import com.mate.bence.udalosti.R;

import java.util.HashMap;

public class UvodnaObrazovka extends AppCompatActivity implements KommunikaciaOdpoved {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uvodna_obrazovka);

        init();
    }

    private void init() {
        Preferencie preferencie = new Preferencie(this);
        UvodnaObrazovkaUdaje uvodnaObrazovkaUdaje = new UvodnaObrazovkaUdaje(getApplicationContext());

        if (preferencie.jePrvyStart()) {
            cistyStart(RychlaUkazkaAplikacie.class, false, null);
        } else {
            if (Pripojenie.pripojenieExistuje(this)) {
                if (uvodnaObrazovkaUdaje.zistiCiPouzivatelskoKontoExistuje()) {
                    HashMap<String, String> pouzivatelskeUdaje = uvodnaObrazovkaUdaje.prihlasPouzivatela();
                    AutentifikaciaUdaje autentifikaciaUdaje = new AutentifikaciaUdaje(this, getApplicationContext());
                    autentifikaciaUdaje.miestoPrihlasenia(pouzivatelskeUdaje.get("email"), pouzivatelskeUdaje.get("heslo"));
                } else {
                    cistyStart(Autentifikacia.class, false, null);
                }
            } else {
                cistyStart(Autentifikacia.class, true, getString(R.string.chyba_ziadne_spojenie));
            }
        }
    }

    private void cistyStart(Class trieda, boolean chyba, String opisChyby) {
        Intent dalej = new Intent(UvodnaObrazovka.this, trieda);
        if (chyba) {
            dalej.putExtra("chyba", opisChyby);
        }
        dalej.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(dalej);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    @Override
    public void odpovedServera(String odpoved, String od, HashMap<String, String> udaje) {
        switch (od) {
            case Status.AUTENTIFIKACIA_PRIHLASENIE:
                Intent podlaSpravnosti;
                if (odpoved.equals(Status.VSETKO_V_PORIADKU)) {
                    podlaSpravnosti = new Intent(UvodnaObrazovka.this, Navigacia.class);
                    podlaSpravnosti.putExtra("email", udaje.get("email"));
                    podlaSpravnosti.putExtra("meno", udaje.get("meno"));
                    podlaSpravnosti.putExtra("heslo", udaje.get("heslo"));
                    podlaSpravnosti.putExtra("token", udaje.get("token"));
                    podlaSpravnosti.putExtra("obrazok", udaje.get("obrazok"));
                } else {
                    podlaSpravnosti = new Intent(UvodnaObrazovka.this, Autentifikacia.class);
                    podlaSpravnosti.putExtra("neUspesnePrihlasenie", true);
                    if (udaje.get("email") != null) {
                        podlaSpravnosti.putExtra("email", udaje.get("email"));
                    }
                }
                podlaSpravnosti.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(podlaSpravnosti);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
                break;
        }
    }
}