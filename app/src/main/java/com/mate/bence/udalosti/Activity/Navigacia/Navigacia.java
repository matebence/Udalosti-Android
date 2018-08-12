package com.mate.bence.udalosti.Activity.Navigacia;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mate.bence.udalosti.Activity.Autentifikacia.Autentifikacia;
import com.mate.bence.udalosti.Activity.Navigacia.Fragment.Ludia.Ludia;
import com.mate.bence.udalosti.Activity.Navigacia.Fragment.Nastavenia.Nastavenia;
import com.mate.bence.udalosti.Activity.Navigacia.Fragment.PodlaPozicie.PodlaPozicie;
import com.mate.bence.udalosti.Activity.Navigacia.Fragment.Udalosti.Udalosti;
import com.mate.bence.udalosti.Dialog.Oznamenie.DialogOznameni;
import com.mate.bence.udalosti.Nastroje.Odhlasenie;
import com.mate.bence.udalosti.R;
import com.mate.bence.udalosti.Udaje.Nastavenia.Status;
import com.mate.bence.udalosti.Udaje.Siet.Model.KommunikaciaOdpoved;

import java.io.File;
import java.util.HashMap;

public class Navigacia extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, KommunikaciaOdpoved {

    private static final String TAG = Navigacia.class.getSimpleName();

    private boolean odhlasenieZoServera = false;
    public TextView titul;

    private String email, meno, obrazok;

    private NavigaciaUdaje navigaciaUdaje;
    private FragmentManager fragmentManager;

    private HashMap<String, String> miestoPrihlasenia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aplikacia_bocne_menu);

        if (pristup()) {
            this.navigaciaUdaje = new NavigaciaUdaje(this, getApplicationContext());
            this.miestoPrihlasenia = navigaciaUdaje.miestoPrihlasenia();
            this.fragmentManager = getSupportFragmentManager();

            nacitajFragmentUdalosti();
            spustiBezpecneOdhlasenie();
            init();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.odhlasenieZoServera = true;
        navigaciaUdaje.odhlasenie(email);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout bocneMenu = findViewById(R.id.bocne_menu);
        if (bocneMenu.isDrawerOpen(GravityCompat.START)) {
            bocneMenu.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem polozka) {
        int id = polozka.getItemId();
        Bundle udaje = getIntent().getExtras();

        if (id == R.id.nav_udalosti) {

            Udalosti udalosti = new Udalosti();
            this.titul.setText("Udalosti");

            udaje.putString("stat", miestoPrihlasenia.get("stat"));

            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            udalosti.setArguments(udaje);
            zmenObsah(udalosti, Status.UDALOSTI_OBJAVUJ);

        } else if (id == R.id.nav_podla_pozicie) {

            PodlaPozicie podlaPozicie = new PodlaPozicie();
            if (miestoPrihlasenia.get("mesto") != null) {
                this.titul.setText(miestoPrihlasenia.get("mesto"));
            } else if (miestoPrihlasenia.get("okres") != null) {
                this.titul.setText(miestoPrihlasenia.get("okres"));
            } else if (miestoPrihlasenia.get("stat") != null) {
                this.titul.setText(miestoPrihlasenia.get("stat"));
            }

            udaje.putString("stat", miestoPrihlasenia.get("stat"));
            udaje.putString("okres", miestoPrihlasenia.get("okres"));
            udaje.putString("mesto", miestoPrihlasenia.get("mesto"));

            podlaPozicie.setArguments(udaje);
            zmenObsah(podlaPozicie, Status.PODLA_POZICIE);

        } else if (id == R.id.nav_ludia) {

            Ludia ludia = new Ludia();
            this.titul.setText("Ludia");

            ludia.setArguments(udaje);
            zmenObsah(ludia, Status.LUDIA_VSETCI);

        } else if (id == R.id.nav_nastavenia) {
            Nastavenia nastavenia = new Nastavenia();
            this.titul.setText("Nastavenia");

            nastavenia.setArguments(udaje);
            zmenObsah(nastavenia, Status.NASTAVENIA);

        } else if (id == R.id.nav_odhlasit_sa) {
            navigaciaUdaje.odhlasenie(email);
        }

        DrawerLayout bocneMenu = findViewById(R.id.bocne_menu);
        bocneMenu.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void odpovedServera(String odpoved, String od, HashMap<String, String> udaje) {
        switch (od) {
            case Status.AUTENTIFIKACIA_ODHLASENIE:
                Intent data = getIntent();
                if (!(odhlasenieZoServera)) {
                    if ((data != null) && (odpoved.equals(Status.VSETKO_V_PORIADKU))) {
                        navigaciaUdaje.automatickePrihlasenieVypnute(email);

                        data.removeExtra("email");
                        data.removeExtra("token");
                        data.removeExtra("meno");
                        data.removeExtra("obrazok");

                        Intent odhlasitSa = new Intent(Navigacia.this, Autentifikacia.class);
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

    public void init() {
        this.titul = findViewById(R.id.titulok);
        Toolbar toolBar = findViewById(R.id.toolbar);

        nastavToolbar(toolBar);
        nastavBocneMenu(toolBar);
    }

    private boolean pristup() {
        Bundle udaje = getIntent().getExtras();
        if (udaje != null) {
            if ((udaje.getString("email") == null) || (udaje.getString("meno") == null) ||
                    (udaje.getString("heslo") == null) || (udaje.getString("obrazok") == null) ||
                    (udaje.getString("token") == null)) {

                Intent chyba = new Intent(Navigacia.this, Autentifikacia.class);

                chyba.putExtra("neUspesnePrihlasenie", true);
                chyba.putExtra("email", udaje.getString("email"));

                chyba.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(chyba);
                finish();
            } else {
                this.email = udaje.getString("email");
                this.meno = udaje.getString("meno");
                this.obrazok = udaje.getString("obrazok");
            }
            return true;
        } else {
            return false;
        }
    }

    private void nacitajFragmentUdalosti() {
        Udalosti udalosti = new Udalosti();
        Bundle udaje = getIntent().getExtras();

        udaje.putString("stat", miestoPrihlasenia.get("stat"));
        udaje.putString("okres", miestoPrihlasenia.get("okres"));
        udaje.putString("mesto", miestoPrihlasenia.get("mesto"));

        udalosti.setArguments(udaje);
        zmenObsah(udalosti, Status.UDALOSTI_OBJAVUJ);
    }

    private void spustiBezpecneOdhlasenie() {
        Intent bezpecneOdhlasenie = new Intent(getApplicationContext(), Odhlasenie.class);
        bezpecneOdhlasenie.putExtra("email", email);
        startService(bezpecneOdhlasenie);
    }


    public void zmenObsah(Fragment fragment, String nazov) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.obsah, fragment);
        if (!(nazov.equals(Status.UDALOSTI_OBJAVUJ))) {
            fragmentTransaction.addToBackStack(nazov);
        }
        fragmentTransaction.commit();
    }

    private void nastavToolbar(Toolbar toolBar) {
        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void nastavBocneMenu(Toolbar toolBar) {
        DrawerLayout bocneMenu = findViewById(R.id.bocne_menu);

        ActionBarDrawerToggle otvorZatvor = new ActionBarDrawerToggle(
                this,
                bocneMenu,
                toolBar,
                R.string.bocne_menu_otvorene,
                R.string.bocne_menu_zatvorene);
        bocneMenu.addDrawerListener(otvorZatvor);
        otvorZatvor.syncState();

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_hamburger);

        nastavPouzivatelskeUdaje(meno, obrazok).setNavigationItemSelectedListener(this);
    }

    public NavigationView nastavPouzivatelskeUdaje(String meno, String obrazok) {
        NavigationView navigationView = findViewById(R.id.bocne_menu_polozky);
        View headerView = navigationView.getHeaderView(0);

        TextView pouzivatelskeMeno = headerView.findViewById(R.id.pouzivatelske_meno);
        ImageView obrazokPuzivatela = headerView.findViewById(R.id.profilova_fotka_pouzivatela);

        try {
            Bitmap orezanyObrazok = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), Uri.fromFile(new File(getCacheDir(), obrazok)));
            RoundedBitmapDrawable okruhliObrazok = RoundedBitmapDrawableFactory.create(getResources(), orezanyObrazok);
            okruhliObrazok.setCircular(true);
            obrazokPuzivatela.setImageDrawable(okruhliObrazok);
        } catch (Exception e) {
            Log.v(TAG, "Chyba pri zaokrúhlení fotky " + e.toString());
        }

        pouzivatelskeMeno.setText(meno);
        return navigationView;
    }
}