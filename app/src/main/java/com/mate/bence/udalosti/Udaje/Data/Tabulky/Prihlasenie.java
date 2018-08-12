package com.mate.bence.udalosti.Udaje.Data.Tabulky;

public class Prihlasenie {

    private String email;
    private String heslo;
    private String meno;
    private String obrazok;

    public Prihlasenie(String email, String heslo, String meno, String obrazok) {
        this.email = email;
        this.heslo = heslo;
        this.meno = meno;
        this.obrazok = obrazok;
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

    @Override
    public String toString() {
        return "Prihlasenie{" +
                "email='" + email + '\'' +
                ", heslo='" + heslo + '\'' +
                ", meno='" + meno + '\'' +
                ", obrazok='" + obrazok + '\'' +
                '}';
    }
}
