package com.techpark.lastfmclient.adapters;

import com.techpark.lastfmclient.api.user.User;

/**
 * Created by andrew on 29.10.14.
 */
public class NavMenuHeader implements NavDrawerItem {
    public static final int HEADER_TYPE = 2;
    private int id;
    private int plays;
    private String since;
    private String fullname;
    private String poster;
    private String avatar;

    public static NavDrawerItem getInstance(int id, User user) {
        NavMenuHeader s = new NavMenuHeader();
        s.id = id;
        s.plays = user.getPlaycount();
        s.since = user.getRegistered();
        s.fullname = user.getFullname();
        s.avatar = user.getAvatar();
        s.poster = user.getMostPlayedArtist();
        return s;
    }

    public int getPlays() {
        return plays;
    }

    public void setPlays(int plays) {
        this.plays = plays;
    }

    public String getSince() {
        return since;
    }

    public void setSince(String since) {
        this.since = since;
    }

    public String getFullName() {
        return fullname;
    }

    public void setFullName(String fullname) {
        this.fullname = fullname;
    }



    @Override
    public int getId() {
        return id;
    }

    @Override
    public int getType() {
        return HEADER_TYPE;
    }

    @Override
    public boolean updateActionBarTitle() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public String getLabel() {
        return null;
    }

    @Override
    public boolean isUserItem() {
        return false;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }
}
