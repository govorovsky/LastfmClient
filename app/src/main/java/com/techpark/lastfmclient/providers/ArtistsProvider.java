package com.techpark.lastfmclient.providers;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.techpark.lastfmclient.adapters.ArtistWrapper;
import com.techpark.lastfmclient.api.ApiQuery;
import com.techpark.lastfmclient.api.artist.Artist;
import com.techpark.lastfmclient.api.artist.ArtistGetInfo;
import com.techpark.lastfmclient.api.artist.ArtistHelpers;
import com.techpark.lastfmclient.db.ArtistsTable;
import com.techpark.lastfmclient.db.LastfmContentProvider;
import com.techpark.lastfmclient.network.NetworkUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by max on 16/11/14.
 */
public class ArtistsProvider implements IProvider {
    private final Context mContext;
    public final static String BUNDLE_ARTIST = "artist";

    public class Actions {
        public static final int GET = 1;
        public static final int INFO = 2;
    }

    public ArtistsProvider(Context context) {
        this.mContext = context;
    }

    @Override
    public void execMethod(int methodId, Bundle extraData) {
        String artist = extraData.getString(BUNDLE_ARTIST);
        if (artist == null || artist.isEmpty())
            return;

        switch (methodId) {
            case Actions.GET:
                getArtist(artist); // ?
                break;
            case Actions.INFO:
                getArtistInfo(artist);
                break;
        }
    }

    public ArtistWrapper getArtistNet(String artistName) throws IOException, JSONException {
        ApiQuery query = new ArtistGetInfo(artistName);
        query.prepare();
        String response = NetworkUtils.httpRequest(query);

        return ArtistHelpers.getArtistInfoFromJSON(new JSONObject(response).getJSONObject("artist"));
    }

    public Artist getArtistFromDB(String artistName) {
        Cursor c = mContext.getContentResolver().query(
                Uri.withAppendedPath(ArtistsTable.CONTENT_URI_ID_ARTIST, artistName), null, null, null, null
        );
        return ArtistHelpers.getArtistFromCursor(c);
    }

    public void getArtist(String artistName) {
        ContentValues artistsValues;

        try {
            ArtistWrapper a = getArtistNet(artistName);
            artistsValues = ArtistsTable.getContentValues(a);
            ContentResolver resolver = mContext.getContentResolver();
            resolver.insert(ArtistsTable.CONTENT_URI, artistsValues);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getArtistInfo(String artistName) {
        ArrayList<ContentValues> artistsValues = new ArrayList<>();

        try {
            ArtistWrapper aw = getArtistNet(artistName);
            artistsValues.add(ArtistsTable.getContentValues(aw));

            for (Artist a : aw.getSimilarArtists())
                artistsValues.add(ArtistsTable.getContentValues(a));
        } catch (Exception e) {
            e.printStackTrace();
        }

        ContentResolver resolver = mContext.getContentResolver();

        ContentValues[] aV = new ContentValues[artistsValues.size()];
        aV = artistsValues.toArray(aV);

        resolver.bulkInsert(ArtistsTable.CONTENT_URI_ID_ARTIST, aV);
    }


/*    public void getArtistInfo(String artistName) {
        ArrayList<ContentValues> artistsValues = new ArrayList<>();

        try {
            Artist a = getArtistNet(artistName);
            artistsValues.add(ArtistsTable.getContentValues(a));

            for (String s : a.getSimilars())
                artistsValues.add(ArtistsTable.getContentValues(getArtistNet(s)));
        } catch (Exception e) {
            e.printStackTrace();
        }

        ContentResolver resolver = mContext.getContentResolver();

        ContentValues[] aV = new ContentValues[artistsValues.size()];
        aV = artistsValues.toArray(aV);

        resolver.bulkInsert(ArtistsTable.CONTENT_URI_ID_ARTIST, aV);
    }
*/
    static String getArtistImgNet(String artistName) throws IOException {
        ApiQuery queryArtist = new ArtistGetInfo(artistName);
        queryArtist.prepare();
        String response = NetworkUtils.httpRequest(queryArtist);
        try {
            Artist a = ArtistHelpers.getArtistFromJSON(new JSONObject(response).getJSONObject("artist"));
            return a.getImage(Artist.ImageSize.EXTRALARGE);
        } catch (JSONException e) {
            return "";
        }
    }
}
