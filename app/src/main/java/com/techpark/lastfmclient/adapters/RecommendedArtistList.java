package com.techpark.lastfmclient.adapters;

import com.techpark.lastfmclient.api.artist.Artist;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by max on 30/10/14.
 */
public class RecommendedArtistList {
    private ArrayList<RecommendedArtist> artists = new ArrayList<>();

    public static class RecommendedArtist extends Artist {
        private Artist similar_first;
        private Artist similar_second;

        public RecommendedArtist(Artist a) {
            super(a.getArtistName(), a.getUrl(), a.getImages());
        }

        public RecommendedArtist(String artist, String url, ArrayList<String> images) {
            super(artist, url, images);
        }

        public void setSimilarArtists(Artist similar_first, Artist similar_second) {
            this.similar_first = similar_first;
            this.similar_second = similar_second;
        }

        public Artist getSimilarFirst() {
            return this.similar_first;
        }

        public Artist getSimilarSecond() {
            return this.similar_second;
        }
    }

    public void addArtist(RecommendedArtist a) {
        artists.add(a);
    }

    public ArrayList<RecommendedArtist> getArtists() {
        return artists;
    }
}
