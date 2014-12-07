package com.techpark.lastfmclient.fragments;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.techpark.lastfmclient.adapters.RecentTracksList;
import com.techpark.lastfmclient.api.ApiResponse;

/**
 * Created by Andrew Govorovsky on 08.12.14.
 */
public class RecentTracksLoader extends AsyncTaskLoader<ApiResponse<RecentTracksList>> {

    private ApiResponse<RecentTracksList> mData;
    private ApiResponse.Type type;

    public RecentTracksLoader(Context context, ApiResponse.Type type) {
        super(context);
        this.type = type;
    }

    @Override
    public ApiResponse<RecentTracksList> loadInBackground() {
        ApiResponse<RecentTracksList> apiResponse = null;
        if (type == ApiResponse.Type.CACHE) {

        }

        return apiResponse;
    }

    @Override
    protected void onStartLoading() {
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
        cancelLoad();
    }

    @Override
    protected void onReset() {
        onStopLoading();
        mData = null;
    }
}
