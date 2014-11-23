package com.techpark.lastfmclient.fragments;

import android.app.DownloadManager;
import android.content.AsyncTaskLoader;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.techpark.lastfmclient.adapters.RecommendedArtistList;
import com.techpark.lastfmclient.api.user.UserHelpers;
import com.techpark.lastfmclient.db.RecommendedArtistsTable;
import com.techpark.lastfmclient.providers.RecommendedProvider;

/**
 * Created by max on 22/11/14.
 */
public class MusicListLoader extends AsyncTaskLoader<RecommendedArtistList> {
    private int mPage = 1;

    public MusicListLoader(Context context, int page) {
        super(context);
        Log.d("Cretae MusicListLoder", "" + page);
        this.mPage = page;
    }

    @Override
    public RecommendedArtistList loadInBackground() {
        if (this.mPage == 1) {
            ContentResolver resolver = getContext().getContentResolver();
            Cursor dbdata = resolver.query(RecommendedArtistsTable.CONTENT_URI_ID_RECOMMENDED, null, null, null, null);
            return UserHelpers.getRecommendedArtistsFromCursor(dbdata, -1);
        }

        Log.d("MusicListLoader loadInBackground", "" + mPage);
        RecommendedProvider provider = new RecommendedProvider(getContext());
        try {
            RecommendedArtistList list = provider.getRecomendationsNet(mPage);
            if (list.getTotalPages() == mPage)
                return null;
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void onStartLoading() {
        forceLoad();
    }

    @Override
    public void deliverResult(RecommendedArtistList list) {
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
    public void onCanceled(RecommendedArtistList list) {
        super.onCanceled(list);
    }

    @Override
    protected void onReset() {
        super.onReset();
    }
}
