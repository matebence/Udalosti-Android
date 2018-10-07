package com.mate.bence.udalosti.Udaje.Data;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mate.bence.udalosti.Udaje.Data.Tabulky.Miesto;
import com.mate.bence.udalosti.Udaje.Data.Tabulky.Pouzivatel;

import java.util.HashMap;

public class SQLiteDatabaza extends SQLiteOpenHelper implements SQLDateImplementacia{

    private static final int VERZIA_DATABAZY = 1;
    private static final String NAZOV_DATABAZY = "udalosti";

    public SQLiteDatabaza(Context context) {
        super(context, NAZOV_DATABAZY, null, VERZIA_DATABAZY);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String VYTVOR_TABULKU_POUZIVATEL =
                "CREATE TABLE " +
                        SQLiteTabulky.Pouzivatel.NAZOV_TABULKY +
                        "(" + SQLiteTabulky.Pouzivatel.ID_STLPCA + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        SQLiteTabulky.Pouzivatel.EMAIL + "  VARCHAR(255)," +
                        SQLiteTabulky.Pouzivatel.HESLO + "  VARCHAR(128)" + ")";

        final String VYTVOR_TABULKU_MIESTO =
                "CREATE TABLE " +
                        SQLiteTabulky.Miesto.NAZOV_TABULKY +
                        "(" + SQLiteTabulky.Miesto.ID_STLPCA + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        SQLiteTabulky.Miesto.POZICIA + "  VARCHAR(30)," +
                        SQLiteTabulky.Miesto.OKRES + "  VARCHAR(30)," +
                        SQLiteTabulky.Miesto.KRAJ + "  VARCHAR(30)," +
                        SQLiteTabulky.Miesto.PSC + "  VARCHAR(10)," +
                        SQLiteTabulky.Miesto.STAT + "  VARCHAR(30)," +
                        SQLiteTabulky.Miesto.ZNAK_STATU + "  VARCHAR(10)" + ")";

        sqLiteDatabase.execSQL(VYTVOR_TABULKU_POUZIVATEL);
        sqLiteDatabase.execSQL(VYTVOR_TABULKU_MIESTO);
    }

    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int staraVerzia, int novaVerzia) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + SQLiteTabulky.Pouzivatel.NAZOV_TABULKY);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + SQLiteTabulky.Miesto.NAZOV_TABULKY);
        onCreate(sqLiteDatabase);
    }

    public void novePouzivatelskeUdaje(Pouzivatel pouzivatel) {
        ContentValues data = new ContentValues();
        data.put(SQLiteTabulky.Pouzivatel.EMAIL, pouzivatel.getEmail());
        data.put(SQLiteTabulky.Pouzivatel.HESLO, pouzivatel.getHeslo());
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.insert(SQLiteTabulky.Pouzivatel.NAZOV_TABULKY, null, data);
        sqLiteDatabase.close();
    }

    public void aktualizujPouzivatelskeUdaje(Pouzivatel pouzivatel) {
        ContentValues data = new ContentValues();
        data.put(SQLiteTabulky.Pouzivatel.EMAIL, pouzivatel.getEmail());
        data.put(SQLiteTabulky.Pouzivatel.HESLO, pouzivatel.getHeslo());
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.update(
                SQLiteTabulky.Pouzivatel.NAZOV_TABULKY,
                data,
                SQLiteTabulky.Pouzivatel.EMAIL + "= ?", new String[]{pouzivatel.getEmail()});
        sqLiteDatabase.close();
    }

    public void odstranPouzivatelskeUdaje(String email) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.delete(
                SQLiteTabulky.Pouzivatel.NAZOV_TABULKY,
                SQLiteTabulky.Pouzivatel.EMAIL + "= ?", new String[]{email});
        sqLiteDatabase.close();
    }

    public boolean pouzivatelskeUdaje() {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String[] stlpce = {SQLiteTabulky.Pouzivatel.EMAIL};
        @SuppressLint("Recycle")
        Cursor data = sqLiteDatabase.query(
                SQLiteTabulky.Pouzivatel.NAZOV_TABULKY,
                stlpce,
                null,
                null,
                null,
                null,
                null);
        if (data.moveToFirst()) {
            return true;
        }
        sqLiteDatabase.close();
        return false;
    }

    public HashMap<String, String> vratAktualnehoPouzivatela() {
        HashMap<String, String> pouzivatelskeUdaje;
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String[] stlpce = {SQLiteTabulky.Pouzivatel.EMAIL, SQLiteTabulky.Pouzivatel.HESLO};
        @SuppressLint("Recycle")
        Cursor data = sqLiteDatabase.query(
                SQLiteTabulky.Pouzivatel.NAZOV_TABULKY,
                stlpce,
                null,
                null,
                null,
                null,
                null);
        if (data.moveToFirst()) {
            pouzivatelskeUdaje = new HashMap<>();
            pouzivatelskeUdaje.put("email", data.getString(data.getColumnIndex(SQLiteTabulky.Pouzivatel.EMAIL)));
            pouzivatelskeUdaje.put("heslo", data.getString(data.getColumnIndex(SQLiteTabulky.Pouzivatel.HESLO)));
            sqLiteDatabase.close();
            return pouzivatelskeUdaje;
        } else {
            sqLiteDatabase.close();
            return null;
        }
    }

    public void noveMiestoPrihlasenia(Miesto miesto) {
        ContentValues data = new ContentValues();
        data.put(SQLiteTabulky.Miesto.POZICIA, miesto.getPozicia());
        data.put(SQLiteTabulky.Miesto.OKRES, miesto.getOkres());
        data.put(SQLiteTabulky.Miesto.KRAJ, miesto.getKraj());
        data.put(SQLiteTabulky.Miesto.PSC, miesto.getPsc());
        data.put(SQLiteTabulky.Miesto.STAT, miesto.getStat());
        data.put(SQLiteTabulky.Miesto.ZNAK_STATU, miesto.getZnakStatu());
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.insert(SQLiteTabulky.Miesto.NAZOV_TABULKY, null, data);
        sqLiteDatabase.close();
    }

    public void aktualizujMiestoPrihlasenia(Miesto miesto) {
        int idMiesto = 1;
        ContentValues data = new ContentValues();
        data.put(SQLiteTabulky.Miesto.POZICIA, miesto.getPozicia());
        data.put(SQLiteTabulky.Miesto.OKRES, miesto.getOkres());
        data.put(SQLiteTabulky.Miesto.KRAJ, miesto.getKraj());
        data.put(SQLiteTabulky.Miesto.PSC, miesto.getPsc());
        data.put(SQLiteTabulky.Miesto.STAT, miesto.getStat());
        data.put(SQLiteTabulky.Miesto.ZNAK_STATU, miesto.getZnakStatu());
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.update(
                SQLiteTabulky.Miesto.NAZOV_TABULKY,
                data,
                SQLiteTabulky.Miesto.ID_STLPCA + "= ?", new String[]{Integer.toString(idMiesto)});
        sqLiteDatabase.close();
    }

    public boolean miestoPrihlasenia() {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String[] stlpce = {SQLiteTabulky.Miesto.STAT};
        @SuppressLint("Recycle")
        Cursor data = sqLiteDatabase.query(
                SQLiteTabulky.Miesto.NAZOV_TABULKY,
                stlpce,
                null,
                null,
                null,
                null,
                null);
        if (data.moveToFirst()) {
            return true;
        }
        sqLiteDatabase.close();
        return false;
    }

    public HashMap<String, String> vratMiestoPrihlasenia() {
        HashMap<String, String> miestoPrihlasenia;
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String[] stlpce = {SQLiteTabulky.Miesto.POZICIA, SQLiteTabulky.Miesto.OKRES,
                SQLiteTabulky.Miesto.KRAJ, SQLiteTabulky.Miesto.PSC,
                SQLiteTabulky.Miesto.STAT, SQLiteTabulky.Miesto.ZNAK_STATU};
        @SuppressLint("Recycle")
        Cursor data = sqLiteDatabase.query(
                SQLiteTabulky.Miesto.NAZOV_TABULKY,
                stlpce,
                null,
                null,
                null,
                null,
                null);
        if (data.moveToFirst()) {
            miestoPrihlasenia = new HashMap<>();
            miestoPrihlasenia.put("pozicia", data.getString(data.getColumnIndex(SQLiteTabulky.Miesto.POZICIA)));
            miestoPrihlasenia.put("okres", data.getString(data.getColumnIndex(SQLiteTabulky.Miesto.OKRES)));
            miestoPrihlasenia.put("kraj", data.getString(data.getColumnIndex(SQLiteTabulky.Miesto.KRAJ)));
            miestoPrihlasenia.put("psc", data.getString(data.getColumnIndex(SQLiteTabulky.Miesto.PSC)));
            miestoPrihlasenia.put("stat", data.getString(data.getColumnIndex(SQLiteTabulky.Miesto.STAT)));
            miestoPrihlasenia.put("znakStatu", data.getString(data.getColumnIndex(SQLiteTabulky.Miesto.ZNAK_STATU)));
            sqLiteDatabase.close();
            return miestoPrihlasenia;
        } else {
            sqLiteDatabase.close();
            return null;
        }
    }
}