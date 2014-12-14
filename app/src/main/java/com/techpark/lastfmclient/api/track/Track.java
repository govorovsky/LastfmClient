package com.techpark.lastfmclient.api.track;

import java.io.Serializable;

/**
 * Created by Andrew Govorovsky on 14.12.14.
 */
public class Track implements Serializable {
    private String artist;
    private String name;
    private String album = "";
    private String tags = "";
    private String artistImg = "";
    private String albumImg = "";
    private int playCnt = 0;
    private int duration;
    private String summary = "";
    private String content = "";
    private boolean loved;


    public Track() {
    }


    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }


    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public boolean isLoved() {
        return loved;
    }

    public void setLoved(boolean loved) {
        this.loved = loved;
    }


    public int getPlayCnt() {
        return playCnt;
    }

    public void setPlayCnt(int playCnt) {
        this.playCnt = playCnt;
    }

    public String getArtistImg() {
        return artistImg;
    }

    public void setArtistImg(String artistImg) {
        this.artistImg = artistImg;
    }

    public String getAlbumImg() {
        return albumImg;
    }

    public void setAlbumImg(String albumImg) {
        this.albumImg = albumImg;
    }

    @Override
    public String toString() {
        return "Track{" +
                "artist='" + artist + '\'' +
                ", name='" + name + '\'' +
                ", album='" + album + '\'' +
                ", tags='" + tags + '\'' +
                ", artistImg='" + artistImg + '\'' +
                ", albumImg='" + albumImg + '\'' +
                ", playCnt=" + playCnt +
                ", duration=" + duration +
                ", summary='" + summary + '\'' +
                ", content='" + content + '\'' +
                ", loved=" + loved +
                '}';
    }
}
