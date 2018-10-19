package com.mate.bence.udalosti.Zoznam.Udalosti;

import android.view.View;

import com.mate.bence.udalosti.Zoznam.Udalost;

public interface ZvolenaUdalost {
    void podrobnostiUdalosti(View view, int pozicia);
    void podrobnostiUdalosti(int pozicia, Udalost udalost);
}