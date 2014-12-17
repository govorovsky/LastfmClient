package com.techpark.lastfmclient.api.event;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.techpark.lastfmclient.adapters.EventsList;
import com.techpark.lastfmclient.api.artist.Artist;
import com.techpark.lastfmclient.db.UpcomingEventsTable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by max on 27/11/14.
 */
public class EventHelpers {

    public static EventsList getUpcomingEventsFromCursor(Cursor cursor, int limit) {
        EventsList list = new EventsList();

        if (limit == -1)
            limit = cursor.getCount();

        cursor.moveToFirst();
        for (int i = 0; i < limit; ++i) {
            EventsList.EventWrapper e = new EventsList.EventWrapper(
                    cursor.getString(0), cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), null, Integer.parseInt(cursor.getString(5)),
                    new HashMap<String, String>(), new ArrayList<String>()
            );

            e.getImages().put(Event.ImageSize.EXTRALARGE, cursor.getString(4));
            if (cursor.getString(6) != null) {
                Artist a = new Artist(
                        cursor.getString(6), null
                );
                a.getImages().put(Artist.ImageSize.LARGE, cursor.getString(9));
                a.getImages().put(Artist.ImageSize.MEGA, cursor.getString(10));
                e.addArtist(a);
            }

            if (cursor.getString(7) != null) {
                Artist a = new Artist(
                        cursor.getString(7), null
                );
                a.getImages().put(Artist.ImageSize.LARGE, cursor.getString(11));
                e.addArtist(a);
            }

            if (cursor.getString(8) != null) {
                Artist a = new Artist(
                        cursor.getString(8), null
                );
                a.getImages().put(Artist.ImageSize.LARGE, cursor.getString(12));
                e.addArtist(a);
            }

            list.addEvent(e);
            cursor.moveToNext();
        }

        return list;
    }

    public static EventsList getEventsFromJSON(String json) throws JSONException {
        EventsList list = new EventsList();
        JSONObject object = new JSONObject(json).getJSONObject("events");
        JSONArray events = object.getJSONArray("event");

        class JSONHelper {
            public EventsList.EventWrapper getEventJSON(JSONObject event) throws JSONException {
                JSONObject venue = event.getJSONObject("venue");
                JSONObject location = venue.getJSONObject("location");
                StringBuilder location_str = new StringBuilder()
                        .append(location.getString("city"))
                        .append(";")
                        .append(location.getString("country"));

                JSONArray json_images = event.getJSONArray("image");
                HashMap<String, String> images = new HashMap<>();
                for (int i = 0; i < json_images.length(); ++i) {
                    JSONObject o = json_images.getJSONObject(i);
                    images.put(Event.ImageSizes.get(i), o.getString("#text"));
                }

                JSONObject json_artists = event.getJSONObject("artists");
                JSONArray json_artists_arr = json_artists.optJSONArray("artist");
                ArrayList<String> artists = new ArrayList<>();

                if (json_artists_arr != null) {
                    for (int i = 0; i < json_artists_arr.length(); ++i) {
                        artists.add(json_artists_arr.getString(i));
                    }
                } else {
                    artists.add(json_artists.getString("artist"));
                }

                return new EventsList.EventWrapper(
                    event.getString("title"), venue.getString("name"), location_str.toString(), event.getString("startDate"),
                    event.getString("url"), Integer.parseInt(event.getString("attendance")), images, artists
                );
            }
        }

        JSONHelper h = new JSONHelper();
        if (events != null) {
            for (int i = 0; i < events.length(); ++i) {
                JSONObject e = events.getJSONObject(i);
                list.addEvent(h.getEventJSON(e));
            }
        } else {
            JSONObject e = object.getJSONObject("events");
            list.addEvent(h.getEventJSON(e));
        }

        JSONObject attrs = object.getJSONObject("@attr");
        String totalPages = attrs.getString("totalPages");
        list.setTotalPages(Integer.parseInt(totalPages));

        return list;
    }
}
