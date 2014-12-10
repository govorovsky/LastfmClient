package com.techpark.lastfmclient.api.event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by max on 27/11/14.
 */
public class Event {

    public class ImageSize {
        public final static String SMALL = "small";
        public final static String MEDIUM = "medium";
        public final static String LARGE = "large";
        public final static String EXTRALARGE = "extralarge";
    }

    public static ArrayList<String> ImageSizes = new ArrayList<>(
            Arrays.asList(
                    ImageSize.SMALL, ImageSize.MEDIUM, ImageSize.LARGE, ImageSize.EXTRALARGE
            )
    );

    private String title;
    private String venue_name;
    private String venue_location;
    private String date;
    private String url;
    private int attendance;
    private HashMap<String, String> images;
    private ArrayList<String> artists;

    public Event(String title, String venue_name, String venue_location,
                 String date, String url, int attendance, HashMap<String, String> images,
                 ArrayList<String> artists)
    {
        this.title = title;
        this.venue_name = venue_name;
        this.venue_location = venue_location;
        this.date = date;
        this.url = url;
        this.attendance = attendance;
        this.images = images;
        this.artists = artists;
    }

    public String getTitle() { return this.title; }
    public String getVenueName() { return this.venue_name; }
    public String getVenueLocation() { return this.venue_location; }
    public String getDate() { return this.date; }
    public String getUrl() { return this.url; }
    public int getAttendance() { return this.attendance; }
    public String getImage(int size) { return this.images.get(ImageSizes.get(size)); }
    public String getImage(String size) { return this.images.get(size); }
    public ArrayList<String> getArtists() { return this.artists; }
    public HashMap<String, String> getImages() { return this.images; }

}
