package com.techpark.lastfmclient.providers;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.techpark.lastfmclient.adapters.ReleasesList;
import com.techpark.lastfmclient.api.ApiQuery;
import com.techpark.lastfmclient.api.release.ReleaseHelpers;
import com.techpark.lastfmclient.api.user.UserGetNewReleases;
import com.techpark.lastfmclient.api.user.UserHelpers;
import com.techpark.lastfmclient.db.NewReleasesTable;
import com.techpark.lastfmclient.network.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by max on 24/11/14.
 */
public class ReleaseProvider implements IProvider {
    private final Context mContext;

    public ReleaseProvider(Context context) {
        this.mContext = context;
    }

    public class Actions {
        public static final int GET = 1;
    }

    @Override
    public void execMethod(int methodId, Bundle extraData) {
        switch(methodId) {
            case Actions.GET:
                getNewReleases();
                break;
        }
    }

    public ReleasesList getNewReleasesNet() throws JSONException, IOException {
        ApiQuery query = new UserGetNewReleases(UserHelpers.getUserSessionPrefs(mContext).getString(UserHelpers.PREF_NAME, ""), "1");
        query.prepare();
        String response = NetworkUtils.httpRequest(query);

        ReleasesList list = ReleaseHelpers.getNewReleasesFromJSON(response);
        
        return list;
    }

    public void getNewReleases() {
        ArrayList<ContentValues> releasesValues = new ArrayList<>();

        try {
            ReleasesList list = getNewReleasesNet();
            for (ReleasesList.ReleaseWrapper r: list.getReleases()) {
                releasesValues.add(UserHelpers.getContentValues(r));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        ContentResolver resolver = mContext.getContentResolver();
        //TODO: httprequest on server
        ContentValues[] rV = new ContentValues[releasesValues.size()];
        rV = releasesValues.toArray(rV);
        resolver.bulkInsert(NewReleasesTable.CONTENT_URI_ID_RELEASE, rV);
    }
}
