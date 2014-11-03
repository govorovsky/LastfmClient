package com.techpark.lastfmclient.services;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Intent;
import android.util.Log;

import com.techpark.lastfmclient.api.ApiQuery;
import com.techpark.lastfmclient.api.user.User;
import com.techpark.lastfmclient.api.user.UserGetInfo;
import com.techpark.lastfmclient.api.user.UserHelpers;
import com.techpark.lastfmclient.db.UsersTable;
import com.techpark.lastfmclient.network.NetworkUtils;

import java.io.IOException;

/**
 * Created by andrew on 30.10.14.
 */
public class UserInfoService extends IntentService {

    public static final String TAG = UserInfoService.class.getName();

    public static final String BUNDLE_USERNAME = "username";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public UserInfoService(String name) {
        super(name);
    }

    public UserInfoService() {
        super(UserInfoService.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String username = intent.getStringExtra(BUNDLE_USERNAME);
        ApiQuery query = new UserGetInfo(username);
        query.prepare();

        Log.e(TAG, "UserInfoService started");

        String response = null;
        try {
            response = NetworkUtils.httpRequest(query);
            Log.d(TAG, response);
            User user = UserHelpers.getUserFromJson(response);
            ContentResolver resolver = getContentResolver();
            /* TODO */
//            resolver.insert(UsersTable.CONTENT_URI, UserHelpers.getContentValues(user));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
