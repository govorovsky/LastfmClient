package com.techpark.lastfmclient.adapters;

import com.techpark.lastfmclient.api.artist.Artist;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by max on 18/12/14.
 */
public class ArtistWrapper extends Artist {
    private ArrayList<Artist> similarArtists = new ArrayList<>();

    public ArtistWrapper() {
        super();
    }

    public ArtistWrapper(String name, String url, HashMap<String, String> images) {
        super(name, url, images);
    }

    public ArtistWrapper(Artist a) {
        super(a.getArtistName(), a.getUrl(), a.getImages());
    }

    public void setSimilarArtists(ArrayList<Artist> similars) {
        this.similarArtists.addAll(similars);
    }

    public ArrayList<Artist> getSimilarArtists() {
        return this.similarArtists;
    }
}
