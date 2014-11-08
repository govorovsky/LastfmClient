package com.techpark.lastfmclient.adapters;


import java.util.ArrayList;

public class ReleasesList {
    private ArrayList<Release> releases = new ArrayList(); //Pair?

    public static class Release implements MusicItem {
        private String band;
        private String name;
        private String url;
        private String image_url;

        public Release(String band, String name, String url, String image_url) {
            this.band = band;
            this.name = name;
            this.url = url;
            this.image_url = image_url;
        }

        public String getBand() { return this.band; }
        public String getName() { return this.name; }
        public String getUrl() { return this.url; }
        public String getImageUrl() { return this.image_url; }
    }


}
