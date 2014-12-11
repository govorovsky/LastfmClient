package com.techpark.lastfmclient.providers;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;

import com.techpark.lastfmclient.api.ApiQuery;
import com.techpark.lastfmclient.api.artist.Artist;
import com.techpark.lastfmclient.api.artist.ArtistGetInfo;
import com.techpark.lastfmclient.api.artist.ArtistHelpers;
import com.techpark.lastfmclient.db.ArtistsTable;
import com.techpark.lastfmclient.network.NetworkUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by max on 16/11/14.
 */
public class ArtistsProvider implements IProvider {
    private final Context mContext;

    public class Actions {
        public static final int GET = 1;
    }

    public ArtistsProvider(Context context) {
        this.mContext = context;
    }

    @Override
    public void execMethod(int methodId, Bundle extraData) {
        switch(methodId) {
            case Actions.GET:
                String artist = extraData.getString("artist");
                getArtist(artist);
                break;
        }
    }

    public Artist getArtistNet(String artistName) throws IOException, JSONException {
        ApiQuery query = new ArtistGetInfo(artistName);
        query.prepare();
        String response = NetworkUtils.httpRequest(query);

        return ArtistHelpers.getArtistFromJSON(new JSONObject(response).getJSONObject("artist"));
    }

    public void getArtist(String artistName) {
        ContentValues artistValues;

        try {
            Artist a = getArtistNet(artistName);
            artistValues = ArtistHelpers.getContentValues(a);
            ContentResolver resolver = mContext.getContentResolver();
            resolver.insert(ArtistsTable.CONTENT_URI, artistValues);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
