package com.techpark.lastfmclient.db;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Andrew Govorovsky on 14.12.14.
 */
public class TrackTable implements BaseColumns {

    private TrackTable() {

    }


    public static final String TABLE_NAME = "track";
    public static final Uri CONTENT_URI = Uri.parse("content://" + DBLastfmHelper.AUTHORITY + "/track");
    public static final Uri CONTENT_URI_ID_TRACK = Uri.parse("content://" + DBLastfmHelper.AUTHORITY + "/track/");

    public static final String COLUMN_ARTIST = "artist";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_ALBUM = "album";
    public static final String COLUMN_ARTIST_IMG = "img_artist";
    public static final String COLUMN_ALBUM_IMG = "img_album";
    public static final String COLUMN_TAGS = "tags";
    public static final String COLUMN_USERLOVED = "loved";
    public static final String COLUMN_SUMMARY = "summary";
    public static final String COLUMN_CONTENT = "content";
    public static final String COLUMN_TIMESTAMP = "timestamp";
    public static final String COLUMN_PLAYCNT = "playcnt";
    public static final String COLUMN_DURATION = "duration";


    public static final String SQL_CREATE_RECENT_TRACK_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
            + _ID + " INTEGER PRIMARY KEY, "
            + COLUMN_NAME + " TEXT, "
            + COLUMN_ARTIST + " TEXT, "
            + COLUMN_ALBUM + " TEXT, "
            + COLUMN_ARTIST_IMG + " TEXT, "
            + COLUMN_ALBUM_IMG + " TEXT, "
            + COLUMN_TAGS + " TEXT, "
            + COLUMN_USERLOVED + " BOOLEAN, "
            + COLUMN_SUMMARY + " TEXT, "
            + COLUMN_CONTENT + " TEXT, "
            + COLUMN_PLAYCNT + " INTEGER, "
            + COLUMN_DURATION + " INTEGER, "
            + COLUMN_TIMESTAMP + " INTEGER "
            + " ); ";
}
