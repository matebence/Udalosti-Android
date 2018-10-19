package com.mate.bence.udalosti.Activity.Udalosti;

import java.util.HashMap;

public interface UdalostiImplementacia {
    void zoznamUdalosti(String email, String stat, String token);

    void zoznamUdalostiPodlaPozicie(String email, String stat, String okres, String mesto, String token);

    void automatickePrihlasenieVypnute(String email);

    void odhlasenie(String email);

    HashMap miestoPrihlasenia();

    void zoznamZaujmov(String email, String token);

    void zaujem(String email, String token, int idUdalost);

    void potvrdZaujem(String email, String token, int idUdalost);

    void odstranZaujem(String email, String token, int idUdalost);
}