package com.techpark.lastfmclient.adapters;

import com.techpark.lastfmclient.api.artist.Artist;
import com.techpark.lastfmclient.api.user.RecommendedArtist;

import java.util.ArrayList;

/**
 * Created by max on 30/10/14.
 */
public class RecommendedArtistList {
    private ArrayList<RecommendedArtistWrapper> artists = new ArrayList<>();

    public static class RecommendedArtistWrapper extends Artist {
        private Artist similar_first;
        private Artist similar_second;

        public RecommendedArtistWrapper(Artist a) {
            super(a.getArtistName(), a.getUrl(), a.getImages());
        }

        public RecommendedArtistWrapper(String artist, String url, ArrayList<String> images) {
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

        public RecommendedArtist castRecommendedArtist() {
            String second = null;
            if (this.similar_second != null)
                second = this.similar_second.getArtistName();

            return new RecommendedArtist(
                    this.getArtistName(),
                    similar_first.getArtistName(),
                    second
            );
        }
    }

    public void addArtist(RecommendedArtistWrapper a) {
        artists.add(a);
    }

    public ArrayList<RecommendedArtistWrapper> getArtists() {
        return artists;
    }
}
