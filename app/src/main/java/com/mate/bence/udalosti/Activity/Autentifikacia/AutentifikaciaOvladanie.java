package com.mate.bence.udalosti.Activity.Autentifikacia;

public interface AutentifikaciaOvladanie {
    void tlacidloRegistrovatSa(String meno, String email, String heslo, String potvrd, String pohlavie);

    void tlacidloPrihlasitSa(String email, String heslo);

    void tlacidloZabudnuteHeslo(String email);

    void registracia();

    void zabudnuteHeslo();
}