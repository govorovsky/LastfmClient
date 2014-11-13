package com.techpark.lastfmclient.api.user;

import com.techpark.lastfmclient.api.ApiParamNames;
import com.techpark.lastfmclient.api.ApiQuery;
import com.techpark.lastfmclient.network.Method;

/**
 * Created by max on 07/11/14.
 */
public class UserGetRecommendedArtists extends ApiQuery {
    private String session_key;

    public UserGetRecommendedArtists(String sk) {
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
