package com.mate.bence.udalosti.Activity.Autentifikacia;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.mate.bence.udalosti.Activity.Autentifikacia.Fragment.Prihlasenie;
import com.mate.bence.udalosti.Activity.Autentifikacia.Fragment.Registracia;
import com.mate.bence.udalosti.Activity.Udalosti.Udalosti;
import com.mate.bence.udalosti.Dialog.DialogOdpoved;
import com.mate.bence.udalosti.Dialog.DialogOznameni;
import com.mate.bence.udalosti.Dialog.DialogPotvrdeni;
import com.mate.bence.udalosti.Nastroje.Casovac;
import com.mate.bence.udalosti.Nastroje.Pripojenie;
import com.mate.bence.udalosti.R;
import com.mate.bence.udalosti.Udaje.Nastavenia.Nastavenia;
import com.mate.bence.udalosti.Udaje.Siet.Model.KommunikaciaOdpoved;
import com.mate.bence.udalosti.Udaje.Siet.Model.DlzkaRequestu;

import java.util.HashMap;

public class Autentifikacia extends AppCompatActivity implements AutentifikaciaOvladanie, KommunikaciaOdpoved, LocationListener, DlzkaRequestu {

    private static final String TAG = Autentifikacia.class.getName();
    private final int REQUEST_CODE_GPS = 101;

    private EditText meno, email, heslo, potvrd;
    private LinearLayout nacitavanie;

    private Casovac casovac;

    private FragmentManager fragmentManager;
    private LocationManager managerPozicie;

    private AutentifikaciaUdaje autentifikaciaUdaje;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_autentifikacia);

        init();
    }

    @Override
    public void onBackPressed() {
        if (this.fragmentManager.getBackStackEntryCount() > 0) {
            this.fragmentManager.popBackStackImmediate();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        this.autentifikaciaUdaje.miestoPrihlasenia(email.getText().toString(), heslo.getText().toString(), location.getLatitude(), location.getLongitude(), true, false);
        this.casovac.cancel();
        this.managerPozicie.removeUpdates(this);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_CODE_GPS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    zistiPoziciu();
                } else {
                    this.nacitavanie.setVisibility(View.GONE);
                    new DialogOznameni(this, "Chyba", getString(R.string.udalosti_gps_pristup));
                }
            }
        }
    }

    @Override
    public void tlacidloPrihlasitSa(EditText email, EditText heslo) {
        Log.v(Autentifikacia.TAG, "Metoda Autentifikacia - tlacidloPrihlasitSa bola vykonana");

        if (Pripojenie.pripojenieExistuje(this)) {
            if(!(this.managerPozicie.isProviderEnabled(LocationManager.GPS_PROVIDER))){

                new DialogPotvrdeni(this, getString(R.string.udalosti_gps_titul), getString(R.string.udalosti_gps_text), getString(R.string.dialog_potvrdeni_gps_tlacidlo_a), getString(R.string.dialog_potvrdeni_gps_tlacidlo_b), new DialogOdpoved() {
                    @Override
                    public void tlacidloA() {
                        Intent nastavenia = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(nastavenia);
                    }

                    @Override
                    public void tlacidloB() {
                    }
                }).show();
            }else{
                this.nacitavanie.setVisibility(View.VISIBLE);
                this.email = email;
                this.heslo = heslo;

                zistiPoziciu();
            }
        } else {
            new DialogOznameni(this, "Chyba", getString(R.string.chyba_ziadne_spojenie));
        }
    }

    @Override
    public void tlacidloRegistrovatSa(EditText meno, EditText email, EditText heslo, EditText potvrd) {
        Log.v(Autentifikacia.TAG, "Metoda Autentifikacia - tlacidloRegistrovatSa bola vykonana");

        if (Pripojenie.pripojenieExistuje(this)) {
            this.nacitavanie.setVisibility(View.VISIBLE);
            this.meno = meno;
            this.email = email;
            this.heslo = heslo;
            this.potvrd = potvrd;

            this.autentifikaciaUdaje.registracia(meno.getText().toString(), email.getText().toString(), heslo.getText().toString(), potvrd.getText().toString());
        } else {
            new DialogOznameni(this, "Chyba", getString(R.string.chyba_ziadne_spojenie));
        }
    }

    @Override
    public void registracia() {
        Log.v(Autentifikacia.TAG, "Metoda Autentifikacia - registracia bola vykonana");

        pridajFragment(new Registracia(), Nastavenia.AUTENTIFIKACIA_REGISTRACIA);
    }

    @Override
    public void odpovedServera(String odpoved, String od, HashMap<String, String> udaje) {
        Log.v(Autentifikacia.TAG, "Metoda odpovedServera - Autentifikacia bola vykonana");

        switch (od) {
            case Nastavenia.AUTENTIFIKACIA_REGISTRACIA:
                this.meno.setFocusableInTouchMode(true);
                this.email.setFocusableInTouchMode(true);
                this.heslo.setFocusableInTouchMode(true);
                this.potvrd.setFocusableInTouchMode(true);

                if (odpoved.equals(Nastavenia.VSETKO_V_PORIADKU)) {
                    this.fragmentManager.popBackStackImmediate();

                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), getString(R.string.autentifikacia_uspesna_registracia), Snackbar.LENGTH_SHORT);
                    View pozadie = snackbar.getView();
                    pozadie.setBackgroundColor(getResources().getColor(R.color.farba_sekundarna));
                    snackbar.setActionTextColor(getResources().getColor(android.R.color.white));
                    snackbar.show();
                } else {
                    new DialogOznameni(this, "Chyba", odpoved);
                }
                break;

            case Nastavenia.AUTENTIFIKACIA_PRIHLASENIE:
                email.setFocusableInTouchMode(true);
                heslo.setFocusableInTouchMode(true);

                if (odpoved.equals(Nastavenia.VSETKO_V_PORIADKU)) {
                    this.autentifikaciaUdaje.ulozPrihlasovacieUdajeDoDatabazy(
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
    public void zistiPoziciuPodlaSiete() {
        Log.v(Autentifikacia.TAG, "Metoda Autenttifikacia zistiPoziciuPodlaSiete bola vykonana");

        this.autentifikaciaUdaje.miestoPrihlasenia(email.getText().toString(), heslo.getText().toString(), false);
    }

    private void init(){
        Log.v(Autentifikacia.TAG, "Metoda init - Autentifikacia bola vykonana");

        this.fragmentManager = getFragmentManager();
        this.managerPozicie = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        this.autentifikaciaUdaje = new AutentifikaciaUdaje(this, getApplicationContext());
        this.nacitavanie = findViewById(R.id.nacitavanie);

        this.casovac = new Casovac(Nastavenia.DLZKA_REQUESTU,managerPozicie,this, this);

        pridajFragment(new Prihlasenie(), Nastavenia.AUTENTIFIKACIA_PRIHLASENIE);
        automatickePrihlasenieChyba();
    }

    private void pridajFragment(Fragment fragment, String nazov) {
        Log.v(Autentifikacia.TAG, "Metoda pridajFragment bola vykonana");

        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        animujObsah(nazov, fragmentTransaction);
        fragmentTransaction.replace(R.id.prihlasenie_registracia_zabudnute_heslo, fragment);
        if (!(nazov.equals(Nastavenia.AUTENTIFIKACIA_PRIHLASENIE))) {
            fragmentTransaction.addToBackStack(nazov);
        }
        fragmentTransaction.commit();
    }

    private void animujObsah(String fragment, FragmentTransaction fragmentTransaction) {
        Log.v(Autentifikacia.TAG, "Metoda animujObsah bola vykonana");

        switch (fragment) {
            case Nastavenia.AUTENTIFIKACIA_REGISTRACIA:
                fragmentTransaction.setCustomAnimations(R.animator.vstupit_vychod_fragment, R.animator.ukoncit_vchod_fragment, R.animator.vstupit_vchod_fragment, R.animator.ukoncit_vychod_fragment);
                break;
            default:
                fragmentTransaction.setCustomAnimations(R.animator.zviditelnit, 0, 0, 0);
                break;
        }
    }

    public void zistiPoziciu() {
        Log.v(Autentifikacia.TAG, "Metoda zistiPoziciu bola vykonana");

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE_GPS);
        } else {
            this.managerPozicie.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            this.casovac.start();
        }
    }

    private void automatickePrihlasenieChyba() {
        Log.v(Autentifikacia.TAG, "Metoda automatickePrihlasenieChyba bola vykonana");

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