package com.techpark.lastfmclient.db;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Andrew Govorovsky on 03.12.14.
 */
public class RecentTracksTable implements BaseColumns {

    private RecentTracksTable() {

    }


    public static final String TABLE_NAME = "recent_track";
    public static final Uri CONTENT_URI = Uri.parse("content://" + DBLastfmHelper.AUTHORITY + "/recent_track");

    public static final String COLUMN_ARTIST = "artist";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_ALBUM = "album";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_IMAGE = "img";
    public static final String COLUMN_TIMESTAMP = "timestamp";


    public static final String SQL_CREATE_RECENT_TRACK_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
            + _ID + " INTEGER PRIMARY KEY, "
            + COLUMN_NAME + " TEXT, "
            + COLUMN_ARTIST + " TEXT, "
            + COLUMN_ALBUM + " TEXT, "
            + COLUMN_IMAGE + " TEXT, "
            + COLUMN_DATE + " TEXT, "
            + COLUMN_TIMESTAMP + " INTEGER "
            + " ); ";

}
