package com.techpark.lastfmclient.api.release;

import com.techpark.lastfmclient.api.artist.Artist;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by max on 27/11/14.
 */
public class Release {

    public static int RECOMMENDED_SIZE = 8;

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

    private String name;
    private String artist;
    private String url;
    private HashMap<String, String> images;
    private String date;

    public Release() {
        /* void */
    }

    public Release(String name, String artist, String url, String date, HashMap<String, String> images) {
        this.artist = artist;
        this.name = name;
        this.url = url;
        this.date = date;
        this.images = images;
    }

    public String getArtistName() { return this.artist; }
    public String getUrl() {return this.url;}
    public String getDate() {return this.date;}
    public String getReleaseName() { return this.name; }
    public String getImage(int size) {return this.images.get(ImageSizes.get(size)); }
    public String getImage(String size) {return this.images.get(size); }
    public HashMap<String, String> getImages() {return this.images; }
}
