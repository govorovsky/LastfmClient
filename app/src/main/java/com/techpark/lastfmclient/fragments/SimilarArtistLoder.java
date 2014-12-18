package com.techpark.lastfmclient.fragments;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;

import com.techpark.lastfmclient.api.artist.Artist;
import com.techpark.lastfmclient.api.artist.ArtistHelpers;
import com.techpark.lastfmclient.db.ArtistsTable;

import java.util.ArrayList;

/**
 * Created by max on 18/12/14.
 */
public class SimilarArtistLoder extends AsyncTaskLoader<ArrayList<Artist>> {
    private ArrayList<Artist> mData;
    private String artistName;

    public SimilarArtistLoder(Context context, String artist) {
        super(context);
        this.artistName = artist;
    }

    @Override
    public ArrayList<Artist> loadInBackground() {
        return ArtistHelpers.getSimilarArtists(artistName, getContext(), -1);
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
    public void deliverResult(ArrayList<Artist> list) {
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
