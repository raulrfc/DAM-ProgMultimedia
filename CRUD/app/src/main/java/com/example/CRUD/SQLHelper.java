package com.example.CRUD;

import android.content.ContentValues;
import android.content.Context;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLHelper extends SQLiteOpenHelper {
    static final String DATABASE_NAME = "Wishlist";
    static final String DATABASE_TABLE = "Wishes";
    static final String KEY_ID = "ID";
    static final String KEY_NAME = "Name";
    static final String KEY_URL = "URL";
    static final String KEY_PRICE = "Price";
    static final String KEY_RATING = "Rating";
    static final String DATABASE_CREATE = "CREATE TABLE Wishes (ID INTEGER PRIMARY KEY AUTOINCREMENT, Name TEXT, Price TEXT, URL TEXT, Rating FLOAT)";

    public SQLHelper(Context contexto, CursorFactory factory, int version) {
        super(contexto, DATABASE_NAME, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnterior, int versionNueva) {
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
        db.execSQL(DATABASE_CREATE);
        Log.e("upgrade", "Actualización realizada con exito");
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int versionAnterior, int versionNueva) {
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
        db.execSQL(DATABASE_CREATE);
        Log.e("downgrade", "Retroactualización realizada con exito");
    }

    public long insertWish(SQLiteDatabase db,String name, String url, String price, float rating) {
        ContentValues values = new ContentValues();

        values.put(KEY_NAME, name);
        values.put(KEY_URL, url);
        values.put(KEY_PRICE, price);
        values.put(KEY_RATING, rating);

        return db.insert(DATABASE_TABLE, null, values);
    }

    public boolean deleteWish(SQLiteDatabase db, int id) {
        return db.delete(DATABASE_TABLE, KEY_ID + "=" + id, null) > 0;
    }

    public boolean updateWish(SQLiteDatabase db, String name, String url, String price, float rating) {
        ContentValues values = new ContentValues();

        values.put(KEY_NAME, name);
        values.put(KEY_URL, url);
        values.put(KEY_PRICE, price);
        values.put(KEY_RATING, rating);

        return db.update(DATABASE_TABLE, values, KEY_URL + " = " + "'" + url +"'", null) > 0;
    }

    public Cursor getAllWishes(SQLiteDatabase db){
        return db.query(DATABASE_TABLE, new String[] {KEY_ID, KEY_NAME, KEY_PRICE, KEY_URL, KEY_RATING}, null, null, null, null, null);
    }
}
