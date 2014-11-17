package com.techpark.lastfmclient.db;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by max on 15/11/14.
 */
public class RecommendedArtistsTable implements BaseColumns {

    private RecommendedArtistsTable() {}

    public static final String TABLE_NAME = "recommended";
    public static final Uri CONTENT_URI = Uri.parse("content://" + DBLastfmHelper.AUTHORITY + "/recommended");
    public static final Uri CONTENT_URI_ID_RECOMMENDED = Uri.parse("content://" + DBLastfmHelper.AUTHORITY + "/recommended/");

    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_SIMILAR_FIRST = "similar_first";
    public static final String COLUMN_SIMILAR_SECOND = "similar_second";

    public static final String SQL_CREATE_RECOMMENDED_TABLE = "CREATE TABLE "
            + TABLE_NAME + " (" + _ID + " INTEGER PRIMARY KEY, "
            + COLUMN_NAME + " TEXT UNIQUE, "
            + COLUMN_SIMILAR_FIRST + " TEXT,"
            + COLUMN_SIMILAR_SECOND + " TEXT "
            + ");";
}
