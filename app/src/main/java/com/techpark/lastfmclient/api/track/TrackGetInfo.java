package com.techpark.lastfmclient.api.track;

import com.techpark.lastfmclient.api.ApiParamNames;
import com.techpark.lastfmclient.api.ApiQuery;
import com.techpark.lastfmclient.network.Method;

/**
 * Created by Andrew Govorovsky on 14.12.14.
 */
public class TrackGetInfo extends ApiQuery {

    private String artist;
    private String user;
    private String track;

    public TrackGetInfo(String artist, String track, String user) {
        this.artist = artist;
        this.user = user;
        this.track = track;
    }

    @Override
    public String getName() {
        return "track.getInfo";
    }

    @Override
    public void prepare() {
        entity.add(ApiParamNames.API_USERNAME, user);
        entity.add(ApiParamNames.API_ARTIST, artist);
        entity.add(ApiParamNames.API_TRACK, track);
        build(this, false);
    }

    @Override
    public Method getMethod() {
        return Method.GET;
    }
}
