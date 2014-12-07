package com.techpark.lastfmclient.api.user;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by andrew on 30.10.14.
 */
public class User implements Serializable {


    private String name;
    private String fullname;
    private String avatar;
    private String country;
    private int age;
    private String gender;
    private int playcount;
    private String registered;
    private String mostPlayedArtist;


    public User(@NonNull String name, @NonNull String fullname, @NonNull String avatar, @NonNull String country, int age, @NonNull String gender, int playcount, @NonNull String registered) {
        this.name = name;
        this.fullname = fullname;
        this.avatar = avatar;
        this.country = country;
        this.age = age;
        this.gender = gender;
        this.playcount = playcount;
        this.registered = registered;
    }


    /**
     * empty user, useful when no data
     * are available
     */
    private static class EmptyUser extends User {
        EmptyUser() {
            super("", "", "", "", -1, "", 0, "");
            setMostPlayedArtist("");
        }
    }

    public static final EmptyUser EMPTY_USER = new EmptyUser();

    public String getName() {
        return name;
    }

    public String getFullname() {
        return fullname;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getCountry() {
        return country;
    }

    public int getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public int getPlaycount() {
        return playcount;
    }

    public String getRegistered() {
        return registered;
    }

    public String getMostPlayedArtist() {
        return mostPlayedArtist;
    }

    public void setMostPlayedArtist(String mostPlayedArtist) {
        this.mostPlayedArtist = mostPlayedArtist;
    }
}
