package com.techpark.lastfmclient.services;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.techpark.lastfmclient.db.RecommendedArtistsTable;
import com.techpark.lastfmclient.providers.RecommendedProvider;
import com.techpark.lastfmclient.providers.UsersProvider;

/**
 * Created by Andrew Gov on 08.11.14.
 */
public class ServiceHelper {
    private Context mContext;

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
        mContext.startService(intent);
    }

    public void getRecommendedArtists() {
        Intent intent = new Intent(mContext, ServiceProcessor.class);
        intent.putExtra(ServiceProcessor.PROVIDER, ServiceProcessor.Providers.RECOMMENDED_PROVIDER);
        intent.putExtra(ServiceProcessor.METHOD, RecommendedProvider.Actions.GET);

        mContext.startService(intent);
    }

    public void freeDataBase() {
        ContentResolver resolver = mContext.getContentResolver();
//        resolver.delete(UsersTable.CONTENT_URI, null, null);
//        resolver.delete(ArtistsTable.CONTENT_URI, null, null);
        resolver.delete(RecommendedArtistsTable.CONTENT_URI, null, null);
    }
}
