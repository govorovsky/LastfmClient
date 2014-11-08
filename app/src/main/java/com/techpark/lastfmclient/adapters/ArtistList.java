package com.techpark.lastfmclient.adapters;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by max on 30/10/14.
 */
public class ArtistList {
    private ArrayList<Artist> artists = new ArrayList<>();

    public static class Artist implements MusicItem {
        private String name;
        private String url;
        private String image_url;
        private ArrayList<String> similar; //Pair?

        public Artist(String name, String url, String image) {
            this.name = name;
            this.url = url;
            this.image_url = image;
            this.similar = new ArrayList<>(Arrays.asList("band1", "band2"));
        }

        public String getName() {return this.name;}
        public String getUrl() {return this.url;}
        public String getImage() {return this.image_url;}
        public ArrayList<String> getSimilar() {return this.similar;}
    }

    public void addArtist(Artist a) {
        artists.add(a);
    }

    public ArrayList<Artist> getArtists() {
        return artists;
    }
}
