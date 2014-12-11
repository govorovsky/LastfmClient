package com.techpark.lastfmclient.fragments;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.techpark.lastfmclient.adapters.ReleasesList;
import com.techpark.lastfmclient.providers.ReleaseProvider;

/**
 * Created by max on 11/12/14.
 */
class ReleaseLoader extends AsyncTaskLoader<ReleasesList> {
    private int mPage = 0;
    private ReleasesList mData;

    public ReleaseLoader(Context context, int page) {
        super(context);
        this.mPage = page;
    }

    @Override
    public ReleasesList loadInBackground() {
        ReleaseProvider provider = new ReleaseProvider(getContext());
        return provider.getReleasesDB(mPage, 10);
    }

    @Override
    public void onStartLoading() {
        if (mData == null) {
            forceLoad();
        } else {
            deliverResult(mData);
        }
    }

    @Override
    public void deliverResult(ReleasesList list) {
        mData = list;
        if (isReset()) {
            /* void */
        }

        if (isStarted()) {
            super.deliverResult(list);
        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    protected void onReset() {
        super.onReset();
    }
}
