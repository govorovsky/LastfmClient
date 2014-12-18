package com.techpark.lastfmclient.api.artist;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by max on 13/11/14.
 */
public class Artist implements Serializable {

    public static final int ARTIST_SIZE = 11;

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
    private String bioSummary;
    private String bioContent;
    private HashMap<String, String> images;
    private ArrayList<String> tags;
    private ArrayList<String> similars;

    public Artist() {
        /* void */
    }

    public Artist(String artist, String url, HashMap<String, String> images) {
        this.artist = artist;
        this.url = url;
        this.images = images;

        this.tags = new ArrayList<>();
        this.similars = new ArrayList<>();

        this.bioContent = "";
        this.bioSummary = "";
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

    public ArrayList<String> getTags() { return this.tags; }

    public ArrayList<String> getSimilars() { return this.similars; }

    public String getBioSummary() { return this.bioSummary; }

    public String getBioContent() { return this.bioContent; }

    public void setBioSummary(String summary) { this.bioSummary = summary; }

    public void setBioContent(String content) { this.bioContent = content; }

    public void addTag(ArrayList<String> tags) { this.tags.addAll(tags); }
    public void addTag(String tag) { this.tags.add(tag); }

    public void addSimilar(ArrayList<String> similars) { this.similars.addAll(similars); }
    public void addSimilar(String similar) { this.similars.add(similar); }
    public void addSimilar(Artist a) { this.similars.add(a.getArtistName()); }
}
