package com.techpark.lastfmclient.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.CursorJoiner;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.techpark.lastfmclient.api.artist.Artist;


/**
 * Created by Andrew Gov on 03.11.14.
 */
public class LastfmContentProvider extends ContentProvider {

    private static final String TAG = LastfmContentProvider.class.getSimpleName();

    private DBLastfmHelper dbLastfmHelper;
    private UriMatcher uriMatcher;

    private SQLiteDatabase readDb;
    private SQLiteDatabase writeDb;

    // all entities here
    private class DBEntity {
        final static int USER = 1;
        final static int USER_INFO = 2;
        final static int TRACK_INFO = 3;
        final static int ARTIST = 4;
        final static int RECOMMENDED = 5;
    }

    public LastfmContentProvider() {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        /* for example "/user/name" will return full user info for name */
        uriMatcher.addURI(DBLastfmHelper.AUTHORITY, UsersTable.TABLE_NAME + "/", DBEntity.USER);
        uriMatcher.addURI(DBLastfmHelper.AUTHORITY, UsersTable.TABLE_NAME + "/*", DBEntity.USER_INFO);

        uriMatcher.addURI(DBLastfmHelper.AUTHORITY, ArtistsTable.TABLE_NAME + "/", DBEntity.ARTIST);
        uriMatcher.addURI(DBLastfmHelper.AUTHORITY, RecommendedArtistsTable.TABLE_NAME + "/*", DBEntity.RECOMMENDED);

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
        switch (uriMatcher.match(uri)) {
            case DBEntity.USER_INFO:
                return queryUser(uri, projection, selection, selArgs);
            case DBEntity.ARTIST:
                return queryArtist(uri, projection, selection, selArgs);
            case DBEntity.RECOMMENDED:
                return queryRecommended(uri, projection, selection, selArgs);
        }
        return null;
    }

    private Cursor queryUser(Uri uri, String[] projection, String selection, String[] selArgs) {
        Cursor c = readDb.query(UsersTable.TABLE_NAME, projection, UsersTable.COLUMN_NAME + "=?", new String[]{uri.getLastPathSegment()}, null, null, null);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    private Cursor queryArtist(Uri uri, String[] projection, String selection, String[] selArgs) {
        Cursor c = readDb.query(ArtistsTable.TABLE_NAME, projection, ArtistsTable.COLUMN_NAME + "=?",
                new String[]{uri.getLastPathSegment()}, null, null, null);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    private Cursor queryRecommended(Uri uri, String[] projection, String selection, String[] selArgs) {
        //Cursor rec = readDb.query(RecommendedArtistsTable.TABLE_NAME, projection, RecommendedArtistsTable.COLUMN_NAME + "=?",
        //        new String[]{uri.getLastPathSegment()}, null, null, null);
        //Cursor art = readDb.query(ArtistsTable.TABLE_NAME, null, null, null, null, null, null);

        String query = "select " +
                "r.name, r.similar_first, r.similar_second, " +
                "a.image_small, a.image_medium, a.image_large, a.image_extralarge, a.image_mega, " +
                "s1.image_small, s1.image_medium, s1.image_large, s1.image_extralarge, s1.image_mega, " +
                "s2.image_small, s2.image_medium, s2.image_large, s2.image_extralarge, s2.image_mega " +
                "from recommended r join artist a on r.name = a.name " +
                "join artist s1 on r.similar_first = s1.name " +
                "join artist s2 on r.similar_second = s2.name";

        Cursor c = readDb.rawQuery(query, null); //TODO: selection

        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        switch (uriMatcher.match(uri)) {
            case DBEntity.USER:
                return insertUser(contentValues);
            case DBEntity.ARTIST:
                return insertArtist(contentValues);
            case DBEntity.RECOMMENDED:
                return insertRecommended(contentValues);

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
            Log.d(TAG, "ADDED. NOTIFY URI: " + newUri.toString());
            getContext().getContentResolver().notifyChange(newUri, null);
            return newUri;
        }
        return null;
    }

    private Uri insertArtist(ContentValues contentValues) {
        String artistName = contentValues.getAsString(ArtistsTable.COLUMN_NAME);
        long rowId;
        if (artistExist(artistName)) {
            rowId = writeDb.update(ArtistsTable.TABLE_NAME, contentValues, ArtistsTable.COLUMN_NAME + "=?", new String[]{artistName});
        } else {
            rowId = writeDb.insert(ArtistsTable.TABLE_NAME, null, contentValues);
        }

        if (rowId > 0) {
            Uri newUri = Uri.withAppendedPath(ArtistsTable.CONTENT_URI_ID_ARTIST, artistName);
            getContext().getContentResolver().notifyChange(newUri, null);
            return newUri;
        }
        return null;
    }

    private Uri insertRecommended(ContentValues contentValues) {
        String artistName = contentValues.getAsString(RecommendedArtistsTable.COLUMN_NAME);
        long rowId;
        if (recommendedExist(artistName)) {
            rowId = writeDb.update(
                    RecommendedArtistsTable.TABLE_NAME,
                    contentValues,
                    RecommendedArtistsTable.COLUMN_NAME + "=?",
                    new String[]{artistName}
            );
        } else {
            rowId = writeDb.insert(RecommendedArtistsTable.TABLE_NAME, null, contentValues);
        }

        if (rowId > 0) {
            Uri newUri = Uri.withAppendedPath(RecommendedArtistsTable.CONTENT_URI_ID_RECOMMENDED, artistName);
            getContext().getContentResolver().notifyChange(newUri, null);
            return newUri;
        }
        return null;
    }

    public Cursor getArtistTable() {
        return readDb.query(
                ArtistsTable.TABLE_NAME,
                null,
                null,
                null,
                null, null, null
        );
    }


    private boolean userExists(String username) {
        Cursor c = readDb.query(
                UsersTable.TABLE_NAME,
                new String[]{UsersTable.COLUMN_NAME},
                UsersTable.COLUMN_NAME + " =?",
                new String[]{username},
                null, null, null
        );
        return c.getCount() != 0;
    }

    private boolean artistExist(String artistName) {
        Cursor c = readDb.query(ArtistsTable.TABLE_NAME, new String[]{ArtistsTable.COLUMN_NAME}, ArtistsTable.COLUMN_NAME + " =?",
                new String[]{artistName}, null, null, null);
        return c.getCount() > 0;
    }

    private boolean recommendedExist(String artistName) {
        Cursor c = readDb.query(
                RecommendedArtistsTable.TABLE_NAME,
                new String[]{RecommendedArtistsTable.COLUMN_NAME},
                RecommendedArtistsTable.COLUMN_NAME + " =?",
                new String[]{artistName}, null, null, null);
        return c.getCount() > 0;
    }
}
