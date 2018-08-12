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

public class Prihlasenie extends Fragment implements View.OnClickListener {

    private static final String TAG = Prihlasenie.class.getName();
    private AutentifikaciaOvladanie autentifikaciaOvladanie;
    private EditText email, heslo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_autentifikacia_prihlasenie, container, false);
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
            case R.id.prihlasit_sa:
                autentifikaciaOvladanie.tlacidloPrihlasitSa(email.getText().toString(), heslo.getText().toString());
                break;
            case R.id.registrovat_sa:
                autentifikaciaOvladanie.registracia();
                break;
        }
    }

    private void init(View view) {
        Button prihlasenie = view.findViewById(R.id.prihlasit_sa);
        prihlasenie.setOnClickListener(this);

        Button registracia = view.findViewById(R.id.registrovat_sa);
        registracia.setOnClickListener(this);

        this.email = view.findViewById(R.id.prihlasenie_email_vstup);
        this.heslo = view.findViewById(R.id.prihlasenie_heslo_vstup);
    }
}
