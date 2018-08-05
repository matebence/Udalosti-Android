package com.mate.bence.udalosti.Udaje.Data;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mate.bence.udalosti.Udaje.Data.Tabulky.Miesto;

import java.util.HashMap;

public class SQLiteDatabaza extends SQLiteOpenHelper {

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
                        SQLiteTabulky.Pouzivatel.HESLO + "  VARCHAR(128)" +")";

        final String VYTVOR_TABULKU_MIESTO =
                "CREATE TABLE " +
                        SQLiteTabulky.Miesto.NAZOV_TABULKY +
                        "(" + SQLiteTabulky.Miesto.ID_STLPCA + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                        SQLiteTabulky.Miesto.STAT + "  VARCHAR(30)," +
                        SQLiteTabulky.Miesto.OKRES + "  VARCHAR(30)," +
                        SQLiteTabulky.Miesto.MESTO + "  VARCHAR(30)" + ")";

        sqLiteDatabase.execSQL(VYTVOR_TABULKU_POUZIVATEL);
        sqLiteDatabase.execSQL(VYTVOR_TABULKU_MIESTO);
    }

    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int staraVerzia, int novaVerzia) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + SQLiteTabulky.Pouzivatel.NAZOV_TABULKY);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + SQLiteTabulky.Miesto.NAZOV_TABULKY);
        onCreate(sqLiteDatabase);
    }

    public long novePouzivatelskeUdaje(com.mate.bence.udalosti.Udaje.Data.Tabulky.Pouzivatel pouzivatel) {
        ContentValues data = new ContentValues();
        data.put(SQLiteTabulky.Pouzivatel.EMAIL, pouzivatel.getEmail());
        data.put(SQLiteTabulky.Pouzivatel.HESLO, pouzivatel.getHeslo());
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        long id = sqLiteDatabase.insert(SQLiteTabulky.Pouzivatel.NAZOV_TABULKY, null, data);
        sqLiteDatabase.close();
        return id;
    }

    public boolean aktualizujPouzivatelskeUdaje(com.mate.bence.udalosti.Udaje.Data.Tabulky.Pouzivatel pouzivatel) {
        ContentValues data = new ContentValues();
        data.put(SQLiteTabulky.Pouzivatel.EMAIL, pouzivatel.getEmail());
        data.put(SQLiteTabulky.Pouzivatel.HESLO, pouzivatel.getHeslo());
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        int riadok = sqLiteDatabase.update(
                SQLiteTabulky.Pouzivatel.NAZOV_TABULKY,
                data,
                SQLiteTabulky.Pouzivatel.EMAIL + "= ?", new String[]{pouzivatel.getEmail()});
        sqLiteDatabase.close();
        return riadok > 0;
    }

    public boolean odstranPouzivatelskeUdaje(String email) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        int riadok = sqLiteDatabase.delete(
                SQLiteTabulky.Pouzivatel.NAZOV_TABULKY,
                SQLiteTabulky.Pouzivatel.EMAIL + "= ?", new String[]{email});
        sqLiteDatabase.close();
        return riadok > 0;
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

    public long noveMiestoPrihlasenia(Miesto miesto) {
        ContentValues data = new ContentValues();
        data.put(SQLiteTabulky.Miesto.STAT, miesto.getStat());
        data.put(SQLiteTabulky.Miesto.OKRES, miesto.getOkres());
        data.put(SQLiteTabulky.Miesto.MESTO, miesto.getMesto());
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        long id = sqLiteDatabase.insert(SQLiteTabulky.Miesto.NAZOV_TABULKY, null, data);
        sqLiteDatabase.close();
        return id;
    }

    public boolean aktualizujMiestoPrihlasenia(Miesto miesto) {
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
        return riadok > 0;
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