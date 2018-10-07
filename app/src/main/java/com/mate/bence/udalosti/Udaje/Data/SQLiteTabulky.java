package com.mate.bence.udalosti.Udaje.Data;

public class SQLiteTabulky {

    public static class Miesto {
        public static final String NAZOV_TABULKY = "miesto";
        public static final String ID_STLPCA = "_id";
        public static final String POZICIA = "pozicia";
        public static final String OKRES = "okres";
        public static final String KRAJ = "kraj";
        public static final String PSC = "psc";
        public static final String STAT = "stat";
        public static final String ZNAK_STATU = "znak_statu";
    }

    public static class Pouzivatel {
        public static final String NAZOV_TABULKY = "pouzivatel";
        public static final String ID_STLPCA = "_id";
        public static final String EMAIL = "email";
        public static final String HESLO = "heslo";
    }
}