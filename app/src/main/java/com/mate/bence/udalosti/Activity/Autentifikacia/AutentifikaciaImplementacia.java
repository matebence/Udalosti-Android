package com.mate.bence.udalosti.Activity.Autentifikacia;

import android.net.Uri;

public interface AutentifikaciaImplementacia {
    String registracneCisloZariadeniu();

    void ulozObrazok(String nazovObrazka);

    boolean zistiCiObrazokJeUlozeny(String nazovObrazka);

    void ucetJeNePristupny(String email);

    void ulozPrihlasovacieUdajeDoDatabazy(String email, String heslo, String meno, String obrazok);

    void miestoPrihlasenia(String email, String heslo);

    void prihlasenie(String email, String heslo, String stat, String okres, String mesto);

    void registracia(Uri fileUri, String meno, String email, String heslo, String potvrd, String pohlavie, String kluc);

    void zabudnuteHeslo(String email);
}