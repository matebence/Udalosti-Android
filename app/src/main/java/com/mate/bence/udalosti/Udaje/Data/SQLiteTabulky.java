package com.mate.bence.udalosti.Udaje.Data;

class SQLiteTabulky {

    static class Miesto {
        static final String NAZOV_TABULKY = "miesto";
        static final String ID_STLPCA = "_id";
        static final String POZICIA = "pozicia";
        static final String OKRES = "okres";
        static final String KRAJ = "kraj";
        static final String PSC = "psc";
        static final String STAT = "stat";
        static final String ZNAK_STATU = "znak_statu";
    }

    static class Pouzivatel {
        static final String NAZOV_TABULKY = "pouzivatel";
        static final String ID_STLPCA = "_id";
        static final String EMAIL = "email";
        static final String HESLO = "heslo";
    }
}