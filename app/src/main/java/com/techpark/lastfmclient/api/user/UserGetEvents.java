package com.techpark.lastfmclient.api.user;

import com.techpark.lastfmclient.api.ApiConstants;
import com.techpark.lastfmclient.api.ApiParamNames;
import com.techpark.lastfmclient.api.ApiQuery;
import com.techpark.lastfmclient.network.Method;

/**
 * Created by max on 27/11/14.
 */
public class UserGetEvents extends ApiQuery {
    private int mPage;
    private String sk;

    public UserGetEvents(String sk, int page) {
        this.mPage = page;
        this.sk = sk;
    }

    @Override
    public String getName() {
        return "user.getRecommendedEvents";
    }

    @Override
    public void prepare() {
        entity.add(ApiParamNames.API_LIMIT, ApiConstants.API_RECOMMENDED_PER_PAGE);
        entity.add(ApiParamNames.API_PAGE, "" + this.mPage);
        entity.add(ApiParamNames.API_SESSION_KEY, this.sk);
        //TODO: latitude, longtitude
        build(this, true);
    }

    @Override
    public Method getMethod() {
        return Method.GET;
    }
}
