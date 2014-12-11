package com.techpark.lastfmclient.api.release;

import android.database.Cursor;

import com.techpark.lastfmclient.adapters.ReleasesList;
import com.techpark.lastfmclient.api.artist.Artist;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by max on 11/12/14.
 */
public class ReleaseHelpers {

    public static ReleasesList getNewReleasesFromJSON(String json) throws JSONException {
        ReleasesList list = new ReleasesList();

        JSONObject object = new JSONObject(json);
        JSONObject albums = object.getJSONObject("albums");

        class JSONHelper {
            public ReleasesList.ReleaseWrapper getReleaseJSON(JSONObject album) throws JSONException {
                JSONObject artist = album.getJSONObject("artist");
                JSONObject attrs = album.getJSONObject("@attr");
                JSONArray json_images = album.getJSONArray("image");

                HashMap<String, String> images = new HashMap<>();
                for (int j = 0; j< json_images.length(); ++j) {
                    JSONObject o = json_images.getJSONObject(j);
                    images.put(Release.ImageSizes.get(j), o.getString("#text"));
                }

                return new ReleasesList.ReleaseWrapper(
                        album.getString("name"), artist.getString("name"), album.getString("url"),
                        attrs.getString("releasedate"), images
                );
            }
        }

        JSONHelper h = new JSONHelper();

        JSONArray releases = albums.optJSONArray("album");
        if (releases != null) {
            for (int i = 0; i < releases.length(); ++i) {
                JSONObject album = releases.getJSONObject(i);
                list.addRelease(h.getReleaseJSON(album));
            }
        } else {
            JSONObject release = albums.getJSONObject("album");
            list.addRelease(h.getReleaseJSON(release));
        }

        return list;
    }

    public static ReleasesList getNewReleasesFromCursor(Cursor cursor, int limit) {
        ReleasesList list = new ReleasesList();

        if (limit == -1)
            limit = cursor.getCount();

        cursor.moveToLast();
        for (int i = 0; i < limit && cursor.move(0); ++i) {
            Artist artist = new Artist(
                    cursor.getString(5), null
            );
            artist.getImages().put(Artist.ImageSize.LARGE, cursor.getString(6));

            ReleasesList.ReleaseWrapper r = new ReleasesList.ReleaseWrapper(
                    cursor.getString(0), artist.getArtistName(), cursor.getString(1), cursor.getString(2), new HashMap<String, String>()
            );
            r.getImages().put(Release.ImageSize.EXTRALARGE, cursor.getString(4));

            r.setArtist(artist);

            list.addRelease(r);
            cursor.moveToPrevious();
        }
        return list;
    }
}
