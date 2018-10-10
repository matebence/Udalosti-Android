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

import com.mate.bence.udalosti.Activity.Autentifikacia.Autentifikacia;
import com.mate.bence.udalosti.Activity.Autentifikacia.AutentifikaciaUdaje;
import com.mate.bence.udalosti.Activity.RychlaUkazkaAplikacie.RychlaUkazkaAplikacie;
import com.mate.bence.udalosti.Activity.Udalosti.Udalosti;
import com.mate.bence.udalosti.Nastroje.Pripojenie;
import com.mate.bence.udalosti.R;
import com.mate.bence.udalosti.Udaje.Data.Preferencie;
import com.mate.bence.udalosti.Udaje.Nastavenia.Nastavenia;
import com.mate.bence.udalosti.Udaje.Siet.Model.KommunikaciaOdpoved;

import java.util.HashMap;

public class UvodnaObrazovka extends AppCompatActivity implements KommunikaciaOdpoved, LocationListener {

    private UvodnaObrazovkaUdaje uvodnaObrazovkaUdaje;
    private final int REQUEST_CODE_GPS = 101;
    private LocationManager managerPozicie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uvodna_obrazovka);

        init();
    }

    @Override
    public void odpovedServera(String odpoved, String od, HashMap<String, String> udaje) {
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
        HashMap<String, String> pouzivatelskeUdaje = uvodnaObrazovkaUdaje.prihlasPouzivatela();
        AutentifikaciaUdaje autentifikaciaUdaje = new AutentifikaciaUdaje(this, getApplicationContext());
        autentifikaciaUdaje.miestoPrihlasenia(pouzivatelskeUdaje.get("email"), pouzivatelskeUdaje.get("heslo"),location.getLatitude(),location.getLongitude());
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

    private void init() {
        Preferencie preferencie = new Preferencie(this);

        this.uvodnaObrazovkaUdaje = new UvodnaObrazovkaUdaje(getApplicationContext());
        this.managerPozicie = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (preferencie.jePrvyStart()) {
            cistyStart(RychlaUkazkaAplikacie.class, false, null);
        } else {
            if (Pripojenie.pripojenieExistuje(this)) {
                if (uvodnaObrazovkaUdaje.zistiCiPouzivatelskoKontoExistuje()) {
                    if(!(managerPozicie.isProviderEnabled(LocationManager.GPS_PROVIDER))){
                        cistyStart(Autentifikacia.class, true, getString(R.string.chyba_ziadna_siet_gps));
                    }else{
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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE_GPS);
        } else {
            managerPozicie.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
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
}