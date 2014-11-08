package com.techpark.lastfmclient.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.techpark.lastfmclient.api.user.User;
import com.techpark.lastfmclient.api.user.UserHelpers;

/**
 * Created by Andrew Gov on 03.11.14.
 */
public class LastfmContentProvider extends ContentProvider {

    private DBLastfmHelper dbLastfmHelper;
    private UriMatcher uriMatcher;

    private SQLiteDatabase readDb;
    private SQLiteDatabase writeDb;

    // all entities here
    private static final int USER = 1;
    private static final int USER_INFO = 2;
    private static final int TRACK_INFO = 3;


    public LastfmContentProvider() {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        /* for example "/user/name" will return full user info for name */
        uriMatcher.addURI(DBLastfmHelper.AUTHORITY, UsersTable.TABLE_NAME + "/", USER);
        uriMatcher.addURI(DBLastfmHelper.AUTHORITY, UsersTable.TABLE_NAME + "/*", USER_INFO);

    }

    @Override
    public boolean onCreate() {
        dbLastfmHelper = new DBLastfmHelper(getContext());
        writeDb = dbLastfmHelper.getWritableDatabase();
        readDb = dbLastfmHelper.getReadableDatabase();

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
            case USER:
                return insertUser(contentValues);

            default:
                return null;
        }
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }


    private Uri insertUser(ContentValues contentValues) {
        /* update old user info if exists */
        String username = contentValues.getAsString(UsersTable.COLUMN_NAME);
        long rowId;
        if (userExists(username)) {
            rowId = writeDb.update(UsersTable.TABLE_NAME, contentValues, UsersTable.COLUMN_NAME + "=?", new String[]{username});
        } else {
            rowId = writeDb.insert(UsersTable.TABLE_NAME, null, contentValues);
        }
        if (rowId > 0) {
            Uri newUri = Uri.withAppendedPath(UsersTable.CONTENT_URI_ID_USER, username);
            getContext().getContentResolver().notifyChange(newUri, null);
            return newUri;
        }
        return null;
    }

    private boolean userExists(String username) {
        Cursor c = readDb.query(UsersTable.TABLE_NAME, new String[]{UsersTable.COLUMN_NAME}, "name = ?", new String[]{username}, null, null, null);
        return c.getCount() != 0;
    }
}
