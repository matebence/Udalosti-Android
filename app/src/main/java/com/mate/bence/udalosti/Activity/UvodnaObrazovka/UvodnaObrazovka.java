package com.mate.bence.udalosti.Activity.UvodnaObrazovka;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.mate.bence.udalosti.Activity.Autentifikacia.Autentifikacia;
import com.mate.bence.udalosti.Udaje.Siet.Model.DlzkaRequestu;
import com.mate.bence.udalosti.Activity.Autentifikacia.AutentifikaciaUdaje;
import com.mate.bence.udalosti.Activity.RychlaUkazkaAplikacie.RychlaUkazkaAplikacie;
import com.mate.bence.udalosti.Activity.Udalosti.Udalosti;
import com.mate.bence.udalosti.Nastroje.Casovac;
import com.mate.bence.udalosti.Nastroje.Pripojenie;
import com.mate.bence.udalosti.R;
import com.mate.bence.udalosti.Udaje.Data.Preferencie;
import com.mate.bence.udalosti.Udaje.Nastavenia.Nastavenia;
import com.mate.bence.udalosti.Udaje.Siet.Model.KommunikaciaOdpoved;

import java.util.HashMap;

public class UvodnaObrazovka extends AppCompatActivity implements KommunikaciaOdpoved, LocationListener, DlzkaRequestu {

    private static final String TAG = UvodnaObrazovka.class.getName();
    private final int REQUEST_CODE_GPS = 101;

    private LocationManager managerPozicie;
    private Casovac casovac;

    private HashMap<String, String> pouzivatelskeUdaje;
    private AutentifikaciaUdaje autentifikaciaUdaje;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uvodna_obrazovka);

        init();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_CODE_GPS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    zistiPoziciu();
                } else {
                    cistyStart(Autentifikacia.class, true, getString(R.string.udalosti_gps_pristup));
                }
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        this.autentifikaciaUdaje.miestoPrihlasenia(this.pouzivatelskeUdaje.get("email"), this.pouzivatelskeUdaje.get("heslo"), location.getLatitude(), location.getLongitude(), true, false);
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
    public void odpovedServera(String odpoved, String od, HashMap<String, String> udaje) {
        Log.v(UvodnaObrazovka.TAG, "Metoda odpovedServera - UvodnaObrazovka bola vykonana");

        switch (od) {
            case Nastavenia.AUTENTIFIKACIA_PRIHLASENIE:
                Intent podlaSpravnosti;
                if (odpoved.equals(Nastavenia.VSETKO_V_PORIADKU)) {
                    podlaSpravnosti = new Intent(UvodnaObrazovka.this, Udalosti.class);
                    podlaSpravnosti.putExtra("email", udaje.get("email"));
                    podlaSpravnosti.putExtra("heslo", udaje.get("heslo"));
                    podlaSpravnosti.putExtra("token", udaje.get("token"));
                } else {
                    podlaSpravnosti = new Intent(UvodnaObrazovka.this, Autentifikacia.class);
                    podlaSpravnosti.putExtra("neUspesnePrihlasenie", true);
                    if (udaje != null) {
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

    @Override
    public void zistiPoziciuPodlaSiete() {
        Log.v(UvodnaObrazovka.TAG, "Metoda zistiPoziciuPodlaSiete - UvodnaObrazovka bola vykonana");

        this.autentifikaciaUdaje.miestoPrihlasenia(this.pouzivatelskeUdaje.get("email"), this.pouzivatelskeUdaje.get("heslo"), false);
    }

    private void init() {
        Log.v(UvodnaObrazovka.TAG, "Metoda init - UvodnaObrazovka bola vykonana");

        Preferencie preferencie = new Preferencie(this);

        this.autentifikaciaUdaje = new AutentifikaciaUdaje(this, getApplicationContext());
        UvodnaObrazovkaUdaje uvodnaObrazovkaUdaje = new UvodnaObrazovkaUdaje(getApplicationContext());
        this.pouzivatelskeUdaje = uvodnaObrazovkaUdaje.prihlasPouzivatela();
        this.managerPozicie = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        this.casovac = new Casovac(Nastavenia.DLZKA_REQUESTU, this.managerPozicie,this, this);

        if (preferencie.jePrvyStart()) {
            cistyStart(RychlaUkazkaAplikacie.class, false, null);
        } else {
            if (Pripojenie.pripojenieExistuje(this)) {
                if (uvodnaObrazovkaUdaje.zistiCiPouzivatelskoKontoExistuje()) {
                    if (!(this.managerPozicie.isProviderEnabled(LocationManager.GPS_PROVIDER))) {
                        cistyStart(Autentifikacia.class, true, getString(R.string.chyba_ziadna_siet_gps));
                    } else {
                        zistiPoziciu();
                    }
                } else {
                    cistyStart(Autentifikacia.class, false, null);
                }
            } else {
                cistyStart(Autentifikacia.class, true, getString(R.string.chyba_ziadne_spojenie));
            }
        }
    }

    public void zistiPoziciu() {
        Log.v(UvodnaObrazovka.TAG, "Metoda zistiPoziciu bola vykonana");

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE_GPS);
        } else {
            this.managerPozicie.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            this.casovac.start();
        }
    }

    private void cistyStart(Class trieda, boolean chyba, String opisChyby) {
        Log.v(UvodnaObrazovka.TAG, "Metoda cistyStart bola vykonana");

        Intent dalej = new Intent(UvodnaObrazovka.this, trieda);
        if (chyba) {
            dalej.putExtra("chyba", opisChyby);
        }
        dalej.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(dalej);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }
}