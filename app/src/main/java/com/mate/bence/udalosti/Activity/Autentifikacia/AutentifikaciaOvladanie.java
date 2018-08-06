package com.mate.bence.udalosti.Activity.Autentifikacia;

public interface AutentifikaciaOvladanie {
    void tlacidloRegistrovatSa(String meno, String email, String heslo, String potvrd);

    void tlacidloPrihlasitSa(String email, String heslo);

    void registracia();
}