package com.mate.bence.udalosti.Udaje.Siet.Model.Autentifikator;

public class Pouzivatel {

    private String token;

    public Pouzivatel(String token) {
        this.token = token;
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
                "token='" + token + '\'' +
                '}';
    }
}
