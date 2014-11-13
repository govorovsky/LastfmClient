package com.techpark.lastfmclient.api.auth;

/**
* Created by Andrew Gov on 14.11.14.
*/
public class Auth {
    private String key;
    private String name;


    public Auth(String key, String name) {
        this.key = key;
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }
}
