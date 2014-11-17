package com.techpark.lastfmclient.db;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by andrew on 30.10.14.
 */
public class UsersTable implements BaseColumns {

    public static final int USER_SIZE = 10; // num of fields

    private UsersTable() {
    }

    public static final String TABLE_NAME = "user";
    public static final Uri CONTENT_URI = Uri.parse("content://" + DBLastfmHelper.AUTHORITY + "/user");
    public static final Uri CONTENT_URI_ID_USER = Uri.parse("content://" + DBLastfmHelper.AUTHORITY + "/user/");

    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_REALNAME = "realname";
    public static final String COLUMN_AVATAR = "avatar";
    public static final String COLUMN_COUNTRY = "country";
    public static final String COLUMN_AGE = "age";
    public static final String COLUMN_GENDER = "sex";
    public static final String COLUMN_PLAYCOUNT = "playcount";
    public static final String COLUMN_REGISTERED = "registered";
    public static final String COLUMN_COVER = "cover";
    public static final String COLUMN_TIMESTAMP = "timestamp";


    public static final String SQL_CREATE_USER_TABLE = "CREATE TABLE " + TABLE_NAME + " ("
            + _ID + " INTEGER PRIMARY KEY, "
            + COLUMN_NAME + " TEXT UNIQUE, "
            + COLUMN_REALNAME + " TEXT, "
            + COLUMN_AVATAR + " TEXT, "
            + COLUMN_COUNTRY + " TEXT, "
            + COLUMN_AGE + " INTEGER, "
            + COLUMN_GENDER + " TEXT, "
            + COLUMN_PLAYCOUNT + " INTEGER, "
            + COLUMN_REGISTERED + " TEXT, "
            + COLUMN_COVER + " TEXT, "
            + COLUMN_TIMESTAMP + " INTEGER "
            + " ); ";

}
