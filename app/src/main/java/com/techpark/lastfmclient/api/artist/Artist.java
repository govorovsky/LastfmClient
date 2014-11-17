package com.techpark.lastfmclient.api.artist;

import java.util.ArrayList;

/**
 * Created by max on 13/11/14.
 */
public class Artist {

    public static final int ARTIST_SIZE = 7;

    public class ImageSize {
        public final static int SMALL = 0;
        public final static int MEDIUM = 1;
        public final static int LARGE = 2;
        public final static int EXTRALARGE = 3;
        public final static int MEGA = 4;
    }

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
