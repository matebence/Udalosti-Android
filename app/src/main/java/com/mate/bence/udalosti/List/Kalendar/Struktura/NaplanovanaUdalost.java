package com.mate.bence.udalosti.List.Kalendar.Struktura;

public class NaplanovanaUdalost extends Zoznam {

    private Informacie informacie;

    public Informacie getInformacie() {
        return informacie;
    }

    public void setInformacie(Informacie informacie) {
        this.informacie = informacie;
    }

    @Override
    public int cast() {
        return NAPLANOVANA_UDALOST;
    }
}