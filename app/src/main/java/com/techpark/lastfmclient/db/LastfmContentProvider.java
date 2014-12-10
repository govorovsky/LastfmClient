package com.techpark.lastfmclient.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

/**
 * Created by Andrew Gov on 03.11.14.
 */
public class LastfmContentProvider extends ContentProvider {

    private DBLastfmHelper dbLastfmHelper;
    private UriMatcher uriMatcher;

    private SQLiteDatabase readDb;
    private SQLiteDatabase writeDb;

    // all entities here
    private class DBEntity {
        final static int USER = 1;
        final static int USER_INFO = 2;
        final static int ARTIST = 4;
        final static int RECOMMENDED = 5;
        final static int RECOMMENDED_INFO = 6;
        final static int TRACK_RECENT = 7;
        final static int LIBRARY = 73;
    }

    public LastfmContentProvider() {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        /* for example "/user/name" will return full user info for name */
        uriMatcher.addURI(DBLastfmHelper.AUTHORITY, UsersTable.TABLE_NAME + "/", DBEntity.USER);
        uriMatcher.addURI(DBLastfmHelper.AUTHORITY, UsersTable.TABLE_NAME + "/*", DBEntity.USER_INFO);

        uriMatcher.addURI(DBLastfmHelper.AUTHORITY, RecentTracksTable.TABLE_NAME + "/", DBEntity.TRACK_RECENT);

        uriMatcher.addURI(DBLastfmHelper.AUTHORITY, LibraryTable.TABLE_NAME + "/", DBEntity.LIBRARY);

        uriMatcher.addURI(DBLastfmHelper.AUTHORITY, ArtistsTable.TABLE_NAME + "/", DBEntity.ARTIST);
        uriMatcher.addURI(DBLastfmHelper.AUTHORITY, RecommendedArtistsTable.TABLE_NAME + "/", DBEntity.RECOMMENDED);
        uriMatcher.addURI(DBLastfmHelper.AUTHORITY, RecommendedArtistsTable.TABLE_NAME + "/*", DBEntity.RECOMMENDED_INFO);

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
            case DBEntity.TRACK_RECENT:
                return queryRecentTracks(uri, projection, selection, selArgs);
            case DBEntity.LIBRARY:
                return queryLibrary(uri, projection, selection, selArgs);
            case DBEntity.ARTIST:
                return queryArtist(uri, projection, selection, selArgs);
            case DBEntity.RECOMMENDED: //TODO: limit as param?
                return queryRecommended(uri, projection, selection, selArgs);
        }
        return null;
    }

    private Cursor queryLibrary(Uri uri, String[] projection, String selection, String[] selArgs) {
        Cursor c = readDb.query(LibraryTable.TABLE_NAME, projection, null, null, null, null, null);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    private Cursor queryUser(Uri uri, String[] projection, String selection, String[] selArgs) {
        Cursor c = readDb.query(UsersTable.TABLE_NAME, projection, UsersTable.COLUMN_NAME + "=?", new String[]{uri.getLastPathSegment()}, null, null, null);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    private Cursor queryRecentTracks(Uri uri, String[] projection, String selection, String[] selArgs) {
        Cursor c = readDb.query(RecentTracksTable.TABLE_NAME, projection, null, null, null, null, null);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }


    private Cursor queryArtist(Uri uri, String[] projection, String selection, String[] selArgs) {
        Cursor c = readDb.query(ArtistsTable.TABLE_NAME, projection, ArtistsTable.COLUMN_NAME + "=?",
                new String[]{uri.getLastPathSegment()}, null, null, null);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    //TODO
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
                return updateOrInsertUser(contentValues);
            case DBEntity.ARTIST:
                return updateOrInsertArtist(contentValues, false);
            case DBEntity.RECOMMENDED:
                return updateOrInsertRecommended(contentValues, false);
            default:
                return null;
        }
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        switch (uriMatcher.match(uri)) {
            case DBEntity.RECOMMENDED:
                return writeDb.delete(uri.getLastPathSegment(), s, strings);
            case DBEntity.TRACK_RECENT:
                return writeDb.delete(uri.getLastPathSegment(), s, strings);
            case DBEntity.LIBRARY:
                return writeDb.delete(uri.getLastPathSegment(), s, strings);
        }
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }

    private Uri updateOrInsertUser(ContentValues contentValues) {
        String username = contentValues.getAsString(UsersTable.COLUMN_NAME);

        long rowId = writeDb.update(UsersTable.TABLE_NAME, contentValues, UsersTable.COLUMN_NAME + "=?", new String[]{username});

        if (rowId == 0) {
            rowId = writeDb.insert(UsersTable.TABLE_NAME, null, contentValues);
        }

        if (rowId > 0) {
            Uri newUri = Uri.withAppendedPath(UsersTable.CONTENT_URI_ID_USER, username);
            getContext().getContentResolver().notifyChange(newUri, null);
            return newUri;
        }
        return null;

    }

    private Uri updateOrInsertArtist(ContentValues contentValues, boolean isBatch) {
        String artistName = contentValues.getAsString(ArtistsTable.COLUMN_NAME);

        long rowId = writeDb.update(ArtistsTable.TABLE_NAME, contentValues, ArtistsTable.COLUMN_NAME + "=?", new String[]{artistName});

        if (rowId == 0) {
            rowId = writeDb.insert(ArtistsTable.TABLE_NAME, null, contentValues);
        }

        if (rowId > 0) {
            Uri newUri = Uri.withAppendedPath(ArtistsTable.CONTENT_URI_ID_ARTIST, artistName);
            if (!isBatch) {
                getContext().getContentResolver().notifyChange(newUri, null);
            }
            return newUri;
        }
        Log.e("INSERT ERROR", "id=" + rowId + "name=" + artistName);
        return null;
    }

    private Uri updateOrInsertRecommended(ContentValues contentValues, boolean isBatch) {
        String artistName = contentValues.getAsString(RecommendedArtistsTable.COLUMN_NAME);

        long rowId = writeDb.update(
                RecommendedArtistsTable.TABLE_NAME,
                contentValues,
                RecommendedArtistsTable.COLUMN_NAME + "=?",
                new String[]{artistName}
        );

        if (rowId == 0) {
            rowId = writeDb.insert(RecommendedArtistsTable.TABLE_NAME, null, contentValues);
        }


        if (rowId > 0) {
            Uri newUri = Uri.withAppendedPath(RecommendedArtistsTable.CONTENT_URI_ID_RECOMMENDED, artistName);
            if (!isBatch) {
                getContext().getContentResolver().notifyChange(newUri, null);
            }
            return newUri;
        }

        Log.e("INSERT ERROR", "id=" + rowId + "name=" + artistName);

        return null;
    }

    private Uri insertRecentTrack(ContentValues contentValues, boolean isBatch) {
        String trackName = contentValues.getAsString(RecentTracksTable.COLUMN_NAME);

        long rowId = writeDb.insert(RecentTracksTable.TABLE_NAME, null, contentValues);

        if (rowId > 0) {
            Uri newUri = Uri.withAppendedPath(RecentTracksTable.CONTENT_URI, trackName);
            if (!isBatch) {
                getContext().getContentResolver().notifyChange(newUri, null);
            }
            return newUri;
        }
        return null;
    }

    private Uri insertLibrary(ContentValues contentValues, boolean isBatch) {
        String artistName = contentValues.getAsString(RecentTracksTable.COLUMN_NAME);

        long rowId = writeDb.insert(LibraryTable.TABLE_NAME, null, contentValues);

        if (rowId > 0) {
            Uri newUri = Uri.withAppendedPath(LibraryTable.CONTENT_URI, artistName);
            if (!isBatch) {
                getContext().getContentResolver().notifyChange(newUri, null);
            }
            return newUri;
        }
        return null;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        switch (uriMatcher.match(uri)) {
            case DBEntity.RECOMMENDED:
                Log.v("Bulk insert", "recommended");
                return bulkInsertEntity(uri, values, new BulkEntity() {
                    @Override
                    public void insert(ContentValues cv) {
                        updateOrInsertRecommended(cv, true);
                    }
                });
            case DBEntity.ARTIST:
                Log.v("Bulk insert", "artists");
                return bulkInsertEntity(uri, values, new BulkEntity() {
                    @Override
                    public void insert(ContentValues cv) {
                        updateOrInsertArtist(cv, true);
                    }
                });

            case DBEntity.TRACK_RECENT:
                Log.v("Bulk insert", "tracks");
                return bulkInsertEntity(uri, values, new BulkEntity() {
                    @Override
                    public void insert(ContentValues cv) {
                        insertRecentTrack(cv, true);
                    }
                });

            case DBEntity.LIBRARY:
                Log.v("Bulk insert", "library");
                return bulkInsertEntity(uri, values, new BulkEntity() {
                    @Override
                    public void insert(ContentValues cv) {
                        insertLibrary(cv, true);
                    }
                });

        }
        return 0;
    }


    private interface BulkEntity {
        void insert(ContentValues cv);
    }

    private int bulkInsertEntity(Uri uri, ContentValues[] values, BulkEntity entity) {
        writeDb.beginTransaction();
        try {
            for (ContentValues cv : values)
                entity.insert(cv);
            writeDb.setTransactionSuccessful();
        } finally {
            writeDb.endTransaction();
        }
        getContext().getContentResolver().notifyChange(uri, null);
        Log.d("ContentProvider", values.length + " ");
        return values.length;
    }
}
