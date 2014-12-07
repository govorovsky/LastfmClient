package com.techpark.lastfmclient.api.track;

/**
 * Created by Andrew Govorovsky on 03.12.14.
 */
public class RecentTrack {
    private final String artist;
    private final String name;
    private final String album;
    private final String img;
    private final String date;


    public static class Builder {

        private final String artist;
        private final String name;
        private final String date;

        private String album = "";
        private String img = "";

        public Builder(String artist, String name, String date) {
            this.artist = artist;
            this.name = name;
            this.date = date;
        }

        public Builder setAlbum(String album) {
            this.album = album;
            return this;
        }

        public Builder setImg(String img) {
            this.img = img;
            return this;
        }

        public RecentTrack build() {
            return new RecentTrack(this);
        }
    }

    private RecentTrack(Builder b) {
        this.artist = b.artist;
        this.name = b.name;
        this.date = b.date;
        this.album = b.album;
        this.img = b.img;
    }

    @Override
    public String toString() {
        return "RecentTrack{" +
                "artist='" + artist + '\'' +
                ", name='" + name + '\'' +
                ", album='" + album + '\'' +
                ", img='" + img + '\'' +
                ", date='" + date + '\'' +
                '}';
    }

    public String getArtist() {
        return artist;
    }


    public String getName() {
        return name;
    }


    public String getAlbum() {
        return album;
    }


    public String getImg() {
        return img;
    }


    public String getDate() {
        return date;
    }

}
