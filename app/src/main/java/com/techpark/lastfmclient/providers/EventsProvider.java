package com.techpark.lastfmclient.providers;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;

import com.techpark.lastfmclient.adapters.EventsList;
import com.techpark.lastfmclient.api.ApiQuery;
import com.techpark.lastfmclient.api.event.EventHelpers;
import com.techpark.lastfmclient.api.user.UserGetEvents;
import com.techpark.lastfmclient.api.user.UserGetRecommendedArtists;
import com.techpark.lastfmclient.api.user.UserHelpers;
import com.techpark.lastfmclient.db.UpcomingEventsTable;
import com.techpark.lastfmclient.network.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by max on 27/11/14.
 */
public class EventsProvider implements IProvider {
    private final Context mContext;

    public class Actions {
        public static final int UPCOMING = 1;
    }

    public EventsProvider(Context context) {
        this.mContext = context;
    }

    @Override
    public void execMethod(int methodId, Bundle extraData) {
        switch(methodId) {
            case Actions.UPCOMING:
                getUpcomingEvents();
                break;
        }
    }

    public EventsList getEventsNet(int page) throws JSONException, IOException {
        ApiQuery query = new UserGetEvents(UserHelpers.getUserSession(mContext), page); //TODO: place
        query.prepare();
        String response = NetworkUtils.httpRequest(query);
        return EventHelpers.getEventsFromJSON(response);
    }

    private void getUpcomingEvents() {
        ArrayList<ContentValues> eventsValues = new ArrayList<>();

        try {
            EventsList list = getEventsNet(0);
            for (EventsList.EventWrapper e : list.getEvents()) {
                eventsValues.add(UpcomingEventsTable.getEventContentValues(e));
            }

            ContentResolver resolver = mContext.getContentResolver();
            ContentValues[] rV = new ContentValues[eventsValues.size()];
            rV = eventsValues.toArray(rV);
            resolver.bulkInsert(UpcomingEventsTable.CONTENT_URI, rV);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

