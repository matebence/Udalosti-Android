package com.mate.bence.udalosti.Udaje.Siet.Model.Autentifikator;

public class Pouzivatel {

    private String meno;
    private String obrazok;
    private String token;

    public Pouzivatel(String meno, String obrazok, String token) {
        this.meno = meno;
        this.obrazok = obrazok;
        this.token = token;
    }

    public String getMeno() {
        return meno;
    }

    public void setMeno(String meno) {
        this.meno = meno;
    }

    public String getObrazok() {
        return obrazok;
    }

    public void setObrazok(String obrazok) {
        this.obrazok = obrazok;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "Pouzivatel{" +
                "meno='" + meno + '\'' +
                ", obrazok='" + obrazok + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}
