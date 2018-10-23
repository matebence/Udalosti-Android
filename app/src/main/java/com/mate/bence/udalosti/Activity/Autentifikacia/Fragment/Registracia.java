package com.mate.bence.udalosti.Activity.Autentifikacia.Fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.mate.bence.udalosti.Activity.Autentifikacia.AutentifikaciaOvladanie;
import com.mate.bence.udalosti.R;

public class Registracia extends Fragment implements View.OnClickListener {

    private static final String TAG = Registracia.class.getName();

    private AutentifikaciaOvladanie autentifikaciaOvladanie;
    private EditText meno, email, heslo, potrvd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_autentifikacia_registracia, container, false);
        return init(view);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.autentifikaciaOvladanie = (AutentifikaciaOvladanie) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(Registracia.TAG + " Interface AutentifikaciaOvladanie nie je implementovana");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.posli_registraciu:
                this.meno.setFocusable(false);
                this.email.setFocusable(false);
                this.heslo.setFocusable(false);
                this.potrvd.setFocusable(false);
                this.autentifikaciaOvladanie.tlacidloRegistrovatSa(this.meno, this.email, this.heslo, this.potrvd);
                break;
        }
    }

    private View init(View view) {
        Log.v(Registracia.TAG, "Metoda init - Registracia bola vykonana");

        Button registracia = view.findViewById(R.id.posli_registraciu);
        registracia.setOnClickListener(this);

        this.meno = view.findViewById(R.id.registracia_meno);
        this.email = view.findViewById(R.id.registracia_email);
        this.heslo = view.findViewById(R.id.registracia_heslo);
        this.potrvd = view.findViewById(R.id.registracia_potvrd);

        return view;
    }
}