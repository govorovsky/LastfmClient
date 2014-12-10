package com.techpark.lastfmclient.api.user;

import com.techpark.lastfmclient.api.ApiParamNames;
import com.techpark.lastfmclient.api.ApiQuery;
import com.techpark.lastfmclient.network.Method;

/**
 * Created by max on 27/11/14.
 */
public class UserGetNewReleases extends ApiQuery {
    private String userecs;
    private String user;

    public UserGetNewReleases(String user) {
        this(user, null);
    }

    public UserGetNewReleases(String user, String userecs) {
        this.userecs = userecs;
        this.user = user;
    }

    @Override
    public String getName() {
        return "user.getNewReleases";
    }

    @Override
    public void prepare() {
        entity.add(ApiParamNames.API_USER, this.user);
        if (userecs != null)
            entity.add(ApiParamNames.API_RELEASES_SRC, userecs);
        build(this, false);
    }

    @Override
    public Method getMethod() {return Method.GET; }
}
