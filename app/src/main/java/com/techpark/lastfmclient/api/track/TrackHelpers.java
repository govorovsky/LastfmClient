package com.techpark.lastfmclient.api.track;

import android.content.ContentValues;
import android.util.Log;

import com.techpark.lastfmclient.db.RecentTracksTable;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Andrew Govorovsky on 03.12.14.
 */
public class TrackHelpers {

    public static RecentTrack getRecentTrackFromJson(JSONObject json) {
        try {

            String artist = json.getJSONObject("artist").getString("#text");
            String name = json.getString("name");
            String album = json.getJSONObject("album").getString("#text");
            String image = ((JSONObject) json.getJSONArray("image").get(3)).getString("#text");
            String date;
            try {
                date = json.getJSONObject("date").getString("#text");
            } catch (JSONException e) {
                date = "Now playing";
            }

            return new RecentTrack.Builder(artist, name, date).setAlbum(album).setImg(image).build();

        } catch (JSONException e) {
            Log.e("Track parsing from json", e.toString());
            return null;

        }
    }

    public static ContentValues getRecentTrackContentValues(RecentTrack track) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(RecentTracksTable.COLUMN_NAME, track.getName());
        contentValues.put(RecentTracksTable.COLUMN_ALBUM, track.getAlbum());
        contentValues.put(RecentTracksTable.COLUMN_ARTIST, track.getArtist());
        contentValues.put(RecentTracksTable.COLUMN_IMAGE, track.getImg());
        contentValues.put(RecentTracksTable.COLUMN_DATE, track.getDate());
        contentValues.put(RecentTracksTable.COLUMN_TIMESTAMP, System.currentTimeMillis());
        return contentValues;
    }


}
