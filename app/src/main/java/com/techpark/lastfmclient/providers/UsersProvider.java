package com.techpark.lastfmclient.providers;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.techpark.lastfmclient.api.ApiQuery;
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

        ApiQuery query = new UserGetInfo(username);
        query.prepare();

        Log.d(TAG, "getUser");

        String response = null;
        try {
            response = NetworkUtils.httpRequest(query);
            Log.d(TAG, response);
            User user = UserHelpers.getUserFromJson(response);
            ContentResolver resolver = mContext.getContentResolver();
            /* TODO  */
            resolver.insert(UsersTable.CONTENT_URI, UserHelpers.getContentValues(user));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
