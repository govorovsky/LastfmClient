package com.techpark.lastfmclient.api.user;

import com.techpark.lastfmclient.api.ApiConstants;
import com.techpark.lastfmclient.api.ApiParamNames;
import com.techpark.lastfmclient.api.ApiQuery;
import com.techpark.lastfmclient.network.Method;

/**
 * Created by max on 15/11/14.
 */
public class UserGetRecommendedArtists extends ApiQuery {
    private String sk;
    private int page;

    public UserGetRecommendedArtists(String sk, int page) {
        this.sk = sk;
        this.page = page;
    }

    @Override
    public String getName() {
        return "user.getRecommendedArtists";
    }

    @Override
    public void prepare() {
        entity.add(ApiParamNames.API_LIMIT, ApiConstants.API_RECOMMENDED_PER_PAGE);
        entity.add(ApiParamNames.API_PAGE, "" + page);
        entity.add(ApiParamNames.API_SESSION_KEY, sk);
        build(this, true);
    }

    @Override
    public Method getMethod() {
        return Method.GET;
    }

}
