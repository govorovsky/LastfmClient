package com.techpark.lastfmclient.api.artist;

import com.techpark.lastfmclient.api.ApiParamNames;
import com.techpark.lastfmclient.api.ApiQuery;
import com.techpark.lastfmclient.network.Method;

/**
 * Created by max on 15/11/14.
 */
public class GetRecommended extends ApiQuery {
    private String sk;

    public GetRecommended(String sk) {
        this.sk = sk;
    }

    @Override
    public String getName() {
        return "user.getRecommendedArtists";
    }

    @Override
    public void prepare() {
        //entity.add(ApiParamNames.API_LIMIT, "4"); //TODO: or get all and cursor them?
        entity.add(ApiParamNames.API_SESSION_KEY, sk);
        build(this, true);
    }

    @Override
    public Method getMethod() {
        return Method.GET;
    }

}
