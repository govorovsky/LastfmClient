package com.techpark.lastfmclient.api.artist;

import java.util.ArrayList;

/**
 * Created by max on 13/11/14.
 */
public class Artist {
    public static int IMAGE_SMALL = 0;
    public static int IMAGE_MEDIUM = 1;
    public static int IMAGE_LARGE = 2;
    public static int IMAGE_EXTRALARGE = 3;
    public static int IMAGE_MEGA = 4;

    private String artist;
    private String url;
    private ArrayList<String> images;

    public Artist() {
        /* void */
    }

    public Artist(String artist, String url, ArrayList<String> images) {
        this.artist = artist;
        this.url = url;
        this.images = images;
    }

    public String getArtistName() {
        return this.artist;
    }

    public String getUrl() {
        return this.url;
    }

    public String getImage(int size) {
        return this.images.get(size);
    }

    public ArrayList<String> getImages() {
        return this.images;
    }
}
