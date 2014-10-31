package com.techpark.lastfmclient.api;

import android.util.Log;

import com.techpark.lastfmclient.network.KeyValueHolder;
import com.techpark.lastfmclient.network.Method;

import java.util.Collections;
import java.util.LinkedList;


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

    /**
     *
     * @param query query to build
     * @param requireSig only POST requests require signature, and some GET ....
     */
    public final void build(ApiQuery query, boolean requireSig) {

        KeyValueHolder params = query.getEntity();
        params.add(ApiParamNames.API_METHOD, query.getName())
                .add(ApiParamNames.API_KEY, ApiConstants.API_KEY);

        if (requireSig) {

            LinkedList<KeyValueHolder.Holder> temp = new LinkedList<>(params.getList());

            StringBuilder stringBuilder = new StringBuilder();
            Collections.sort(temp);

            for (KeyValueHolder.Holder holder : temp) {
                stringBuilder.append(holder.getKey()).append(holder.getVal());
            }
            stringBuilder.append(ApiConstants.API_SECRET);
            params.add(ApiParamNames.API_SIG, ApiHelpers.getMD5(stringBuilder.toString()));

        }
        params.add(ApiParamNames.API_FORMAT, "json");
        Log.d("SESSION", params.toString());
    }
}
