package com.techpark.lastfmclient.api.artist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

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

    public static Artist getArtistInfoFromJSON(JSONObject json) throws JSONException {
        Artist a = getArtistFromJSON(json);

        JSONArray similars = json.getJSONObject("similar").optJSONArray("artist");
        ArrayList<String> similarArtists = new ArrayList<>();
        if (similars != null) {
            for (int i = 0; i < similars.length(); ++i)
                similarArtists.add(((JSONObject) similars.get(i)).getString("name"));
        } else {
            JSONObject similar = json.getJSONObject("similar").getJSONObject("artist"); //TODO: just similar?
            similarArtists.add(similar.getString("name"));
        }
        a.addSimilar(similarArtists);

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
        cursor.moveToFirst();

        HashMap<String, String> images = new HashMap<>();
        for (int i = 0; i < Artist.ImageSizes.size(); ++i) {
            images.put(Artist.ImageSizes.get(i), cursor.getString(i + 3));
        }

        Artist a = new Artist(
                cursor.getString(1), cursor.getString(2), images
        );

        a.setBioSummary(cursor.getString(8));
        a.setBioContent(cursor.getString(9));

        String[] tags = cursor.getString(10).split(",");
        a.addTag(new ArrayList<>(Arrays.asList(tags)));

        String[] simArtists = cursor.getString(11).split(",");
        a.addSimilar(new ArrayList<>(Arrays.asList(simArtists)));

        return a;
    }

    public static ArrayList<Artist> getSimilarArtists(Artist artist, Context context) {
        ArrayList<Artist> similars = new ArrayList<>();

        ArtistsProvider provider = new ArtistsProvider(context);
        for (String s : artist.getSimilars())
            similars.add(provider.getArtistFromDB(s));

        return similars;
    }
}
