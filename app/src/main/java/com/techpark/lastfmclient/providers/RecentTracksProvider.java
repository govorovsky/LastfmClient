package com.techpark.lastfmclient.providers;

import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.techpark.lastfmclient.adapters.RecentTracksList;
import com.techpark.lastfmclient.api.ApiQuery;
import com.techpark.lastfmclient.api.ApiResponse;
import com.techpark.lastfmclient.api.artist.ArtistGetInfo;
import com.techpark.lastfmclient.api.track.RecentTrack;
import com.techpark.lastfmclient.api.track.TrackHelpers;
import com.techpark.lastfmclient.api.user.UserGetRecentTracks;
import com.techpark.lastfmclient.api.user.UserHelpers;
import com.techpark.lastfmclient.db.RecentTracksTable;
import com.techpark.lastfmclient.network.NetworkUtils;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by Andrew Govorovsky on 03.12.14.
 */
public class RecentTracksProvider implements IProvider {

    public static final String BUNDLE_USERNAME = "username";
    public static final String BUNDLE_LIMIT = "limit";
    private Context mContext;

    public RecentTracksProvider(Context context) {
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
                getRecentTracks(username, limit);
                break;
        }

    }

    private void getRecentTracks(String username, int limit) {
        ApiQuery query = new UserGetRecentTracks(username, limit); // TODO Read limit from settings!
        query.prepare();

        try {
            String resp = NetworkUtils.httpRequest(query);
            ApiResponse<RecentTracksList> apiResponse = UserHelpers.getRecentTracksFromJson(resp);
            RecentTracksList list = apiResponse.getData();
            if (list != null && apiResponse.getError().isEmpty()) { // if error occurs, we dont cache anything
                HashMap<String, String> artistImgs = new HashMap<>();
                ApiQuery queryArtist;
                for (RecentTrack track : list) {
                    String artist = track.getArtist();
//                queryArtist = new ArtistGetInfo()

                }
                ContentValues[] contentValues = new ContentValues[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    contentValues[i] = TrackHelpers.getRecentTrackContentValues(list.get(i));
                }

                // clear old cache before insert
                if (contentValues.length > 0) {
                    mContext.getContentResolver().delete(RecentTracksTable.CONTENT_URI, null, null);
                    mContext.getContentResolver().bulkInsert(RecentTracksTable.CONTENT_URI, contentValues);
                }

                Log.d("Recent Tracks", list.toString());

            }
        } catch (IOException e) {
            Log.e("Excepiotion", e.toString());
        }
    }
}
