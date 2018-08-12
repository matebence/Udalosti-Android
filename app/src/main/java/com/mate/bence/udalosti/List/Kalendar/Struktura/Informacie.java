package com.mate.bence.udalosti.List.Kalendar.Struktura;

public class Informacie {

    private int idZaujem;
    private String datum;
    private String nazov;
    private String mesto;

    public Informacie(int idZaujem, String datum, String nazov, String mesto) {
        this.idZaujem = idZaujem;
        this.datum = datum;
        this.nazov = nazov;
        this.mesto = mesto;
    }

    public int getIdZaujem() {
        return idZaujem;
    }

    public String getDatum() {
        return datum;
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
        return "Informacie{" +
                "idZaujem=" + idZaujem +
                ", datum='" + datum + '\'' +
                ", nazov='" + nazov + '\'' +
                ", mesto='" + mesto + '\'' +
                '}';
    }
}