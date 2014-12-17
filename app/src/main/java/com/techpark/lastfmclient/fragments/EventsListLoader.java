package com.techpark.lastfmclient.fragments;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.techpark.lastfmclient.adapters.EventsList;
import com.techpark.lastfmclient.adapters.RecommendedArtistList;
import com.techpark.lastfmclient.api.event.EventHelpers;
import com.techpark.lastfmclient.db.UpcomingEventsTable;
import com.techpark.lastfmclient.providers.EventsProvider;

/**
 * Created by max on 17/12/14.
 */
public class EventsListLoader extends AsyncTaskLoader<EventsList> {
    private int mPage = 1;
    private EventsList mData;

    public EventsListLoader(Context context, int page) {
        super(context);
        this.mPage = page;
    }

    @Override
    public EventsList loadInBackground() {
        if (this.mPage == 1) {
            ContentResolver resolver = getContext().getContentResolver();
            Cursor cursor = resolver.query(UpcomingEventsTable.CONTENT_URI_ID_EVENT, null, null, null, null);
            EventsList list = EventHelpers.getUpcomingEventsFromCursor(cursor, -1);
            cursor.close();
            return list;
        }

        Log.d("MusicListLoader loadInBackground", "" + mPage);
        EventsProvider provider = new EventsProvider(getContext());
        try {
            EventsList list = provider.getEventsNet(mPage);
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
    public void deliverResult(EventsList list) {
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
