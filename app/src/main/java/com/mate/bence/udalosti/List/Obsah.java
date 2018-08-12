package com.mate.bence.udalosti.List;

public class Obsah {
    private String obrazok;
    private String meno;

    public Obsah(String obrazok, String meno) {
        this.obrazok = obrazok;
        this.meno = meno;
    }

    public String getObrazok() {
        return obrazok;
    }

    public void setObrazok(String obrazok) {
        this.obrazok = obrazok;
    }

    public String getMeno() {
        return meno;
    }

    public void setMeno(String meno) {
        this.meno = meno;
    }

    @Override
    public String toString() {
        return "Obsah{" +
                "obrazok='" + obrazok + '\'' +
                ", meno='" + meno + '\'' +
                '}';
    }
}