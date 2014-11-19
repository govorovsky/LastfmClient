package com.techpark.lastfmclient.api.library;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Andrew Govorovsky on 16.11.14.
 */
public class LibraryHelpers {
    public static String parseMostPlayedFromJson(String json) {
        try {
            JSONObject resp = new JSONObject(json);
            JSONObject artists = resp.getJSONObject("artists");
            JSONObject attr = artists.getJSONObject("@attr");
            int perPage = attr.getInt("perPage");
            if (perPage == 1) {
                JSONObject artist = artists.getJSONObject("artist");
                JSONObject image = (JSONObject) artist.getJSONArray("image").get(4);
                return image.getString("#text");
            } else {
                JSONArray artist = artists.getJSONArray("artist");
                JSONObject firstArtist = ((JSONObject) artist.get(0));
                JSONObject image = (JSONObject) firstArtist.getJSONArray("image").get(4);
                return image.getString("#text");
            }
        } catch (JSONException e) {
            return "";
        }
    }
}
