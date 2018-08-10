package com.mate.bence.udalosti.Zoznam;

public class Udalost {

    private String idUdalost;
    private String obrazok;
    private String nazov;
    private String den;
    private String mesiac;
    private String cas;
    private String miesto;

    public Udalost(String idUdalost, String obrazok, String nazov, String den, String mesiac, String cas, String miesto) {
        this.idUdalost = idUdalost;
        this.obrazok = obrazok;
        this.nazov = nazov;
        this.den = den;
        this.mesiac = mesiac;
        this.cas = cas;
        this.miesto = miesto;
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

    public String getMiesto() {
        return miesto;
    }

    public void setMiesto(String miesto) {
        this.miesto = miesto;
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
                ", miesto='" + miesto + '\'' +
                '}';
    }
}