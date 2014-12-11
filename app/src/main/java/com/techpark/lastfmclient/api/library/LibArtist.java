package com.techpark.lastfmclient.api.library;

/**
 * Created by Andrew Govorovsky on 10.12.14.
 */
public class LibArtist {
    private String name;
    private String img;
    private int playcnt;
    private String url;

    public LibArtist(String name, String img, int playcnt, String url) {
        this.name = name;
        this.img = img;
        this.playcnt = playcnt;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public String getImg() {
        return img;
    }

    public int getPlaycnt() {
        return playcnt;
    }

    public String getUrl() {
        return url;
    }
}
