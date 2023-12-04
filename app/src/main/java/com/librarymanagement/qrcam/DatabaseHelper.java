package com.librarymanagement.qrcam;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String COL_1 = "IP";
    public static final String DATABASE_NAME = "IPcon.db";
    public static final String TABLE_NAME = "Internet_P";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Internet_P(IP TEXT)");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Internet_P");
    }

    public boolean updateData(String ip) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, ip);
        if (db.update(TABLE_NAME, contentValues, null, null) > 0) {
            return true;
        }
        return false;
    }

    public boolean ineertData(String ip) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, ip);
        long result = db.insert(TABLE_NAME, null, contentValues);
        db.close();
        if (result == -1) {
            return false;
        }
        return true;
    }

    public Cursor checkifdata() {
        return getWritableDatabase().rawQuery("select * from Internet_P", null);
    }

    public Cursor readIP() {
        return getWritableDatabase().rawQuery("select * from Internet_P", null);
    }
}
