package com.techpark.lastfmclient.adapters;


import com.techpark.lastfmclient.api.artist.Artist;
import com.techpark.lastfmclient.api.release.Release;

import java.util.ArrayList;

public class ReleasesList {
    private ArrayList<ReleaseWrapper> releases = new ArrayList<>();

    public static class ReleaseWrapper extends Release {
        private Artist artistObject;

        public ReleaseWrapper(String name, String artist, String url, String date, ArrayList<String> images) {
            super(name, artist, url, date, images);
        }

        public void setArtist(Artist a) {
            this.artistObject = a;
        }
        public Artist getArtist() { return this.artistObject; }
    }

    public ArrayList<ReleaseWrapper> getReleases() {
        return this.releases;
    }
    public void addRelease(ReleaseWrapper r) {
        releases.add(r);
    }
}
