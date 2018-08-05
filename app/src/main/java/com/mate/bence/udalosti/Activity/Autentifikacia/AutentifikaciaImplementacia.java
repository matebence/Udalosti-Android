package com.mate.bence.udalosti.Activity.Autentifikacia;

import android.net.Uri;

public interface AutentifikaciaImplementacia {
    String registracneCisloZariadeniu();

    void ulozObrazok(String nazovObrazka);

    boolean zistiCiObrazokJeUlozeny(String nazovObrazka);

    void ucetJeNePristupny(String email);

    void ulozPrihlasovacieUdajeDoDatabazy(String email, String heslo, String meno, String obrazok);

    void miestoPrihlasenia(final String email, final String heslo);

    void prihlasenie(final String email, final String heslo, final String stat, final String okres, final String mesto);

    void registracia(final Uri fileUri, final String meno, final String email, final String heslo, final String potvrd, final String pohlavie, final String kluc);

    void zabudnuteHeslo(final String email);
}