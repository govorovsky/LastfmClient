package com.techpark.lastfmclient.api.artist;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by max on 13/11/14.
 */
public class Artist {

    public static final int ARTIST_SIZE = 7;

    public class ImageSize {
        public final static String SMALL = "small";
        public final static String MEDIUM = "medium";
        public final static String LARGE = "large";
        public final static String EXTRALARGE = "extralarge";
        public final static String MEGA = "mega";
    }

    public static ArrayList<String> ImageSizes = new ArrayList<>(
            (Arrays.asList(
                    ImageSize.SMALL, ImageSize.MEDIUM, ImageSize.LARGE, ImageSize.EXTRALARGE, ImageSize.MEGA
            )));

    private String artist;
    private String url;
    private HashMap<String, String> images;

    public Artist() {
        /* void */
    }

    public Artist(String artist, String url, HashMap<String, String> images) {
        this.artist = artist;
        this.url = url;
        this.images = images;
    }

    public Artist(String artist, String url) {
        this(artist, url, new HashMap<String, String>());
    }

    public String getArtistName() {
        return this.artist;
    }

    public String getUrl() {
        return this.url;
    }

    public String getImage(String size) {
        return this.images.get(size);
    }

    public String getImage(int size) {
        return this.images.get(ImageSizes.get(size));
    }

    public HashMap<String, String> getImages() {
        return this.images;
    }
}
