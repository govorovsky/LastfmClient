package com.techpark.lastfmclient.api.artist;

/**
 * Created by max on 15/11/14.
 */
public class RecommendedArtist {

    public static final int RECOMMENDED_SIZE = 3;

    private String artist;
    private String similar_first;
    private String similar_second;

    public RecommendedArtist() {}

    public RecommendedArtist(String artist, String similar_first, String similar_second) {
        this.artist = artist;
        this.similar_first = similar_first;
        this.similar_second = similar_second;
    }

    public String getArtist() {
        return this.artist;
    }

    public String getSimilarFirst() {
        return this.similar_first;
    }

    public String getSimilarSecond() {
        return this.similar_second;
    }
}
