package com.techpark.lastfmclient.providers;

import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;

import com.techpark.lastfmclient.adapters.LibraryArtistsList;
import com.techpark.lastfmclient.api.ApiQuery;
import com.techpark.lastfmclient.api.ApiResponse;
import com.techpark.lastfmclient.api.library.LibArtist;
import com.techpark.lastfmclient.api.library.LibraryGetArtists;
import com.techpark.lastfmclient.api.library.LibraryHelpers;
import com.techpark.lastfmclient.db.LibraryTable;
import com.techpark.lastfmclient.network.NetworkUtils;

import java.io.IOException;

/**
 * Created by Andrew Govorovsky on 10.12.14.
 */
public class LibraryArtistsProvider implements IProvider {
    public static final String BUNDLE_USERNAME = "username";
    public static final String BUNDLE_LIMIT = "limit";
    private Context mContext;

    public LibraryArtistsProvider(Context context) {
        mContext = context;
    }

    public static class Actions {
        public static final int GET = 1;
    }

    @Override
    public void execMethod(int methodId, Bundle extraData) {

        String username = extraData.getString(BUNDLE_USERNAME);
        int limit = extraData.getInt(BUNDLE_LIMIT, 10);

        switch (methodId) {
            case Actions.GET:
                getLibraryArtists(username, limit);
                break;
        }

    }

    private void getLibraryArtists(String username, int limit) {
        ApiQuery query = new LibraryGetArtists(username, limit);
        query.prepare();
        try {
            String resp = NetworkUtils.httpRequest(query);
            ApiResponse<LibraryArtistsList> ar = LibraryHelpers.getArtistsListFromJson(resp);
            LibraryArtistsList list = ar.getData();
            if (list != null && ar.getError().isEmpty()) {

                ContentValues[] contentValues = new ContentValues[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    contentValues[i] = LibraryHelpers.getLibraryArtistContentValues(list.get(i));
                }

                mContext.getContentResolver().delete(LibraryTable.CONTENT_URI, null, null);
                mContext.getContentResolver().bulkInsert(LibraryTable.CONTENT_URI, contentValues);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
