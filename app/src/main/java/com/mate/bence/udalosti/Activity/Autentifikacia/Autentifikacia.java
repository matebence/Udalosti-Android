package com.mate.bence.udalosti.Activity.Autentifikacia;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.mate.bence.udalosti.Activity.Autentifikacia.Fragment.Prihlasenie;
import com.mate.bence.udalosti.Activity.Autentifikacia.Fragment.Registracia;
import com.mate.bence.udalosti.Activity.Udalosti.Udalosti;
import com.mate.bence.udalosti.Dialog.DialogOznameni;
import com.mate.bence.udalosti.Nastroje.Pripojenie;
import com.mate.bence.udalosti.R;
import com.mate.bence.udalosti.Udaje.Nastavenia.Nastavenia;
import com.mate.bence.udalosti.Udaje.Siet.Model.KommunikaciaOdpoved;

import java.util.HashMap;

public class Autentifikacia extends AppCompatActivity implements AutentifikaciaOvladanie, KommunikaciaOdpoved {

    private FragmentManager fragmentManager;
    private AutentifikaciaUdaje autentifikaciaUdaje;
    private LinearLayout nacitavanie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autentifikacia);

        this.fragmentManager = getFragmentManager();
        this.autentifikaciaUdaje = new AutentifikaciaUdaje(this, getApplicationContext());
        this.nacitavanie = findViewById(R.id.nacitavanie);

        pridajFragment(new Prihlasenie(), Nastavenia.AUTENTIFIKACIA_PRIHLASENIE);
        automatickePrihlasenieChyba();
    }

    @Override
    public void onBackPressed() {
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStackImmediate();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void odpovedServera(String odpoved, String od, HashMap<String, String> udaje) {
        switch (od) {
            case Nastavenia.AUTENTIFIKACIA_REGISTRACIA:
                if (odpoved.equals(Nastavenia.VSETKO_V_PORIADKU)) {
                    fragmentManager.popBackStackImmediate();

                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), getString(R.string.autentifikacia_uspesna_registracia), Snackbar.LENGTH_SHORT);
                    View pozadie = snackbar.getView();
                    pozadie.setBackgroundColor(getResources().getColor(R.color.toolbar));
                    snackbar.setActionTextColor(getResources().getColor(android.R.color.white));
                    snackbar.show();
                } else {
                    new DialogOznameni(this, "Chyba", odpoved);
                }
                break;

            case Nastavenia.AUTENTIFIKACIA_PRIHLASENIE:
                if (odpoved.equals(Nastavenia.VSETKO_V_PORIADKU)) {
                    autentifikaciaUdaje.ulozPrihlasovacieUdajeDoDatabazy(
                            udaje.get("email"),
                            udaje.get("heslo"));

                    Intent uspesnePrihlasenie = new Intent(Autentifikacia.this, Udalosti.class);
                    uspesnePrihlasenie.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    uspesnePrihlasenie.putExtra("email", udaje.get("email"));
                    uspesnePrihlasenie.putExtra("heslo", udaje.get("heslo"));
                    uspesnePrihlasenie.putExtra("token", udaje.get("token"));
                    startActivity(uspesnePrihlasenie);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    finish();
                } else {
                    new DialogOznameni(this, "Chyba", odpoved);
                }
                break;
        }
        this.nacitavanie.setVisibility(View.GONE);
    }

    @Override
    public void tlacidloPrihlasitSa(String email, String heslo) {
        if (Pripojenie.pripojenieExistuje(this)) {
            this.nacitavanie.setVisibility(View.VISIBLE);
            autentifikaciaUdaje.miestoPrihlasenia(email, heslo);
        } else {
            new DialogOznameni(this, "Chyba", getString(R.string.chyba_ziadne_spojenie));
        }
    }

    @Override
    public void tlacidloRegistrovatSa(String meno, String email, String heslo, String potvrd) {
        if (Pripojenie.pripojenieExistuje(this)) {
            this.nacitavanie.setVisibility(View.VISIBLE);
            autentifikaciaUdaje.registracia(meno, email, heslo, potvrd);
        } else {
            new DialogOznameni(this, "Chyba", getString(R.string.chyba_ziadne_spojenie));
        }
    }

    @Override
    public void registracia() {
        pridajFragment(new Registracia(), Nastavenia.AUTENTIFIKACIA_REGISTRACIA);
    }

    private void pridajFragment(Fragment fragment, String nazov) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        animujObsah(nazov, fragmentTransaction);
        fragmentTransaction.replace(R.id.prihlasenie_registracia_zabudnute_heslo, fragment);
        if (!(nazov.equals(Nastavenia.AUTENTIFIKACIA_PRIHLASENIE))) {
            fragmentTransaction.addToBackStack(nazov);
        }
        fragmentTransaction.commit();
    }

    private void animujObsah(String fragment, FragmentTransaction fragmentTransaction) {
        switch (fragment) {
            case Nastavenia.AUTENTIFIKACIA_REGISTRACIA:
                fragmentTransaction.setCustomAnimations(R.animator.z_prava_do_lava, R.animator.vysunut_z_lava_do_prava, R.animator.vysunut_z_prava_do_lava, R.animator.z_lava_do_prava);
                break;
            default:
                fragmentTransaction.setCustomAnimations(R.animator.zviditelnit, 0, 0, 0);
                break;
        }
    }

    private void automatickePrihlasenieChyba() {
        Bundle udaje = getIntent().getExtras();
        if (udaje != null) {
            if (udaje.getBoolean("neUspesnePrihlasenie")) {
                new DialogOznameni(this, "Chyba", getString(R.string.chyba_automatickeho_prihlasenia));
                if (udaje.getString("email") != null) {
                    this.autentifikaciaUdaje.ucetJeNePristupny(udaje.getString("email"));
                }
            } else if (udaje.getString("chyba") != null) {
                new DialogOznameni(this, "Chyba", udaje.getString("chyba"));
            }
        }
    }
}