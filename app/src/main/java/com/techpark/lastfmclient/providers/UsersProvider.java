package com.techpark.lastfmclient.providers;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.techpark.lastfmclient.api.ApiQuery;
import com.techpark.lastfmclient.api.library.LibraryGetArtists;
import com.techpark.lastfmclient.api.library.LibraryHelpers;
import com.techpark.lastfmclient.api.user.User;
import com.techpark.lastfmclient.api.user.UserGetInfo;
import com.techpark.lastfmclient.api.user.UserHelpers;
import com.techpark.lastfmclient.db.UsersTable;
import com.techpark.lastfmclient.network.NetworkUtils;

import java.io.IOException;

/**
 * Created by Andrew Gov on 08.11.14.
 */
public class UsersProvider implements IProvider {

    public static final String BUNDLE_USERNAME = "username";

    private static final int CACHE_EXPIRATION = 60 * 1000; // 1 minute

    private final Context mContext;
    private static final String TAG = UsersProvider.class.getSimpleName();


    public UsersProvider(Context mContext) {
        this.mContext = mContext;
    }

    public static class Actions {
        public static final int GET = 1;
    }

    public void execMethod(int methodId, Bundle extra) {

        String username = extra.getString(BUNDLE_USERNAME);

        switch (methodId) {
            case Actions.GET:
                getUser(username);
                break;
        }
    }

    /* call to REST and update SQL if necessary */
    private void getUser(String username) {

        ContentResolver resolver = mContext.getContentResolver();

        // first, check if we have actual record in db
        Cursor c = null;
        try {
            c = resolver.query(Uri.withAppendedPath(UsersTable.CONTENT_URI_ID_USER, username), null, null, null, null);
            if (c.moveToFirst()) {
                // we have such user, check cache time
                long timestamp = c.getLong(10);
                if (System.currentTimeMillis() - timestamp < CACHE_EXPIRATION) {
                    // user is up to date, nothing to do
                    Log.d(TAG, "getUser DB Request");
                    return;
                }
            }
        } finally {
            if (c != null && !c.isClosed()) {
                c.close();
            }
        }

        ApiQuery queryUser = new UserGetInfo(username);
        queryUser.prepare();

        ApiQuery queryMostPlayed = new LibraryGetArtists(username, 1);
        queryMostPlayed.prepare();

        Log.d(TAG, "getUser API Request");

        try {
            String response = NetworkUtils.httpRequest(queryUser);
            Log.d(TAG, response);
            User user = UserHelpers.getUserFromJson(response);

            response = NetworkUtils.httpRequest(queryMostPlayed);
            user.setMostPlayedArtist(LibraryHelpers.parseMostPlayedFromJson(response));

            resolver.insert(UsersTable.CONTENT_URI, UserHelpers.getUserContentValues(user));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
