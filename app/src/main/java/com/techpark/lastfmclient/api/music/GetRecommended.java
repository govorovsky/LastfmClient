package com.techpark.lastfmclient.api.music;

import com.techpark.lastfmclient.api.ApiHelpers;
import com.techpark.lastfmclient.api.ApiParamNames;
import com.techpark.lastfmclient.api.ApiQuery;
import com.techpark.lastfmclient.network.Method;

/**
 * Created by max on 07/11/14.
 */
public class GetRecommended extends ApiQuery {
    private String session_key;

    public GetRecommended(String sk) {
        this.session_key = sk;
    }


    @Override
    public String getName() {
        return "user.getRecommendedArtists";
    }

    @Override
    public void prepare() {
        entity.add(ApiParamNames.API_LIMIT, "4"); //TODO: random
        entity.add(ApiParamNames.API_PAGE, "1");
        entity.add(ApiParamNames.API_SESSION_KEY, this.session_key);
        build(this, true);
    }

    @Override
    public Method getMethod() {
        return Method.GET;
    }
}
