package com.mate.bence.udalosti.Udaje.Siet.Model.Autentifikator;

public class Validacia {

    private String oznam;
    private String meno;
    private String email;
    private String heslo;
    private String potvrd;
    private String pohlavie;
    private String idTelefonu;

    public Validacia(String oznam, String meno, String email, String heslo, String potvrd, String pohlavie, String idTelefonu) {
        this.oznam = oznam;
        this.meno = meno;
        this.email = email;
        this.heslo = heslo;
        this.potvrd = potvrd;
        this.pohlavie = pohlavie;
        this.idTelefonu = idTelefonu;
    }

    public String getOznam() {
        return oznam;
    }

    public void setOznam(String oznam) {
        this.oznam = oznam;
    }

    public String getMeno() {
        return meno;
    }

    public void setMeno(String meno) {
        this.meno = meno;
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

    public void setPotvrd(String potvrd) {
        this.potvrd = potvrd;
    }

    public String getPohlavie() {
        return pohlavie;
    }

    public void setPohlavie(String pohlavie) {
        this.pohlavie = pohlavie;
    }

    public String getIdTelefonu() {
        return idTelefonu;
    }

    public void setIdTelefonu(String idTelefonu) {
        this.idTelefonu = idTelefonu;
    }

    @Override
    public String toString() {
        return "Validacia{" +
                "oznam='" + oznam + '\'' +
                ", meno='" + meno + '\'' +
                ", email='" + email + '\'' +
                ", heslo='" + heslo + '\'' +
                ", potvrd='" + potvrd + '\'' +
                ", pohlavie='" + pohlavie + '\'' +
                ", idTelefonu='" + idTelefonu + '\'' +
                '}';
    }
}
