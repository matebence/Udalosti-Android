package com.mate.bence.udalosti.Zoznam;

public class Udalost {

    private String idUdalost;
    private String obrazok;
    private String den;
    private String mesiac;
    private String nazov;
    private String mesto;
    private String miesto;
    private String cas;

    public Udalost(String idUdalost, String obrazok, String den, String mesiac, String nazov, String mesto, String miesto, String cas) {
        this.idUdalost = idUdalost;
        this.obrazok = obrazok;
        this.den = den;
        this.mesiac = mesiac;
        this.nazov = nazov;
        this.mesto = mesto;
        this.miesto = miesto;
        this.cas = cas;
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

    public String getMiesto() {
        return miesto;
    }

    public void setMiesto(String miesto) {
        this.miesto = miesto;
    }

    public String getCas() {
        return cas;
    }

    public void setCas(String cas) {
        this.cas = cas;
    }

    @Override
    public String toString() {
        return "Udalost{" +
                "idUdalost='" + idUdalost + '\'' +
                ", obrazok='" + obrazok + '\'' +
                ", den='" + den + '\'' +
                ", mesiac='" + mesiac + '\'' +
                ", nazov='" + nazov + '\'' +
                ", mesto='" + mesto + '\'' +
                ", miesto='" + miesto + '\'' +
                ", cas='" + cas + '\'' +
                '}';
    }
}