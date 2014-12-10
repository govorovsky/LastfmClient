package com.techpark.lastfmclient.fragments;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

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
public class RecentTracksLoader extends AsyncTaskLoader<ApiResponse<RecentTracksList>> {

    private ApiResponse<RecentTracksList> mData;
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
    public ApiResponse<RecentTracksList> loadInBackground() {

        if (type == ApiResponse.Type.CACHE) {
            Cursor c = getContext().getContentResolver().query(RecentTracksTable.CONTENT_URI, null, null, null, null);
            RecentTracksList l = UserHelpers.getRecentTracksFromCursor(c);
            c.close();
            return new ApiResponse<>(l, type);
        } else if (type == ApiResponse.Type.API) {
            try {
                Thread.currentThread().sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ApiQuery query = new UserGetRecentTracks(username, 4, page);
            query.prepare();
            try {
                String resp = NetworkUtils.httpRequest(query);
                ApiResponse<RecentTracksList> apiResp = UserHelpers.getRecentTracksFromJson(resp);
                RecentTracksList list = apiResp.getData();
                if (list.size() > 4) {
                    list.remove(0);
                }
                return apiResp;
            } catch (IOException e) {
                return new ApiResponse<>(null, "Network error");
            }
        }
        return null;
    }

    @Override
    protected void onStartLoading() {
        Log.e("ON START LOADING", "DDS");
        if (mData == null) {
            forceLoad();
        } else {
            deliverResult(mData);
        }
    }

    @Override
    public void deliverResult(ApiResponse<RecentTracksList> apiResponse) {
        mData = apiResponse;
        if (isStarted()) {
            super.deliverResult(apiResponse);
        }
    }

    @Override
    protected void onStopLoading() {
        Log.e("ON STOP LOADING", "DDS");
        cancelLoad();
    }

    @Override
    protected void onReset() {
        onStopLoading();
        mData = null;
    }
}
