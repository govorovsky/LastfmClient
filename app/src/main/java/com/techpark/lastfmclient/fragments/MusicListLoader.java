package com.techpark.lastfmclient.fragments;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;
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
    private RecommendedArtistList mData;

    public MusicListLoader(Context context, int page) {
        super(context);
        Log.d("Cretae MusicListLoder", "" + page);
        this.mPage = page;
    }

    @Override
    public RecommendedArtistList loadInBackground() {
        if (this.mPage == 1) {
            ContentResolver resolver = getContext().getContentResolver();
            Cursor cursor = resolver.query(RecommendedArtistsTable.CONTENT_URI_ID_RECOMMENDED, null, null, null, null);
            RecommendedArtistList artistList = UserHelpers.getRecommendedArtistsFromCursor(cursor, -1);
            cursor.close();
            return artistList;
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
        if (mData == null) {
            forceLoad();
        } else {
            deliverResult(mData);
        }
    }

    @Override
    public void deliverResult(RecommendedArtistList list) {
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
