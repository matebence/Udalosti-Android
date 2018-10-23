package com.mate.bence.udalosti.Activity.Autentifikacia;

public interface AutentifikaciaImplementacia {
    void ucetJeNePristupny(String email);

    void ulozPrihlasovacieUdajeDoDatabazy(String email, String heslo);

    void miestoPrihlasenia(String email, String heslo, double zemepisnaSirka, double zemepisnaDlzka, boolean geo, boolean aktualizuj);

    void miestoPrihlasenia(String email, String heslo, boolean ip);

    void prihlasenie(String email, String heslo);

    void registracia(String meno, String email, String heslo, String potvrd);
}