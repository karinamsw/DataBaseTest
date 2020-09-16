package com.example.myapplication3333;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MedecinDbAdapter {
    public static final String KEY_ROWID = "_id";
    public static final String KEY_PHONE = "phone";
    public static final String KEY_NAME = "name";
    public static final String KEY_SPE = "spe";
    public static final String KEY_Adress = "adress";

    private static final String TAG = "MedecinDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String DATABASE_NAME = "World";
    private static final String SQLITE_TABLE = "Medecin";
    private static final int DATABASE_VERSION = 3;

    private final Context mCtx;

    private static final String DATABASE_CREATE =
            "CREATE TABLE if not exists " + SQLITE_TABLE + " (" +
                    KEY_ROWID + " integer PRIMARY KEY autoincrement," +
                    KEY_PHONE + "," +
                    KEY_NAME + "," +
                    KEY_SPE + "," +
                    KEY_Adress + "," +
                    " UNIQUE (" + KEY_PHONE +"));";

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }


        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.w(TAG, DATABASE_CREATE);
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + SQLITE_TABLE);
            onCreate(db);
        }
    }

    public MedecinDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    public MedecinDbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }

    public long createMedecin(String phone, String name,
                              String spe, String address) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_PHONE, phone);
        initialValues.put(KEY_NAME, name);
        initialValues.put(KEY_SPE, spe);
        initialValues.put(KEY_Adress, address);

        return mDb.insert(SQLITE_TABLE, null, initialValues);
    }

    public boolean deleteAllMedecins() {

        int doneDelete = 0;
        doneDelete = mDb.delete(SQLITE_TABLE, null , null);
        Log.w(TAG, Integer.toString(doneDelete));
        return doneDelete > 0;

    }

    public Cursor fetchMedecinsByName(String inputText) throws SQLException {
        Log.w(TAG, inputText);
        Cursor mCursor = null;
        if (inputText == null  ||  inputText.length () == 0)  {
            mCursor = mDb.query(SQLITE_TABLE, new String[] {KEY_ROWID,
                            KEY_PHONE, KEY_NAME, KEY_SPE, KEY_Adress},
                    null, null, null, null, null);

        }
        else {
            mCursor = mDb.query(true, SQLITE_TABLE, new String[] {KEY_ROWID,
                            KEY_PHONE, KEY_NAME, KEY_SPE, KEY_Adress},
                    KEY_SPE + " like '%" + inputText + "%'", null,
                    null, null, null, null);
        }
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }

    public Cursor fetchAllMedecins() {

        Cursor mCursor = mDb.query(SQLITE_TABLE, new String[] {KEY_ROWID,
                        KEY_PHONE, KEY_NAME, KEY_SPE, KEY_Adress},
                null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public void insertSomeCountries() {

        createMedecin("0664864468","Karina","Pediatre","Chemin des mousseaux ,Vigneux Sur seine ");
        createMedecin("0762017599","Redha","Cardiologue","Alger Centre ");
        //createMedecin("DZA","Algeria","Africa","Maghreb");
       // createMedecin("FR","France","Europe","UE");
        //createMedecin("AND","Andorra","Europe","Southern Europe");
        //createMedecin("AGO","Angola","Africa","Central Africa");
        //createMedecin("AIA","Anguilla","North America","Caribbean");

    }

}

