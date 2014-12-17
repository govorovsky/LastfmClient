package com.techpark.lastfmclient.adapters;

import com.techpark.lastfmclient.api.artist.Artist;
import com.techpark.lastfmclient.api.event.Event;
import com.techpark.lastfmclient.api.release.Release;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by max on 27/11/14.
 */
public class EventsList {
    private ArrayList<EventWrapper> events = new ArrayList<>();
    private int totalPages = 0;

    public static class EventWrapper extends Event {
        private ArrayList<Artist> artistObjectArray;

        public EventWrapper(String title, String venue_name, String venue_location,
                     String date, String url, int attendance, HashMap<String, String> images,
                     ArrayList<String> artists)
        {
            super(title, venue_name, venue_location, date, url, attendance, images, artists);
            artistObjectArray = new ArrayList<>();
        }

        public void addArtist(Artist a) {
            this.artistObjectArray.add(a);
        }

        public ArrayList<Artist> getArtist() { return this.artistObjectArray; }
    }

    public ArrayList<EventWrapper> getEvents() {
        return this.events;
    }
    public void addEvent(EventWrapper r) {
        events.add(r);
    }
    public int getTotalPages() { return this.totalPages; }

    public void setTotalPages(int pages) { this.totalPages = pages; }
}
