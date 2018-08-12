package com.mate.bence.udalosti.Udaje.Data;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mate.bence.udalosti.Udaje.Data.Tabulky.Miesto;
import com.mate.bence.udalosti.Udaje.Data.Tabulky.Prihlasenie;

import java.util.HashMap;

public class SQLiteDatabaza extends SQLiteOpenHelper {

    private static final int VERZIA_DATABAZY = 1;
    private static final String NAZOV_DATABAZY = "udalosti";

    public SQLiteDatabaza(Context context) {
        super(context, NAZOV_DATABAZY, null, VERZIA_DATABAZY);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String VYTVOR_TABULKU_PRIHLASENIE =
                "CREATE TABLE " +
                        SQLiteTabulky.Prihlasenie.NAZOV_TABULKY +
                        "(" + SQLiteTabulky.Prihlasenie.ID_STLPCA + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        SQLiteTabulky.Prihlasenie.EMAIL + "  VARCHAR(255)," +
                        SQLiteTabulky.Prihlasenie.HESLO + "  VARCHAR(128)," +
                        SQLiteTabulky.Prihlasenie.MENO + "  VARCHAR(20)," +
                        SQLiteTabulky.Prihlasenie.OBRAZOK + "  VARCHAR(200)" + ")";
        final String VYTVOR_TABULKU_MIESTO =
                "CREATE TABLE " +
                        SQLiteTabulky.Miesto.NAZOV_TABULKY +
                        "(" + SQLiteTabulky.Miesto.ID_STLPCA + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        SQLiteTabulky.Miesto.STAT + "  VARCHAR(30)," +
                        SQLiteTabulky.Miesto.OKRES + "  VARCHAR(30)," +
                        SQLiteTabulky.Miesto.MESTO + "  VARCHAR(30)" + ")";

        sqLiteDatabase.execSQL(VYTVOR_TABULKU_PRIHLASENIE);
        sqLiteDatabase.execSQL(VYTVOR_TABULKU_MIESTO);
    }

    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int staraVerzia, int novaVerzia) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + SQLiteTabulky.Prihlasenie.NAZOV_TABULKY);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + SQLiteTabulky.Miesto.NAZOV_TABULKY);
        onCreate(sqLiteDatabase);
    }

    public void novePouzivatelskeUdaje(Prihlasenie prihlasenie) {
        ContentValues data = new ContentValues();
        data.put(SQLiteTabulky.Prihlasenie.EMAIL, prihlasenie.getEmail());
        data.put(SQLiteTabulky.Prihlasenie.HESLO, prihlasenie.getHeslo());
        data.put(SQLiteTabulky.Prihlasenie.MENO, prihlasenie.getMeno());
        data.put(SQLiteTabulky.Prihlasenie.OBRAZOK, prihlasenie.getObrazok());
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        long id = sqLiteDatabase.insert(SQLiteTabulky.Prihlasenie.NAZOV_TABULKY, null, data);
        sqLiteDatabase.close();
    }

    public void aktualizujPouzivatelskeUdaje(Prihlasenie prihlasenie) {
        ContentValues data = new ContentValues();
        data.put(SQLiteTabulky.Prihlasenie.EMAIL, prihlasenie.getEmail());
        data.put(SQLiteTabulky.Prihlasenie.HESLO, prihlasenie.getHeslo());
        data.put(SQLiteTabulky.Prihlasenie.MENO, prihlasenie.getMeno());
        data.put(SQLiteTabulky.Prihlasenie.OBRAZOK, prihlasenie.getObrazok());
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        int riadok = sqLiteDatabase.update(
                SQLiteTabulky.Prihlasenie.NAZOV_TABULKY,
                data,
                SQLiteTabulky.Prihlasenie.EMAIL + "= ?", new String[]{prihlasenie.getEmail()});
        sqLiteDatabase.close();
    }

    public void odstranPouzivatelskeUdaje(String email) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        int riadok = sqLiteDatabase.delete(
                SQLiteTabulky.Prihlasenie.NAZOV_TABULKY,
                SQLiteTabulky.Prihlasenie.EMAIL + "= ?", new String[]{email});
        sqLiteDatabase.close();
    }

    public boolean pouzivatelskeUdaje() {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String[] stlpce = {SQLiteTabulky.Prihlasenie.EMAIL};
        @SuppressLint("Recycle")
        Cursor data = sqLiteDatabase.query(
                SQLiteTabulky.Prihlasenie.NAZOV_TABULKY,
                stlpce,
                null,
                null,
                null,
                null,
                null);
        boolean riadok = false;
        if (data.moveToFirst()) {
            riadok = true;
            return riadok;
        }
        sqLiteDatabase.close();
        return riadok;
    }

    public HashMap<String, String> vratAktualnehoPouzivatela() {
        HashMap<String, String> pouzivatelskeUdaje;
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String[] stlpce = {SQLiteTabulky.Prihlasenie.EMAIL, SQLiteTabulky.Prihlasenie.HESLO,
                SQLiteTabulky.Prihlasenie.MENO, SQLiteTabulky.Prihlasenie.OBRAZOK};
        @SuppressLint("Recycle")
        Cursor data = sqLiteDatabase.query(
                SQLiteTabulky.Prihlasenie.NAZOV_TABULKY,
                stlpce,
                null,
                null,
                null,
                null,
                null);
        if (data.moveToFirst()) {
            pouzivatelskeUdaje = new HashMap<>();
            pouzivatelskeUdaje.put("email", data.getString(data.getColumnIndex(SQLiteTabulky.Prihlasenie.EMAIL)));
            pouzivatelskeUdaje.put("heslo", data.getString(data.getColumnIndex(SQLiteTabulky.Prihlasenie.HESLO)));
            pouzivatelskeUdaje.put("meno", data.getString(data.getColumnIndex(SQLiteTabulky.Prihlasenie.MENO)));
            pouzivatelskeUdaje.put("obrazok", data.getString(data.getColumnIndex(SQLiteTabulky.Prihlasenie.OBRAZOK)));
            sqLiteDatabase.close();
            return pouzivatelskeUdaje;
        } else {
            sqLiteDatabase.close();
            return null;
        }
    }

    public void noveMiestoPrihlasenia(Miesto miesto) {
        ContentValues data = new ContentValues();
        data.put(SQLiteTabulky.Miesto.STAT, miesto.getStat());
        data.put(SQLiteTabulky.Miesto.OKRES, miesto.getOkres());
        data.put(SQLiteTabulky.Miesto.MESTO, miesto.getMesto());
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        long id = sqLiteDatabase.insert(SQLiteTabulky.Miesto.NAZOV_TABULKY, null, data);
        sqLiteDatabase.close();
    }

    public void aktualizujMiestoPrihlasenie(Miesto miesto) {
        int idMiesto = 0;
        ContentValues data = new ContentValues();
        data.put(SQLiteTabulky.Miesto.STAT, miesto.getStat());
        data.put(SQLiteTabulky.Miesto.OKRES, miesto.getOkres());
        data.put(SQLiteTabulky.Miesto.MESTO, miesto.getMesto());
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        int riadok = sqLiteDatabase.update(
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
        boolean riadok = false;
        if (data.moveToFirst()) {
            riadok = true;
            return riadok;
        }
        sqLiteDatabase.close();
        return riadok;
    }

    public HashMap<String, String> vratMiestoPrihlasenia() {
        HashMap<String, String> miestoPrihlasenia;
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String[] stlpce = {SQLiteTabulky.Miesto.STAT, SQLiteTabulky.Miesto.OKRES,
                SQLiteTabulky.Miesto.MESTO};
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
            miestoPrihlasenia.put("stat", data.getString(data.getColumnIndex(SQLiteTabulky.Miesto.STAT)));
            miestoPrihlasenia.put("okres", data.getString(data.getColumnIndex(SQLiteTabulky.Miesto.OKRES)));
            miestoPrihlasenia.put("mesto", data.getString(data.getColumnIndex(SQLiteTabulky.Miesto.MESTO)));
            sqLiteDatabase.close();
            return miestoPrihlasenia;
        } else {
            sqLiteDatabase.close();
            return null;
        }
    }
}