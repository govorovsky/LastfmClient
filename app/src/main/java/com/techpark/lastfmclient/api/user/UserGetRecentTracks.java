package com.techpark.lastfmclient.api.user;

import com.techpark.lastfmclient.api.ApiParamNames;
import com.techpark.lastfmclient.api.ApiQuery;
import com.techpark.lastfmclient.network.Method;

/**
 * Created by Andrew Govorovsky on 03.12.14.
 */
public class UserGetRecentTracks extends ApiQuery {

    private String username;
    private int limit;
    private int page;

    public UserGetRecentTracks(String username, int limit) {
        this(username, limit, 1);
    }

    public UserGetRecentTracks(String username, int limit, int page) {
        this.username = username;
        this.limit = limit;
        this.page = page;
    }

    @Override
    public String getName() {
        return "user.getRecentTracks";
    }

    @Override
    public void prepare() {
        entity.add(ApiParamNames.API_LIMIT, "" + limit);
        entity.add(ApiParamNames.API_PAGE, "" + page);
        entity.add(ApiParamNames.API_USER, username);
        build(this, false);
    }

    @Override
    public Method getMethod() {
        return Method.GET;
    }
}
