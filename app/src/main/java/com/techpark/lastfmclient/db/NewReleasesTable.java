package com.techpark.lastfmclient.db;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by max on 24/11/14.
 */
public class NewReleasesTable implements BaseColumns {

    private NewReleasesTable() {}

    public static final String TABLE_NAME = "new_releases";
    public static final Uri CONTENT_URI = Uri.parse("content://" + DBLastfmHelper.AUTHORITY + "/new_releases");
    public static final Uri CONTENT_URI_ID_RELEASE = Uri.parse("content://" + DBLastfmHelper.AUTHORITY + "/new_releases/");

    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_URL = "url";
    public static final String COLUMN_ARTIST = "artist";
    public static final String COLUMN_RELEASE_DATE = "date";
    //TODO: with ;
    public static final String COLUMN_IMAGE_SMALL = "image_small";
    public static final String COLUMN_IMAGE_MEDIUM = "image_medium";
    public static final String COLUMN_IMAGE_LARGE = "image_large";
    public static final String COLUMN_IMAGE_EXTRALARGE = "image_extralarge";

    public static final String SQL_CREATE_NEW_RELEASES_TABLE = "CREATE TABLE " + TABLE_NAME +  " ("
            + _ID + " INTEGER PRIMARY KEY, "
            + COLUMN_NAME + " TEXT UNIQUE, "
            + COLUMN_URL + " TEXT, "
            + COLUMN_ARTIST + " TEXT, "
            + COLUMN_RELEASE_DATE + " TEXT, " //date
            + COLUMN_IMAGE_SMALL + " TEXT, "
            + COLUMN_IMAGE_MEDIUM + " TEXT, "
            + COLUMN_IMAGE_LARGE + " TEXT, "
            + COLUMN_IMAGE_EXTRALARGE + " TEXT "
            + " );";
}
