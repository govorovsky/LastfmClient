package com.techpark.lastfmclient.services;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by andrew on 30.10.14.
 */
public class UserInfoService extends IntentService {
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

    }
}