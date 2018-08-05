package com.mate.bence.udalosti.Udaje.Data.Tabulky;

public class Pouzivatel {

    private String email;
    private String heslo;

    public Pouzivatel(String email, String heslo) {
        this.email = email;
        this.heslo = heslo;
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

    @Override
    public String toString() {
        return "Pouzivatel{" +
                "email='" + email + '\'' +
                ", heslo='" + heslo + '\'' +
                '}';
    }
}
