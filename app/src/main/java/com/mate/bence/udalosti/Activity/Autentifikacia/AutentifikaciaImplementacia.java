package com.mate.bence.udalosti.Activity.Autentifikacia;

import android.net.Uri;

public interface AutentifikaciaImplementacia {
    void ucetJeNePristupny(String email);

    void ulozPrihlasovacieUdajeDoDatabazy(String email, String heslo);

    void miestoPrihlasenia(String email, String heslo);

    void prihlasenie(String email, String heslo, String stat, String okres, String mesto);

    void registracia(String meno, String email, String heslo, String potvrd);
}