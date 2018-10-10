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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.mate.bence.udalosti.Activity.Autentifikacia.Autentifikacia;
import com.mate.bence.udalosti.Activity.Udalosti.Karty.Objavuj;
import com.mate.bence.udalosti.Activity.Udalosti.Karty.PodlaPozicie;
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

public class Udalosti extends AppCompatActivity implements KommunikaciaOdpoved, KommunikaciaData {

    private UdalostiUdaje udalostiUdaje;

    public TextView titul;
    private FragmentManager fragmentManager;

    private String email, token;
    private HashMap<String, String> miestoPrihlasenia;

    private boolean odhlasenieZoServera = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_udalosti);

        if (pristup()) {
            this.udalostiUdaje = new UdalostiUdaje(this, this, getApplicationContext());
            this.miestoPrihlasenia = udalostiUdaje.miestoPrihlasenia();
            this.fragmentManager = getSupportFragmentManager();

            spustiBezpecneOdhlasenie();
            init();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.odhlasenieZoServera = true;
        udalostiUdaje.odhlasenie(email);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ikona_odhlasit, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_odhlasit:
                udalostiUdaje.odhlasenie(email);
                return true;
        }
        return false;
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

    @Override
    public void dataZoServera(String odpoved, String od, ArrayList udaje) {
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
                this.token = udaje.getString("token");
            }
            return true;
        } else {
            return false;
        }
    }

    public void init() {
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

    private void spustiBezpecneOdhlasenie() {
        Intent bezpecneOdhlasenie = new Intent(getApplicationContext(), Odhlasenie.class);
        bezpecneOdhlasenie.putExtra("email", email);
        startService(bezpecneOdhlasenie);
    }

    private void nastavToolbar(Toolbar toolBar) {
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void nastavIkonyKartov(TabLayout karty) {
        int ikonyKartov[] = {R.drawable.ic_objavuj, R.drawable.ic_podla_pozicie, R.drawable.ic_zaujmy};
        for (int i = 0; i < karty.getTabCount(); i++) {
            karty.getTabAt(i).setIcon(ikonyKartov[i]);
        }
    }

    private void nastavKarty(ViewPager viewPager) {
        GestaKariet gestaKariet = new GestaKariet(fragmentManager);

        Bundle bundle = new Bundle();
        Objavuj objavuj = new Objavuj();
        PodlaPozicie podlaPozicie = new PodlaPozicie();
        Zaujmy zaujmy = new Zaujmy();

        bundle.putString("stat", miestoPrihlasenia.get("stat"));
        bundle.putString("okres", miestoPrihlasenia.get("okres"));
        bundle.putString("mesto", miestoPrihlasenia.get("mesto"));
        bundle.putString("email", email);
        bundle.putString("token", token);

        objavuj.setArguments(bundle);
        podlaPozicie.setArguments(bundle);

        gestaKariet.nacitajFragment(objavuj, "Udalosti");
        gestaKariet.nacitajFragment(podlaPozicie, "Lokalizátor");
        gestaKariet.nacitajFragment(zaujmy, "Záujmy");

        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(gestaKariet);
    }

    private String nastavTitulKariet(String nazov){
        String[] strArray = nazov.split(" ");
        StringBuilder titul = new StringBuilder();
        for (String s : strArray) {
            String prvePismeno = s.substring(0, 1).toUpperCase() + s.substring(1);
            titul.append(prvePismeno + " ");
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
                    }
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
            PodlaPozicie podlaPozicie = new PodlaPozicie();
            Zaujmy zaujmy = new Zaujmy();

            bundle.putString("stat", miestoPrihlasenia.get("stat"));
            bundle.putString("okres", miestoPrihlasenia.get("okres"));
            bundle.putString("mesto", miestoPrihlasenia.get("mesto"));
            bundle.putString("email", email);
            bundle.putString("token", token);

            objavuj.setArguments(bundle);
            podlaPozicie.setArguments(bundle);

            switch (pozicia) {
                case 0:
                    return objavuj;
                case 1:
                    return podlaPozicie;
                case 2:
                    return zaujmy;
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