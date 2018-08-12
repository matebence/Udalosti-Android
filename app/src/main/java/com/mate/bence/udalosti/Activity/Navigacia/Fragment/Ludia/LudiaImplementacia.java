package com.mate.bence.udalosti.Activity.Navigacia.Fragment.Ludia;

public interface LudiaImplementacia {
    void zoznamPouzivatelov(String email, int od, int pocet, String token);

    void hladanyPouzivatel(String email, String meno, String token);

    void zoznamPriatelov(String email, int od, int pocet, String token);

    void novaZiadost(String email, int pouzivatel, String token);

    void odstraneniePriatelstva(String email, int pouzivatel, String token);
}