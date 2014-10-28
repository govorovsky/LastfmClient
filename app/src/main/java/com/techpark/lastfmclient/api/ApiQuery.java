package com.techpark.lastfmclient.api;

import com.techpark.lastfmclient.network.KeyValueHolder;
import com.techpark.lastfmclient.network.Method;


/**
 * Created by andrew on 28.10.14.
 */
public abstract class ApiQuery {

    protected KeyValueHolder entity = new KeyValueHolder();
    public abstract String getName();
    public abstract void prepare();
    public abstract Method getMethod();

    public KeyValueHolder getEntity() {
        return entity;
    }
}
