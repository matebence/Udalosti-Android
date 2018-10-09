package com.mate.bence.udalosti.Zoznam;

public class Udalost {

    private String idUdalost;
    private String obrazok;
    private String nazov;
    private String den;
    private String mesiac;
    private String cas;
    private String mesto;
    private String ulica;
    private String vstupenka;
    private String zaujemcovia;

    public Udalost(String idUdalost, String obrazok, String nazov, String den, String mesiac, String cas, String mesto, String ulica, String vstupenka, String zaujemcovia) {
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
    }

    public String getIdUdalost() {
        return idUdalost;
    }

    public void setIdUdalost(String idUdalost) {
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

    public String getVstupenka() {
        return vstupenka;
    }

    public void setVstupenka(String vstupenka) {
        this.vstupenka = vstupenka;
    }

    public String getZaujemcovia() {
        return zaujemcovia;
    }

    public void setZaujemcovia(String zaujemcovia) {
        this.zaujemcovia = zaujemcovia;
    }

    @Override
    public String toString() {
        return "Udalost{" +
                "idUdalost='" + idUdalost + '\'' +
                ", obrazok='" + obrazok + '\'' +
                ", nazov='" + nazov + '\'' +
                ", den='" + den + '\'' +
                ", mesiac='" + mesiac + '\'' +
                ", cas='" + cas + '\'' +
                ", mesto='" + mesto + '\'' +
                ", ulica='" + ulica + '\'' +
                ", vstupenka='" + vstupenka + '\'' +
                ", zaujemcovia='" + zaujemcovia + '\'' +
                '}';
    }
}