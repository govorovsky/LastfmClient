package com.techpark.lastfmclient.providers;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;

import com.techpark.lastfmclient.adapters.RecommendedArtistList;
import com.techpark.lastfmclient.api.ApiQuery;
import com.techpark.lastfmclient.api.artist.ArtistHelpers;
import com.techpark.lastfmclient.api.user.UserGetRecommendedArtists;
import com.techpark.lastfmclient.api.user.UserHelpers;
import com.techpark.lastfmclient.db.ArtistsTable;
import com.techpark.lastfmclient.db.RecommendedArtistsTable;
import com.techpark.lastfmclient.network.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;


/**
 * Created by max on 14/11/14.
 */
public class RecommendedProvider implements IProvider {
    private final Context mContext;

    public class Actions {
        public static final int GET = 1;
    }

    @Override
    public void execMethod(int methodId, Bundle extraData) {
        switch (methodId) {
            case Actions.GET:
                getRecommendations();
                break;
        }
    }

    public RecommendedArtistList getRecomendationsNet(int page) throws JSONException, IOException {
        ApiQuery query = new UserGetRecommendedArtists(UserHelpers.getUserSession(mContext), page);
        query.prepare();
        String response = NetworkUtils.httpRequest(query);
        return UserHelpers.getRecommendedArtistsFromJSON(response);
    }

    private void getRecommendations() {
        ArrayList<ContentValues> artistsValues = new ArrayList<>();
        ArrayList<ContentValues> recommendationsValues = new ArrayList<>();

        try {
            RecommendedArtistList rlist = getRecomendationsNet(0);

            for (RecommendedArtistList.RecommendedArtistWrapper r : rlist.getArtists()) {
                recommendationsValues.add(UserHelpers.getContentValues(r.castRecommendedArtist()));
                artistsValues.add(ArtistsTable.getContentValues(r));
                artistsValues.add(ArtistsTable.getContentValues(r.getSimilarFirst()));

                if (r.getSimilarSecond() != null) {
                    artistsValues.add(ArtistsTable.getContentValues(r.getSimilarSecond()));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        ContentResolver resolver = mContext.getContentResolver();

        ContentValues[] rV = new ContentValues[recommendationsValues.size()];
        rV = recommendationsValues.toArray(rV);
        ContentValues[] aV = new ContentValues[artistsValues.size()];
        aV = artistsValues.toArray(aV);

        resolver.bulkInsert(ArtistsTable.CONTENT_URI, aV);
        resolver.bulkInsert(RecommendedArtistsTable.CONTENT_URI_ID_RECOMMENDED, rV);
    }

    public RecommendedProvider(Context context) {
        this.mContext = context;
    }
}
