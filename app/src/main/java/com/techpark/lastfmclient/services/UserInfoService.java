package com.techpark.lastfmclient.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.techpark.lastfmclient.api.ApiQuery;
import com.techpark.lastfmclient.api.user.UserGetInfo;
import com.techpark.lastfmclient.network.NetworkUtils;

import java.io.IOException;

/**
 * Created by andrew on 30.10.14.
 */
public class UserInfoService extends IntentService {

    public static final String TAG = UserInfoService.class.getName();

    public static final String USERNAME = "username";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public UserInfoService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String username = intent.getStringExtra(USERNAME);
        ApiQuery query = new UserGetInfo(username);
        String response = null;
        try {
            response = NetworkUtils.httpRequest(query);
            Log.d(TAG, response);
            /* TODO */
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
