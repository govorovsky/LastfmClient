package com.techpark.lastfmclient.api.auth;

import com.techpark.lastfmclient.api.ApiHelpers;
import com.techpark.lastfmclient.api.ApiQuery;
import com.techpark.lastfmclient.network.Method;

/**
 * Created by andrew on 28.10.14.
 */
public class GetMobileSession extends ApiQuery {
    private String user;
    private String pass;

    private static final String API_USER = "username";
    private static final String API_PASSWORD = "password";


    @Override
    public String getName() {
        return "auth.getMobileSession";
    }

    @Override
    public void prepare() {
        entity.add(API_USER, user);
        entity.add(API_PASSWORD, pass);
        build(this, true);
    }

    @Override
    public Method getMethod() {
        return Method.POST;
    }

    public GetMobileSession(String user, String pass) {
        this.user = user;
        this.pass = pass;
    }

    public String getUser() {
        return user;
    }

    public String getPass() {
        return pass;
    }
}
