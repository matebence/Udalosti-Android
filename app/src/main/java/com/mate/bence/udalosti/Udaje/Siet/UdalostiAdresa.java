package com.mate.bence.udalosti.Udaje.Siet;

public class UdalostiAdresa {
    private UdalostiAdresa() {
    }

    private static final String ADRESA = "https://bmate18.student.ki.fpv.ukf.sk/";

    public static Requesty initAdresu() {
        return HTTPRequest.posliRequest(ADRESA).create(Requesty.class);
    }

    public static String getAdresa() {
        return ADRESA;
    }
}