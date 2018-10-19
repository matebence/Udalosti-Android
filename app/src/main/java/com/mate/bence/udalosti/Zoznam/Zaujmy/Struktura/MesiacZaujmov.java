package com.mate.bence.udalosti.Zoznam.Zaujmy.Struktura;

import com.mate.bence.udalosti.Zoznam.Udalost;

public class MesiacZaujmov extends Zaujem {

    private Udalost udalost;

    public Udalost getUdalost() {
        return udalost;
    }

    public void setUdalost(Udalost udalost) {
        this.udalost = udalost;
    }

    @Override
    public int struktura() {
        return MESIAC_ZAUJMOV;
    }
}