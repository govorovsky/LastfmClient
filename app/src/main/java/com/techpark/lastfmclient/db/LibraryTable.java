package com.techpark.lastfmclient.db;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Andrew Govorovsky on 10.12.14.
 */
public class LibraryTable implements BaseColumns {

    private LibraryTable() {
    }


    public static final String TABLE_NAME = "library";

    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PLAYCNT = "playc";
    public static final String COLUMN_COVER = "cover";
    public static final String COLUMN_URL = "url";

    public static final Uri CONTENT_URI = Uri.parse("content://" + DBLastfmHelper.AUTHORITY + "/library");

    public static final String SQL_CREATE_LIBRARY_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
            + _ID + " INTEGER PRIMARY KEY, "
            + COLUMN_NAME + " TEXT, "
            + COLUMN_PLAYCNT + " INTEGER, "
            + COLUMN_COVER + " TEXT, "
            + COLUMN_URL + " TEXT"
            + " ); ";
}
