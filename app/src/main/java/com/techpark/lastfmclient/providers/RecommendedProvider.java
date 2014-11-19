package com.techpark.lastfmclient.providers;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;

import com.techpark.lastfmclient.adapters.RecommendedArtistList;
import com.techpark.lastfmclient.api.ApiQuery;
import com.techpark.lastfmclient.api.artist.ArtistHelpers;
import com.techpark.lastfmclient.api.user.GetRecommended;
import com.techpark.lastfmclient.api.user.UserHelpers;
import com.techpark.lastfmclient.db.ArtistsTable;
import com.techpark.lastfmclient.db.RecommendedArtistsTable;
import com.techpark.lastfmclient.network.NetworkUtils;

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

    private void getRecommendations() {
        ApiQuery query = new GetRecommended(UserHelpers.getUserSession(mContext));
        query.prepare();

        ArrayList<ContentValues> artistsValues = new ArrayList<>();
        ArrayList<ContentValues> recommendationsValues = new ArrayList<>();

        try {
            String response = NetworkUtils.httpRequest(query);
            RecommendedArtistList rlist = UserHelpers.getRecommendedArtistsFromJSON(response); //TODO: GetRecommended in artist and this in userhelper/

            for (RecommendedArtistList.RecommendedArtistWrapper r : rlist.getArtists()) {
                recommendationsValues.add(UserHelpers.getContentValues(r.castRecommendedArtist()));
                artistsValues.add(ArtistHelpers.getContentValues(r));
                artistsValues.add(ArtistHelpers.getContentValues(r.getSimilarFirst()));

                if (r.getSimilarSecond() != null) {
                    artistsValues.add(ArtistHelpers.getContentValues(r.getSimilarSecond()));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        ContentResolver resolver = mContext.getContentResolver();

        ContentValues[] rV = new ContentValues[recommendationsValues.size()];
        rV = recommendationsValues.toArray(rV);
        ContentValues[] aV = new ContentValues[artistsValues.size()];
        aV = artistsValues.toArray(aV);

        resolver.bulkInsert(RecommendedArtistsTable.CONTENT_URI, rV);
        resolver.bulkInsert(ArtistsTable.CONTENT_URI, aV);
        resolver.notifyChange(RecommendedArtistsTable.CONTENT_URI, null);
    }

    public RecommendedProvider(Context context) {
        this.mContext = context;
    }
}
