package com.mate.bence.udalosti.List.Oznamenie;

import com.mate.bence.udalosti.List.Obsah;

public class Oznamenie extends Obsah {

    private int idUdalost;
    private int precitana;
    private String nazov;
    private String mesto;

    public Oznamenie(String obrazok, String meno, int idUdalost, int precitana, String nazov, String mesto) {
        super(obrazok, meno);
        this.idUdalost = idUdalost;
        this.precitana = precitana;
        this.nazov = nazov;
        this.mesto = mesto;
    }

    public int getIdUdalost() {
        return idUdalost;
    }

    public void setIdUdalost(int idUdalost) {
        this.idUdalost = idUdalost;
    }

    public int getPrecitana() {
        return precitana;
    }

    public String getNazov() {
        return nazov;
    }

    public void setNazov(String nazov) {
        this.nazov = nazov;
    }

    public String getMesto() {
        return mesto;
    }

    public void setMesto(String mesto) {
        this.mesto = mesto;
    }

    @Override
    public String toString() {
        return "Oznamenie{" +
                "idUdalost=" + idUdalost +
                ", precitana=" + precitana +
                ", nazov='" + nazov + '\'' +
                ", mesto='" + mesto + '\'' +
                '}';
    }
}
