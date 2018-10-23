package com.mate.bence.udalosti.Nastroje;

import android.location.LocationListener;
import android.location.LocationManager;
import android.os.CountDownTimer;
import android.util.Log;

import com.mate.bence.udalosti.Udaje.Siet.Model.DlzkaRequestu;

public class Casovac extends CountDownTimer {

    private static final String TAG = Casovac.class.getName();

    private LocationManager managerPozicie;
    private LocationListener locationListener;
    private DlzkaRequestu dlzkaRequestu;

    public Casovac(int od, LocationManager managerPozicie, LocationListener locationListener, DlzkaRequestu dlzkaRequestu){
        super(od,1000);

        Log.v(Casovac.TAG, "Metoda Casovac bola vykonana");

        this.managerPozicie = managerPozicie;
        this.locationListener = locationListener;
        this.dlzkaRequestu = dlzkaRequestu;
    }

    @Override
    public void onTick(long millisUntilFinished) {
    }

    @Override
    public void onFinish() {
        this.dlzkaRequestu.zistiPoziciuPodlaSiete();
        this.managerPozicie.removeUpdates(this.locationListener);
    }
}