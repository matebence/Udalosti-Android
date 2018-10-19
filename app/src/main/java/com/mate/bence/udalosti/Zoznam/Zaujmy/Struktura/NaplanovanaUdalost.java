package com.mate.bence.udalosti.Zoznam.Zaujmy.Struktura;

import com.mate.bence.udalosti.Zoznam.Udalost;

public class NaplanovanaUdalost extends Zoznam {

    private Udalost udalost;

    public Udalost getUdalost() {
        return udalost;
    }

    public void setInformacie(Udalost udalost) {
        this.udalost = udalost;
    }

    @Override
    public int cast() {
        return NAPLANOVANA_UDALOST;
    }
}