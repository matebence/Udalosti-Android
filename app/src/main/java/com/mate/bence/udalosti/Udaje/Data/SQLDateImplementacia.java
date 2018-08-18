package com.mate.bence.udalosti.Udaje.Data;

import com.mate.bence.udalosti.Udaje.Data.Tabulky.Miesto;
import com.mate.bence.udalosti.Udaje.Data.Tabulky.Pouzivatel;

import java.util.HashMap;

public interface SQLDateImplementacia {
    void novePouzivatelskeUdaje(Pouzivatel pouzivatel);

    void aktualizujPouzivatelskeUdaje(Pouzivatel pouzivatel);

    void odstranPouzivatelskeUdaje(String email);

    void noveMiestoPrihlasenia(Miesto miesto);

    void aktualizujMiestoPrihlasenia(Miesto miesto);

    boolean pouzivatelskeUdaje();

    boolean miestoPrihlasenia();

    HashMap<String, String> vratAktualnehoPouzivatela();

    HashMap<String, String> vratMiestoPrihlasenia();
}