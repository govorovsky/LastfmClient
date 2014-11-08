package com.techpark.lastfmclient.tasks;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.techpark.lastfmclient.api.ApiQuery;
import com.techpark.lastfmclient.network.NetworkUtils;

import java.io.IOException;

/**
 * Created by andrew on 28.10.14.
 */
public class ApiQueryTask extends AsyncTaskLoader<String> {

    private ApiQuery query;
    private String resp;

    public ApiQueryTask(Context context, ApiQuery query) {
        super(context);
        this.query = query;
    }

    @Override
    public void deliverResult(String data) {
        resp = data;
        super.deliverResult(data);
    }

    @Override
    protected void onStartLoading() {
        if(resp == null) {
            forceLoad();
        }
    }

    @Override
    public String loadInBackground() {
        String res = null;
        try {
            Thread.sleep(3000);
            Log.d("LOADER", "RUNNING REQUEST");
            res = NetworkUtils.httpRequest(query);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return res;
    }
}
