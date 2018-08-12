package com.mate.bence.udalosti.List.Udalosti;

public class Udalost {

    private String idZaujem;
    private String idUdalost;
    private String obrazok;
    private String nazov;
    private String datum;
    private String cas;
    private String mesto;

    public Udalost(String idZaujem, String idUdalost, String obrazok, String nazov, String datum, String cas, String mesto) {
        this.idZaujem = idZaujem;
        this.idUdalost = idUdalost;
        this.obrazok = obrazok;
        this.nazov = nazov;
        this.datum = datum;
        this.cas = cas;
        this.mesto = mesto;
    }

    public String getIdZaujem() {
        return idZaujem;
    }

    public void setIdZaujem(String idZaujem) {
        this.idZaujem = idZaujem;
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

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
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
}