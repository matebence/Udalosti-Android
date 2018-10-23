package com.mate.bence.udalosti.Activity.Autentifikacia;

import android.widget.EditText;

public interface AutentifikaciaOvladanie {
    void tlacidloRegistrovatSa(EditText meno, EditText email, EditText heslo, EditText potvrd);

    void tlacidloPrihlasitSa(EditText email, EditText heslo);

    void registracia();
}