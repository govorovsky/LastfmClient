package com.techpark.lastfmclient.api.artist;

import android.content.ContentValues;

import com.techpark.lastfmclient.db.ArtistsTable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Andrew Gov on 14.11.14.
 */
public class ArtistHelpers {

    public static ContentValues getContentValues(Artist artist) {
        ContentValues content = new ContentValues(Artist.ARTIST_SIZE);
        content.put(ArtistsTable.COLUMN_NAME, artist.getArtistName());
        content.put(ArtistsTable.COLUMN_URL, artist.getUrl());
        content.put(ArtistsTable.COLUMN_IMAGE_SMALL, artist.getImage(Artist.ImageSize.SMALL));
        content.put(ArtistsTable.COLUMN_IMAGE_MEDIUM, artist.getImage(Artist.ImageSize.MEDIUM));
        content.put(ArtistsTable.COLUMN_IMAGE_LARGE, artist.getImage(Artist.ImageSize.LARGE));
        content.put(ArtistsTable.COLUMN_IMAGE_EXTRALARGE, artist.getImage(Artist.ImageSize.EXTRALARGE));
        content.put(ArtistsTable.COLUMN_IMAGE_MEGA, artist.getImage(Artist.ImageSize.MEGA));
        return content;
    }


    public static Artist getArtistFromJSON(JSONObject json) throws JSONException {
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
}
