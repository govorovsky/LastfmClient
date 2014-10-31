package com.techpark.lastfmclient.api.user;

/**
 * Created by andrew on 30.10.14.
 */
public class User {
    private String name;
    private String fullname;
    private String avatar;
    private String country;
    private int age;
    private String gender;
    private int playcount;
    private String registered;


    public User(String name, String fullname, String avatar, String country, int age, String gender, int playcount, String registered) {
        this.name = name;
        this.fullname = fullname;
        this.avatar = avatar;
        this.country = country;
        this.age = age;
        this.gender = gender;
        this.playcount = playcount;
        this.registered = registered;
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
