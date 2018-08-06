package com.mate.bence.udalosti.Activity.Udalosti;

import java.util.HashMap;

public interface UdalostiImplementacia {
    void automatickePrihlasenieVypnute(String email);

    void odhlasenie(String email);

    HashMap miestoPrihlasenia();
}