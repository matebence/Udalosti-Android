package com.mate.bence.udalosti.Activity.Udalosti;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;

import com.mate.bence.udalosti.Activity.Autentifikacia.Autentifikacia;
import com.mate.bence.udalosti.Activity.Udalosti.Karty.Objavuj;
import com.mate.bence.udalosti.Activity.Udalosti.Karty.PodlaPozicie;
import com.mate.bence.udalosti.Dialog.DialogOznameni;
import com.mate.bence.udalosti.Nastroje.Odhlasenie;
import com.mate.bence.udalosti.R;
import com.mate.bence.udalosti.Udaje.Nastavenia.Nastavenia;
import com.mate.bence.udalosti.Udaje.Siet.Model.KommunikaciaOdpoved;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Udalosti extends AppCompatActivity implements KommunikaciaOdpoved {

    private static final String TAG = Udalosti.class.getSimpleName();

    private boolean odhlasenieZoServera = false;
    private String email, heslo, token;

    private UdalostiUdaje udalostiUdaje;
    private FragmentManager fragmentManager;

    private HashMap<String, String> miestoPrihlasenia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_udalosti);

        if (pristup()) {
            this.udalostiUdaje = new UdalostiUdaje(this, getApplicationContext());
            this.miestoPrihlasenia = udalostiUdaje.miestoPrihlasenia();
            this.fragmentManager = getSupportFragmentManager();

            spustiBezpecneOdhlasenie();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.odhlasenieZoServera = true;
        udalostiUdaje.odhlasenie(email);
    }

    @Override
    public void odpovedServera(String odpoved, String od, HashMap<String, String> udaje) {
        switch (od) {
            case Nastavenia.AUTENTIFIKACIA_ODHLASENIE:
                Intent data = getIntent();
                if (!(odhlasenieZoServera)) {
                    if ((data != null) && (odpoved.equals(Nastavenia.VSETKO_V_PORIADKU))) {
                        udalostiUdaje.automatickePrihlasenieVypnute(email);

                        data.removeExtra("email");
                        data.removeExtra("heslo");
                        data.removeExtra("token");

                        Intent odhlasitSa = new Intent(Udalosti.this, Autentifikacia.class);
                        odhlasitSa.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(odhlasitSa);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        finish();
                    } else {
                        new DialogOznameni(this, "Chyba", odpoved);
                    }
                }
                break;
        }
    }

    private void spustiBezpecneOdhlasenie() {
        Intent bezpecneOdhlasenie = new Intent(getApplicationContext(), Odhlasenie.class);
        bezpecneOdhlasenie.putExtra("email", email);
        startService(bezpecneOdhlasenie);
    }

    private boolean pristup() {
        Bundle udaje = getIntent().getExtras();
        if (udaje != null) {
            if ((udaje.getString("email") == null) || (udaje.getString("heslo") == null) ||
                    (udaje.getString("token") == null)) {

                Intent chyba = new Intent(Udalosti.this, Autentifikacia.class);

                chyba.putExtra("neUspesnePrihlasenie", true);
                chyba.putExtra("email", udaje.getString("email"));

                chyba.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(chyba);
                finish();
            } else {
                this.email = udaje.getString("email");
                this.heslo = udaje.getString("heslo");
                this.token = udaje.getString("token");
            }
            return true;
        } else {
            return false;
        }
    }

    private class GestaKariet extends FragmentPagerAdapter {

        private final List<Fragment> fragmenty = new ArrayList<>();
        private final List<String> nazvyFragmentov = new ArrayList<>();

        private GestaKariet(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int pozicia) {
            Bundle bundle = new Bundle();
            Objavuj objavuj = new Objavuj();
            PodlaPozicie podlaPozicie = new PodlaPozicie();

            bundle.putString("email", email);
            bundle.putString("token", token);

            objavuj.setArguments(bundle);
            podlaPozicie.setArguments(bundle);

            switch (pozicia) {
                case 0:
                    return objavuj;
                case 1:
                    return podlaPozicie;
            }
            return null;
        }

        @Override
        public int getCount() {
            return fragmenty.size();
        }

        void nacitajFragment(Fragment fragment, String titul) {
            fragmenty.add(fragment);
            nazvyFragmentov.add(titul);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return nazvyFragmentov.get(position);
        }
    }
}