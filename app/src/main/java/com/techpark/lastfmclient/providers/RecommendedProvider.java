package com.techpark.lastfmclient.providers;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;

import com.techpark.lastfmclient.adapters.RecommendedArtistList;
import com.techpark.lastfmclient.api.ApiQuery;
import com.techpark.lastfmclient.api.artist.GetRecommended;
import com.techpark.lastfmclient.api.user.UserHelpers;
import com.techpark.lastfmclient.db.ArtistsTable;
import com.techpark.lastfmclient.db.RecommendedArtistsTable;
import com.techpark.lastfmclient.network.NetworkUtils;


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

        try {
            String response = NetworkUtils.httpRequest(query);
            RecommendedArtistList rlist = UserHelpers.getRecommendedArtistsFromJSON(response); //TODO: GetRecommended in artist and this in userhelper/
            ContentResolver resolver = mContext.getContentResolver();

            for (RecommendedArtistList.RecommendedArtistWrapper r : rlist.getArtists()) {
                resolver.insert(RecommendedArtistsTable.CONTENT_URI, UserHelpers.getContentValues(r.castRecommendedArtist()));
                resolver.insert(ArtistsTable.CONTENT_URI, UserHelpers.getContentValues(r));
                resolver.insert(ArtistsTable.CONTENT_URI, UserHelpers.getContentValues(r.getSimilarFirst()));

                if (r.getSimilarSecond() != null)
                    resolver.insert(ArtistsTable.CONTENT_URI, UserHelpers.getContentValues(r.getSimilarSecond()));
            }

        } catch (Exception e) {
            //TODO: look to db
            e.printStackTrace();
        }

    }

    public RecommendedProvider(Context context) {
        this.mContext = context;
    }


}
