package com.mate.bence.udalosti.Activity.Navigacia.Fragment.Nastavenia;

import android.net.Uri;

public interface NastaveniaImplementacia {
    String hesloPrePotvrdenie();

    String fotkaPouzivatela();

    void odstranPouzivatelskeKonto(String email, String token);

    void aktualizujPouzivatelskeKonto(String email, String meno, String heslo, String potvrdenie, Uri obrazok, String token, String nahodnyRetazec);
}
