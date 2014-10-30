package com.techpark.lastfmclient.db;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by andrew on 30.10.14.
 */
public class UsersTable implements BaseColumns {


    private UsersTable() {
    }

    public static final String TABLE_NAME = "users";
    public static final Uri CONTENT_URI = Uri.parse("content://" + DBLastfmHelper.AUTHORITY + "/user");

    public static final String COLUMN_NAME_NAME = "name";
    public static final String COLUMN_NAME_REALNAME = "realname";
    public static final String COLUMN_NAME_AVATAR = "avatar";
    public static final String COLUMN_NAME_COUNTRY = "country";
    public static final String COLUMN_NAME_AGE = "age";
    public static final String COLUMN_NAME_GENDER = "sex";
    public static final String COLUMN_NAME_PLAYCOUNT = "playcount";
    public static final String COLUMN_NAME_REGISTERED = "registered";


    public static final String RAW_SQL = "CREATE TABLE " + TABLE_NAME + " ("
            + _ID + " INTEGER PRIMARY KEY, "
            + COLUMN_NAME_NAME + " TEXT, "
            + COLUMN_NAME_REALNAME + " TEXT, "
            + COLUMN_NAME_AVATAR + " TEXT, "
            + COLUMN_NAME_COUNTRY + " TEXT, "
            + COLUMN_NAME_AGE + " INTEGER, "
            + COLUMN_NAME_GENDER + " TEXT, "
            + COLUMN_NAME_PLAYCOUNT + " INTEGER, "
            + COLUMN_NAME_REGISTERED + " TEXT "
            + " ); ";


}
