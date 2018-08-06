package com.mate.bence.udalosti.Zoznam;

public class Udalost {

    private String idUdalost;
    private String obrazok;
    private String nazov;
    private String datum;
    private String cas;
    private String miesto;

    public Udalost(String idUdalost, String obrazok, String nazov, String datum, String cas, String miesto) {
        this.idUdalost = idUdalost;
        this.obrazok = obrazok;
        this.nazov = nazov;
        this.datum = datum;
        this.cas = cas;
        this.miesto = miesto;
    }

    public String getIdUdalost() {
        return idUdalost;
    }

    public String getObrazok() {
        return obrazok;
    }

    public String getNazov() {
        return nazov;
    }

    public String getDatum() {
        return datum;
    }

    public String getCas() {
        return cas;
    }

    public String getMiesto() {
        return miesto;
    }

    @Override
    public String toString() {
        return "Udalost{" +
                "idUdalost='" + idUdalost + '\'' +
                ", obrazok='" + obrazok + '\'' +
                ", nazov='" + nazov + '\'' +
                ", datum='" + datum + '\'' +
                ", cas='" + cas + '\'' +
                ", miesto='" + miesto + '\'' +
                '}';
    }
}