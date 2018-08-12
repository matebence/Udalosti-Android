package com.mate.bence.udalosti.Activity.Navigacia;

import java.util.HashMap;

public interface NavigaciaImplementacia {
    void automatickePrihlasenieVypnute(String email);

    void odhlasenie(String email);

    HashMap miestoPrihlasenia();
}