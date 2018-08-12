package com.mate.bence.udalosti.Activity.Autentifikacia.Fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.mate.bence.udalosti.Activity.Autentifikacia.AutentifikaciaOvladanie;
import com.mate.bence.udalosti.R;

public class ZabudnuteHeslo extends Fragment implements View.OnClickListener {

    private static final String TAG = Registracia.class.getName();
    private AutentifikaciaOvladanie autentifikaciaOvladanie;
    private EditText email;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_autentifikacia_zabudnute_heslo, container, false);
        init(view);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            autentifikaciaOvladanie = (AutentifikaciaOvladanie) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(TAG + " Interface AutentifikaciaOvladanie nie je implementovana");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.posli_zabudnute_heslo:
                autentifikaciaOvladanie.tlacidloZabudnuteHeslo(email.getText().toString());
                break;
        }
    }

    private void init(View view) {
        Button registracia = view.findViewById(R.id.posli_zabudnute_heslo);
        registracia.setOnClickListener(this);

        this.email = view.findViewById(R.id.vstup_zabudnute_heslo);
    }
}