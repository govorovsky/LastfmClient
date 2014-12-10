package com.techpark.lastfmclient.db;

import android.content.ContentValues;
import android.net.Uri;
import android.provider.BaseColumns;

import com.techpark.lastfmclient.api.artist.Artist;
import com.techpark.lastfmclient.api.event.Event;

/**
 * Created by max on 24/11/14.
 */
public class UpcomingEventsTable implements BaseColumns {
    private UpcomingEventsTable() {}

    public static final int EVENT_SIZE = 10;

    public static final String TABLE_NAME = "upcoming_events";
    public static final Uri CONTENT_URI = Uri.parse("content://" + DBLastfmHelper.AUTHORITY + "/upcoming_events");
    public static final Uri CONTENT_URI_ID_EVENT = Uri.parse("content://" + DBLastfmHelper.AUTHORITY + "/upcoming_events/");

    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_ARTIST_1 = "artist1";
    public static final String COLUMN_ARTIST_2 = "artist2";
    public static final String COLUMN_ARTIST_3 = "artist3";
    public static final String COLUMN_VENUE_NAME = "venue_name";
    public static final String COLUMN_VENUE_LOCATION = "venue_location";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_URL = "url";
    public static final String COLUMN_ATTENDANCE = "attendance";
    public static final String COLUMN_IMAGE_SMALL = "image_small";
    public static final String COLUMN_IMAGE_MEDIUM = "image_medium";
    public static final String COLUMN_IMAGE_LARGE = "image_large";
    public static final String COLUMN_IMAGE_EXTRALARGE = "image_extralarge";

    public static final String SQL_CREATE_UPCOMING_EVENTS = "CREATE TABLE " + TABLE_NAME +  " ("
            + _ID + " INTEGER PRIMARY KEY, "
            + COLUMN_TITLE + " TEXT UNIQUE, "
            + COLUMN_URL + " TEXT, "
            + COLUMN_ARTIST_1 + " TEXT, "
            + COLUMN_ARTIST_2 + " TEXT, "
            + COLUMN_ARTIST_3 + " TEXT, "
            + COLUMN_VENUE_NAME + " TEXT, "
            + COLUMN_VENUE_LOCATION + " TEXT, "
            + COLUMN_DATE + " TEXT, "
            + COLUMN_ATTENDANCE + " TEXT, "
            + COLUMN_IMAGE_SMALL + " TEXT, "
            + COLUMN_IMAGE_MEDIUM + " TEXT, "
            + COLUMN_IMAGE_LARGE + " TEXT, "
            + COLUMN_IMAGE_EXTRALARGE + " TEXT "
            + " );";


    public static ContentValues getEventContentValues(Event event) {
        ContentValues contentValues = new ContentValues(EVENT_SIZE);
        contentValues.put(COLUMN_TITLE, event.getTitle());
        contentValues.put(COLUMN_ARTIST_1, event.getArtists().get(0));

        String artist2 = null;
        String artist3 = null;

        if (event.getArtists().size() > 1)
            artist2 = event.getArtists().get(1);

        if (event.getArtists().size() > 2)
            artist3 = event.getArtists().get(2);

        contentValues.put(COLUMN_ARTIST_2, artist2);
        contentValues.put(COLUMN_ARTIST_3, artist3);
        contentValues.put(COLUMN_VENUE_NAME, event.getVenueName());
        contentValues.put(COLUMN_VENUE_LOCATION, event.getVenueLocation());
        contentValues.put(COLUMN_DATE, event.getDate());
        contentValues.put(COLUMN_URL, event.getUrl());
        contentValues.put(COLUMN_ATTENDANCE, event.getAttendance());
        contentValues.put(COLUMN_IMAGE_SMALL, event.getImage(Event.ImageSize.SMALL));
        contentValues.put(COLUMN_IMAGE_MEDIUM, event.getImage(Event.ImageSize.MEDIUM));
        contentValues.put(COLUMN_IMAGE_LARGE, event.getImage(Event.ImageSize.LARGE));
        contentValues.put(COLUMN_IMAGE_EXTRALARGE, event.getImage(Event.ImageSize.EXTRALARGE));

        return contentValues;
    }

}
