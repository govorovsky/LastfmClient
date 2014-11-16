package com.techpark.lastfmclient.api.user;

import com.techpark.lastfmclient.api.ApiParamNames;
import com.techpark.lastfmclient.api.ApiQuery;
import com.techpark.lastfmclient.network.Method;

/**
 * Created by andrew on 28.10.14.
 */
public class UserGetInfo extends ApiQuery {

    private String username;


    public UserGetInfo(String username) {
        this.username = username;
    }

    @Override
    public String getName() {
        return "user.getinfo";
    }

    @Override
    public void prepare() {
        entity.add(ApiParamNames.API_USER, username);
        build(this, false);
    }

    @Override
    public Method getMethod() {
        return Method.GET;
    }
}
