package com.techpark.lastfmclient.api.artist;

import com.techpark.lastfmclient.api.ApiQuery;
import com.techpark.lastfmclient.network.Method;

/**
 * Created by max on 14/11/14.
 */
public class ArtistGetInfo extends ApiQuery {
    private String artistName;

    public ArtistGetInfo(String artistName) {
        this.artistName = artistName;
    }

    @Override
    public String getName() {
        return "artist.getinfo";
    }

    @Override
    public void prepare() {
        entity.add("artist", artistName);
        build(this, false);
    }

    @Override
    public Method getMethod() {
        return Method.GET;
    }
}
