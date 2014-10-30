package com.techpark.lastfmclient.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by andrew on 30.10.14.
 */
public class DBOpenHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "lastfm.db";
    private static final int DB_VER = 1;
    public static final String AUTHORITY = "com.techpark.lastfmclient";
    public DBOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
