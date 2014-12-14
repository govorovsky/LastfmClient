package com.techpark.lastfmclient.providers;

import android.content.Context;
import android.os.Bundle;

import com.techpark.lastfmclient.api.ApiQuery;
import com.techpark.lastfmclient.api.ApiResponse;
import com.techpark.lastfmclient.api.track.Track;
import com.techpark.lastfmclient.api.track.TrackGetInfo;
import com.techpark.lastfmclient.api.track.TrackHelpers;
import com.techpark.lastfmclient.network.NetworkUtils;

import java.io.IOException;

/**
 * Created by Andrew Govorovsky on 14.12.14.
 */
public class TrackProvider implements IProvider {

    public static final String BUNDLE_TRACK_NAME = "track";
    public static final String BUNDLE_TRACK_ARTIST = "artist";
    public static final String BUNDLE_USERNAME = "username";
    private Context mContext;

    public TrackProvider(Context context) {
        mContext = context;
    }

    public static class Actions {
        public static final int GET = 1;
        public static final int LOVE = 2;
    }

    @Override
    public void execMethod(int methodId, Bundle extraData) {

        String track = extraData.getString(BUNDLE_TRACK_NAME);
        String artist = extraData.getString(BUNDLE_TRACK_ARTIST);
        String username = extraData.getString(BUNDLE_USERNAME);

        switch (methodId) {
            case Actions.GET:
                getTrackInfo(track, artist, username);
        }

    }

    private void getTrackInfo(String track, String artist, String username) {
        ApiQuery query = new TrackGetInfo(artist, track, username);
        query.prepare();
        try {
            String resp = NetworkUtils.httpRequest(query);
            ApiResponse<Track> apiResponse = TrackHelpers.getTrackFromJson(resp);
            if (apiResponse.getError().isEmpty()) {

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
