package com.techpark.lastfmclient.api.user;

import com.techpark.lastfmclient.api.ApiHelpers;
import com.techpark.lastfmclient.api.ApiQuery;
import com.techpark.lastfmclient.network.Method;

/**
 * Created by andrew on 28.10.14.
 */
public class UserGetInfo extends ApiQuery {

    private String username;

    private static final String API_USER = "user";

    public UserGetInfo(String username) {
        this.username = username;
    }

    @Override
    public String getName() {
        return "user.getinfo";
    }

    @Override
    public void prepare() {
        entity.add(API_USER, username);
        ApiHelpers.buildQuery(this, false);
    }

    @Override
    public Method getMethod() {
        return Method.GET;
    }
}
