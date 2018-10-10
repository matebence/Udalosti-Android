package com.mate.bence.udalosti.Udaje.Siet.Model.Autentifikator;

public class Validacia {

    private String oznam;
    private String meno;
    private String email;
    private String heslo;
    private String potvrd;

    public Validacia(String oznam, String meno, String email, String heslo, String potvrd) {
        this.oznam = oznam;
        this.meno = meno;
        this.email = email;
        this.heslo = heslo;
        this.potvrd = potvrd;
    }

    public String getOznam() {
        return oznam;
    }

    public String getMeno() {
        return meno;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHeslo() {
        return heslo;
    }

    public void setHeslo(String heslo) {
        this.heslo = heslo;
    }

    public String getPotvrd() {
        return potvrd;
    }

    @Override
    public String toString() {
        return "Validacia{" +
                "oznam='" + oznam + '\'' +
                ", meno='" + meno + '\'' +
                ", email='" + email + '\'' +
                ", heslo='" + heslo + '\'' +
                ", potvrd='" + potvrd + '\'' +
                '}';
    }
}