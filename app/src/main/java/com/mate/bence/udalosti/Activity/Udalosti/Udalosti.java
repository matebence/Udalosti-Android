package com.mate.bence.udalosti.Activity.Udalosti;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.mate.bence.udalosti.Activity.Autentifikacia.Autentifikacia;
import com.mate.bence.udalosti.Activity.Udalosti.Karty.Objavuj;
import com.mate.bence.udalosti.Activity.Udalosti.Karty.Lokalizator;
import com.mate.bence.udalosti.Activity.Udalosti.Karty.Zaujmy;
import com.mate.bence.udalosti.Dialog.DialogOznameni;
import com.mate.bence.udalosti.Nastroje.Odhlasenie;
import com.mate.bence.udalosti.R;
import com.mate.bence.udalosti.Udaje.Nastavenia.Nastavenia;
import com.mate.bence.udalosti.Udaje.Siet.Model.KommunikaciaData;
import com.mate.bence.udalosti.Udaje.Siet.Model.KommunikaciaOdpoved;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Udalosti extends AppCompatActivity implements KommunikaciaOdpoved, KommunikaciaData, UdalostiPanel {

    private static final String TAG = Udalosti.class.getName();

    public TextView titul;
    private FragmentManager fragmentManager;

    private String email, heslo, token;
    private boolean odhlasenieZoServera = false;

    private UdalostiUdaje udalostiUdaje;
    private HashMap<String, String> miestoPrihlasenia;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_udalosti);

        if (pristup()) {
            this.udalostiUdaje = new UdalostiUdaje(this, this, getApplicationContext());
            this.miestoPrihlasenia = this.udalostiUdaje.miestoPrihlasenia();
            this.fragmentManager = getSupportFragmentManager();

            spustiBezpecneOdhlasenie();
            init();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        this.odhlasenieZoServera = true;
        this.udalostiUdaje.odhlasenie(this.email);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.odhlasit_sa, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.odhlasit_sa:
                this.udalostiUdaje.odhlasenie(email);
                return true;
        }
        return false;
    }

    @Override
    public void odpovedServera(String odpoved, String od, HashMap<String, String> udaje) {
        Log.v(Udalosti.TAG, "Metoda odpovedServera - Udalosti bola vykonana");

        switch (od) {
            case Nastavenia.AUTENTIFIKACIA_ODHLASENIE:
                Intent data = getIntent();
                if (!(this.odhlasenieZoServera)) {
                    if ((data != null) && (odpoved.equals(Nastavenia.VSETKO_V_PORIADKU))) {
                        this.udalostiUdaje.automatickePrihlasenieVypnute(this.email);

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

    @Override
    public void dataZoServera(String odpoved, String od, ArrayList udaje) {
    }

    @Override
    public void aktualizujPanel(String pozicia) {
        Log.v(Udalosti.TAG, "Metoda aktualizujPanel - Udalosti bola vykonana");

        this.miestoPrihlasenia.put("pozicia", udalostiUdaje.miestoPrihlasenia().get("pozicia").toString());
        this.titul.setText(nastavTitulKariet(getApplicationContext().getString(R.string.udalosti_okolie)+" " + pozicia));
    }

    public void init() {
        Log.v(Udalosti.TAG, "Metoda init - Udalosti bola vykonana");

        this.titul = findViewById(R.id.titulok);
        this.titul.setText(miestoPrihlasenia.get("stat"));

        ViewPager dalsieKarty = findViewById(R.id.udalosti_gesta);
        TabLayout karty = findViewById(R.id.udalosti_karty);

        Toolbar toolBar = findViewById(R.id.toolbar);
        nastavToolbar(toolBar);

        nastavKarty(dalsieKarty);
        karty.setupWithViewPager(dalsieKarty);
        karty.addOnTabSelectedListener(zmenFarbuIkonov);

        nastavIkonyKartov(karty);
    }

    private boolean pristup() {
        Log.v(Udalosti.TAG, "Metoda pristup bola vykonana");

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

    private void spustiBezpecneOdhlasenie() {
        Log.v(Udalosti.TAG, "Metoda spustiBezpecneOdhlasenie bola vykonana");

        Intent bezpecneOdhlasenie = new Intent(getApplicationContext(), Odhlasenie.class);
        bezpecneOdhlasenie.putExtra("email", this.email);
        startService(bezpecneOdhlasenie);
    }

    private void nastavToolbar(Toolbar toolBar) {
        Log.v(Udalosti.TAG, "Metoda nastavToolbar bola vykonana");

        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void nastavIkonyKartov(TabLayout karty) {
        Log.v(Udalosti.TAG, "Metoda nastavIkonyKartov bola vykonana");

        int ikonyKartov[] = {R.drawable.ic_objavuj, R.drawable.ic_podla_pozicie, R.drawable.ic_zaujmy};
        for (int i = 0; i < karty.getTabCount(); i++) {
            karty.getTabAt(i).setIcon(ikonyKartov[i]);
        }
    }

    private void nastavKarty(ViewPager viewPager) {
        Log.v(Udalosti.TAG, "Metoda nastavKarty bola vykonana");

        GestaKariet gestaKariet = new GestaKariet(this.fragmentManager);

        Bundle bundle = new Bundle();
        Objavuj objavuj = new Objavuj();
        Lokalizator lokalizator = new Lokalizator();
        Zaujmy zaujmy = new Zaujmy();

        bundle.putString("stat", this.miestoPrihlasenia.get("stat"));
        bundle.putString("okres", this.miestoPrihlasenia.get("okres"));
        bundle.putString("pozicia", this.miestoPrihlasenia.get("pozicia"));
        bundle.putString("email", this.email);
        bundle.putString("heslo", this.heslo);
        bundle.putString("token", this.token);

        objavuj.setArguments(bundle);
        lokalizator.setArguments(bundle);
        zaujmy.setArguments(bundle);

        String[] karty = {getString(R.string.karta_prva), getString(R.string.karta_druha), getString(R.string.karta_tretia)};
        gestaKariet.nacitajFragment(objavuj, karty[0]);
        gestaKariet.nacitajFragment(lokalizator, karty[1]);
        gestaKariet.nacitajFragment(zaujmy, karty[2]);

        viewPager.setOffscreenPageLimit(karty.length);
        viewPager.setAdapter(gestaKariet);
    }

    private String nastavTitulKariet(String nazov){
        Log.v(Udalosti.TAG, "Metoda nastavTitulKariet bola vykonana");

        String[] strArray = nazov.split(" ");
        StringBuilder titul = new StringBuilder();
        for (String s : strArray) {
            String prvePismeno = s.substring(0, 1).toUpperCase() + s.substring(1);
            titul.append(prvePismeno).append(" ");
        }
        return titul.toString();
    }

    private TabLayout.OnTabSelectedListener zmenFarbuIkonov = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            switch (tab.getPosition()) {
                case 0:
                    titul.setText(nastavTitulKariet(miestoPrihlasenia.get("stat")));
                    break;
                case 1:
                    if (miestoPrihlasenia.get("pozicia") != null) {
                        titul.setText(nastavTitulKariet(getApplicationContext().getString(R.string.udalosti_okolie)+" " + miestoPrihlasenia.get("pozicia")));
                    } else if (miestoPrihlasenia.get("okres") != null) {
                        titul.setText(nastavTitulKariet(miestoPrihlasenia.get("okres")));
                    } else if (miestoPrihlasenia.get("kraj") != null) {
                        titul.setText(nastavTitulKariet(miestoPrihlasenia.get("kraj")));
                    }else{
                        titul.setText(getString(R.string.chyba_pozicia_neurcena));
                    }
                    break;
                case 2:
                    titul.setText(getString(R.string.zoznam_zaujmov));
                    break;
            }

            int tabIconColor = ContextCompat.getColor(getApplicationContext(), R.color.tab_ikona_aktivna);
            assert tab.getIcon() != null;
            tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
            int tabIconColor = ContextCompat.getColor(getApplicationContext(), R.color.tab_ikona_inaktivna);
            assert tab.getIcon() != null;
            tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
            int tabIconColor = ContextCompat.getColor(getApplicationContext(), R.color.tab_ikona_aktivna);
            assert tab.getIcon() != null;
            tab.getIcon().setColorFilter(tabIconColor, PorterDuff.Mode.SRC_IN);
        }
    };

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
            Lokalizator lokalizator = new Lokalizator();
            Zaujmy zaujmy = new Zaujmy();

            bundle.putString("stat", miestoPrihlasenia.get("stat"));
            bundle.putString("okres", miestoPrihlasenia.get("okres"));
            bundle.putString("pozicia", miestoPrihlasenia.get("pozicia"));
            bundle.putString("email", email);
            bundle.putString("token", token);

            objavuj.setArguments(bundle);
            lokalizator.setArguments(bundle);
            zaujmy.setArguments(bundle);

            switch (pozicia) {
                case 0:
                    return objavuj;
                case 1:
                    return lokalizator;
                case 2:
                    return zaujmy;
            }
            return null;
        }

        @Override
        public int getCount() {
            return this.fragmenty.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return this.nazvyFragmentov.get(position);
        }

        void nacitajFragment(Fragment fragment, String titul) {
            this.fragmenty.add(fragment);
            this.nazvyFragmentov.add(titul);
        }
    }
}