package com.techpark.lastfmclient.fragments;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.techpark.lastfmclient.api.ApiResponse;

/**
 * Created by Andrew Govorovsky on 11.12.14.
 */
public abstract class BaseLoader<T> extends AsyncTaskLoader<ApiResponse<T>> {

    protected ApiResponse<T> mData;

    public BaseLoader(Context context) {
        super(context);
    }

    @Override
    public ApiResponse<T> loadInBackground() {
        return load();
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
    public void deliverResult(ApiResponse<T> apiResponse) {
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

    protected abstract ApiResponse<T> load();
}
