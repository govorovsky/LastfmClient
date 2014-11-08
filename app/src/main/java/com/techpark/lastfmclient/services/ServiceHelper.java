package com.techpark.lastfmclient.services;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.techpark.lastfmclient.providers.UsersProvider;

/**
 * Created by Andrew Gov on 08.11.14.
 */
public class ServiceHelper {
    private Context mContext;
    private static final String TAG = ServiceHelper.class.getSimpleName();

    public ServiceHelper(Context context) {
        this.mContext = context;
    }

    public void getUser(String username) {
        Intent intent = new Intent(mContext, ServiceProcessor.class);
        intent.putExtra(ServiceProcessor.PROVIDER, ServiceProcessor.Providers.USERS_PROVIDER);
        intent.putExtra(ServiceProcessor.METHOD, UsersProvider.Actions.GET);

        Bundle extras = new Bundle();
        extras.putString(UsersProvider.BUNDLE_USERNAME, username);
        intent.putExtras(extras);
        Log.d(TAG + " Try to invoke getUser with : ", username);
        mContext.startService(intent);
    }
}
