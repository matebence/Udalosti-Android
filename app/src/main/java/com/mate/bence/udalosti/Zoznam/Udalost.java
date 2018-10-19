package com.mate.bence.udalosti.Zoznam;

public class Udalost {

    private int idUdalost;
    private String obrazok;
    private String nazov;
    private String den;
    private String mesiac;
    private String cas;
    private String mesto;
    private String ulica;
    private float vstupenka;
    private int zaujemcovia;
    private int zaujem;

    public Udalost(int idUdalost, String obrazok, String nazov, String den, String mesiac, String cas, String mesto, String ulica, float vstupenka, int zaujemcovia, int zaujem) {
        this.idUdalost = idUdalost;
        this.obrazok = obrazok;
        this.nazov = nazov;
        this.den = den;
        this.mesiac = mesiac;
        this.cas = cas;
        this.mesto = mesto;
        this.ulica = ulica;
        this.vstupenka = vstupenka;
        this.zaujemcovia = zaujemcovia;
        this.zaujem = zaujem;
    }

    public int getIdUdalost() {
        return idUdalost;
    }

    public void setIdUdalost(int idUdalost) {
        this.idUdalost = idUdalost;
    }

    public String getObrazok() {
        return obrazok;
    }

    public void setObrazok(String obrazok) {
        this.obrazok = obrazok;
    }

    public String getNazov() {
        return nazov;
    }

    public void setNazov(String nazov) {
        this.nazov = nazov;
    }

    public String getDen() {
        return den;
    }

    public void setDen(String den) {
        this.den = den;
    }

    public String getMesiac() {
        return mesiac;
    }

    public void setMesiac(String mesiac) {
        this.mesiac = mesiac;
    }

    public String getCas() {
        return cas;
    }

    public void setCas(String cas) {
        this.cas = cas;
    }

    public String getMesto() {
        return mesto;
    }

    public void setMesto(String mesto) {
        this.mesto = mesto;
    }

    public String getUlica() {
        return ulica;
    }

    public void setUlica(String ulica) {
        this.ulica = ulica;
    }

    public float getVstupenka() {
        return vstupenka;
    }

    public void setVstupenka(float vstupenka) {
        this.vstupenka = vstupenka;
    }

    public int getZaujemcovia() {
        return zaujemcovia;
    }

    public void setZaujemcovia(int zaujemcovia) {
        this.zaujemcovia = zaujemcovia;
    }

    public int getZaujem() {
        return zaujem;
    }

    public void setZaujem(int zaujem) {
        this.zaujem = zaujem;
    }

    @Override
    public String toString() {
        return "Udalost{" +
                "idUdalost=" + idUdalost +
                ", obrazok='" + obrazok + '\'' +
                ", nazov='" + nazov + '\'' +
                ", den='" + den + '\'' +
                ", mesiac='" + mesiac + '\'' +
                ", cas='" + cas + '\'' +
                ", mesto='" + mesto + '\'' +
                ", ulica='" + ulica + '\'' +
                ", vstupenka=" + vstupenka +
                ", zaujemcovia=" + zaujemcovia +
                ", zaujem=" + zaujem +
                '}';
    }
}