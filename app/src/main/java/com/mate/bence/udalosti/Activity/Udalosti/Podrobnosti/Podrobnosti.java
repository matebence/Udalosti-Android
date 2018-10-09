package com.mate.bence.udalosti.Activity.Udalosti.Podrobnosti;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mate.bence.udalosti.R;

public class Podrobnosti extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_podrobnosti);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.ukoncit_vychod_activity, R.anim.ukoncit_vchod_activity);
    }
}