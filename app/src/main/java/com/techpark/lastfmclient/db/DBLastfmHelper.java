package com.techpark.lastfmclient.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by andrew on 30.10.14.
 */
public class DBLastfmHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "lastfm.db";
    private static final int DB_VER = 2;
    public static final String AUTHORITY = "com.techpark.lastfmclient";
    public DBLastfmHelper(Context context) {
        super(context, DB_NAME, null, DB_VER);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.e("DB info", "DOWNGRADE DB");
        db.execSQL("DROP TABLE IF EXISTS " + UsersTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ArtistsTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + RecommendedArtistsTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + RecentTracksTable.TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("DB info", "CREATING TABLES....");
        db.execSQL(UsersTable.SQL_CREATE_USER_TABLE);
        db.execSQL(ArtistsTable.SQL_CREATE_ARTIST_TABLE);
        db.execSQL(RecommendedArtistsTable.SQL_CREATE_RECOMMENDED_TABLE);
        db.execSQL(RecentTracksTable.SQL_CREATE_RECENT_TRACK_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.e("DB info", "UPDATING DB");
        db.execSQL("DROP TABLE IF EXISTS " + UsersTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ArtistsTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + RecommendedArtistsTable.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + RecentTracksTable.TABLE_NAME);
        onCreate(db);
    }
}
