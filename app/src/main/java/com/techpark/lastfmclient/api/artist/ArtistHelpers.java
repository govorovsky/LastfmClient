package com.techpark.lastfmclient.api.artist;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Andrew Gov on 14.11.14.
 */
public class ArtistHelpers {

    public static Artist getArtistFromJSON(JSONObject json) throws JSONException {
        JSONArray images = json.getJSONArray("image");
        ArrayList<String> imgs = new ArrayList<>();

        for (int i = 0; i < images.length(); ++i)
            imgs.add(
                    images.getJSONObject(i).getString("#text")
            );

        return new Artist(
                json.getString("name"),
                json.getString("url"),
                imgs
        );
    }
}
