package com.techpark.lastfmclient.db;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by andrew on 30.10.14.
 */
public class UsersTable implements BaseColumns {
    private UsersTable() {}

    public static final String TABLE_NAME = "main";
    public static final Uri CONTENT_URI =  Uri.parse("content://" + DBOpenHelper.AUTHORITY + "/user");


}
