package com.techpark.lastfmclient.api.library;

import com.techpark.lastfmclient.api.ApiParamNames;
import com.techpark.lastfmclient.api.ApiQuery;
import com.techpark.lastfmclient.network.Method;

/**
 * Created by Andrew Gov on 16.11.14.
 */
public class LibraryGetArtists extends ApiQuery {

    private String username;
    private int limit;
    private int page;


    public LibraryGetArtists(String username, int limit, int page) {
        this.username = username;
        this.limit = limit;
        this.page = page;
    }

    public LibraryGetArtists(String username, int limit) {
        this(username, limit, 1);
    }

    @Override
    public String getName() {
        return "library.getArtists";
    }

    @Override
    public void prepare() {
        entity.add(ApiParamNames.API_USER, username);
        entity.add(ApiParamNames.API_LIMIT, String.valueOf(limit));
        entity.add(ApiParamNames.API_PAGE, String.valueOf(page));
        build(this, false);
    }

    @Override
    public Method getMethod() {
        return Method.GET;
    }
}
