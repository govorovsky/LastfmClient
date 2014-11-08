package com.techpark.lastfmclient.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Created by Andrew Gov on 03.11.14.
 */
public class LastfmContentProvider extends ContentProvider {

    private DBLastfmHelper dbLastfmHelper;
    private UriMatcher uriMatcher;

    private SQLiteDatabase readDb;
    private SQLiteDatabase writeDb;

    // all entities here
    private static final int USER_INFO = 1;
    private static final int TRACK_INFO = 2;


    public LastfmContentProvider() {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        /* for example "/user/name" will return full user info for name */
        uriMatcher.addURI(DBLastfmHelper.AUTHORITY, UsersTable.TABLE_NAME + "/*", USER_INFO);

    }

    @Override
    public boolean onCreate() {
        dbLastfmHelper = new DBLastfmHelper(getContext());

        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selArgs, String sortOrder) {
        return null;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        switch (uriMatcher.match(uri)) {
            case USER_INFO:
                /* replace old user info if exists */
                writeDb.replace(UsersTable.TABLE_NAME, null, contentValues);
                break;
        }
        return null;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}
