package com.techpark.lastfmclient.fragments;

import android.content.Context;
import android.database.Cursor;

import com.techpark.lastfmclient.adapters.RecentTracksList;
import com.techpark.lastfmclient.api.ApiQuery;
import com.techpark.lastfmclient.api.ApiResponse;
import com.techpark.lastfmclient.api.user.UserGetRecentTracks;
import com.techpark.lastfmclient.api.user.UserHelpers;
import com.techpark.lastfmclient.db.RecentTracksTable;
import com.techpark.lastfmclient.network.NetworkUtils;

import java.io.IOException;

/**
 * Created by Andrew Govorovsky on 08.12.14.
 */
public class RecentTracksLoader extends BaseLoader<RecentTracksList> {

    private ApiResponse.Type type;
    private int page;
    private String username;

    public RecentTracksLoader(Context context, ApiResponse.Type type, String username) {
        this(context, type, username, 1);
    }

    public RecentTracksLoader(Context activity, ApiResponse.Type type, String username, int page) {
        super(activity);
        this.type = type;
        this.page = page;
        this.username = username;
    }

    @Override
    protected ApiResponse<RecentTracksList> load() {
        if (type == ApiResponse.Type.CACHE) {
            // todo add settings!
            Cursor c = getContext().getContentResolver().query(RecentTracksTable.CONTENT_URI, null, null, null, "15");
            RecentTracksList l = UserHelpers.getRecentTracksFromCursor(c);
            c.close();
            return new ApiResponse<>(l, type);
        } else if (type == ApiResponse.Type.API) {
            ApiQuery query = new UserGetRecentTracks(username, 15, page);
            query.prepare();
            try {
                String resp = NetworkUtils.httpRequest(query);
                ApiResponse<RecentTracksList> apiResp = UserHelpers.getRecentTracksFromJson(resp);
                RecentTracksList list = apiResp.getData();
                if (list != null && list.size() > 4) {
                    list.remove(0);
                }
                return apiResp;
            } catch (IOException e) {
                return new ApiResponse<>(null, "Network error");
            }
        }
        return null;
    }
}
