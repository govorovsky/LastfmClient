package com.techpark.lastfmclient.api.artist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.techpark.lastfmclient.adapters.ArtistWrapper;
import com.techpark.lastfmclient.db.ArtistsTable;
import com.techpark.lastfmclient.providers.ArtistsProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by Andrew Gov on 14.11.14.
 */
public class ArtistHelpers {

    public static ArtistWrapper getArtistInfoFromJSON(JSONObject json) throws JSONException {
        ArtistWrapper a = new ArtistWrapper(getArtistFromJSON(json));

        JSONArray similars = json.getJSONObject("similar").optJSONArray("artist");
        ArrayList<Artist> similarArtists = new ArrayList<>();
        ArrayList<String> similarNames = new ArrayList<>();
        if (similars != null) {
            for (int i = 0; i < similars.length(); ++i) {
                similarArtists.add(getArtistFromJSON(similars.getJSONObject(i)));
                similarNames.add(similarArtists.get(i).getArtistName());
            }
        } else {
            JSONObject similar = json.getJSONObject("similar").getJSONObject("artist"); //TODO: just similar?
            similarArtists.add(getArtistFromJSON(similar));
            similarNames.add(similarArtists.get(0).getArtistName());
        }
        a.setSimilarArtists(similarArtists);
        a.addSimilar(similarNames);

        JSONArray tags = json.getJSONObject("tags").optJSONArray("tag");
        ArrayList<String> artistTags = new ArrayList<>();
        if (tags != null) {
            for (int i = 0; i < tags.length(); ++i)
                artistTags.add(((JSONObject) tags.get(i)).getString("name"));
        } else {
            JSONObject tag = json.getJSONObject("tags").getJSONObject("tag"); //TODO the same
            artistTags.add(tag.getString("name"));
        }
        a.addTag(artistTags);

        JSONObject bio = json.getJSONObject("bio");
        a.setBioContent(bio.getString("content"));
        a.setBioSummary(bio.getString("summary"));

        return a;
    }

    public static Artist getArtistFromJSON(JSONObject json) throws JSONException {
        Log.d("getArtistFromJSON", json.toString());
        JSONArray images = json.getJSONArray("image");
        HashMap<String, String> imgs = new HashMap<>();

        for (int i = 0; i < images.length(); ++i)
            imgs.put(
                    Artist.ImageSizes.get(i),
                    images.getJSONObject(i).getString("#text")
            );

        return new Artist(
                json.getString("name"),
                json.getString("url"),
                imgs
        );
    }

    public static Artist getArtistFromCursor(Cursor cursor) {
        Log.d("getArtistFromCursor", "here");
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getColumnCount(); ++i)
            Log.d("TAGSTAGS: " + cursor.getColumnName(i), cursor.getString(i));

        HashMap<String, String> images = new HashMap<>();
        for (int i = 0; i < Artist.ImageSizes.size(); ++i) {
            images.put(Artist.ImageSizes.get(i), cursor.getString(i + 3));
        }

        Artist a = new Artist(
                cursor.getString(1), cursor.getString(2), images
        );

        String[] tags = cursor.getString(8).split(",");
        a.addTag(new ArrayList<>(Arrays.asList(tags)));

        String[] simArtists = cursor.getString(9).split(",");
        a.addSimilar(new ArrayList<>(Arrays.asList(simArtists)));

        a.setBioSummary(cursor.getString(10));
        a.setBioContent(cursor.getString(11));

        return a;
    }

    public static ArrayList<Artist> getSimilarArtists(String artist, Context context, int limit) {
        ArtistsProvider provider = new ArtistsProvider(context);
        Artist a = provider.getArtistFromDB(artist);

        return getSimilarArtists(a, context, limit);
    }

    public static ArrayList<Artist> getSimilarArtists(Artist artist, Context context, int limit) {
        ArrayList<Artist> similars = new ArrayList<>();

        if (limit == -1)
            limit = artist.getSimilars().size();

        ArtistsProvider provider = new ArtistsProvider(context);
        for (int i = 0; i < artist.getSimilars().size() && i < limit; ++i) {
            Log.d("getSiilarArtists NNN", artist.getSimilars().get(i));
            similars.add(provider.getArtistFromDB(
                    artist.getSimilars().get(i)
            ));
        }
        return similars;
    }
}
