package com.techpark.lastfmclient.api.user;

import android.support.annotation.NonNull;

/**
 * Created by andrew on 30.10.14.
 */
public class User {

    static final int USER_SIZE = 8; // num of fields

    private String name;
    private String fullname;
    private String avatar;
    private String country;
    private int age;
    private String gender;
    private int playcount;
    private String registered;


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
     * Creates anon user, useful when no data
     * are available
     */
    public User() {
        this.name = "";
        this.fullname = "";
        this.avatar = "";
        this.country = "";
        this.age = -1;
        this.gender = "";
        this.playcount = 0;
        this.registered = "";
    }

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
}
