package com.mate.bence.udalosti.Activity.Navigacia.Fragment.Udalosti;

public interface UdalostiImplementacia {
    void zoznamUdalosti(String email, String idUdalost, String datum, String stat, int od, int pocet, String token);

    void zoznamNaplanovanychUdalosti(String email, int od, int pocet, String token);

    void zoznamOznameniZiadosti(String email, int od, int pocet, String token);

    void hladanaUdalost(String email, String nazov, String stat, String token);

    void zaujemOudalost(String email, int idUdalost, String token);

    void odstranenieUdalostiKalendara(String email, int idZaujemUdalosti, String token);

    void odpovedNaZiadost(String email, int odpoved, int idZiadost, String token);
}