package com.techpark.lastfmclient.api.track;

import android.content.ContentValues;
import android.util.Log;

import com.techpark.lastfmclient.api.ApiResponse;
import com.techpark.lastfmclient.db.RecentTracksTable;
import com.techpark.lastfmclient.db.TrackTable;

import org.json.JSONArray;
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

    public static ApiResponse<Track> getTrackFromJson(String json) {

        try {
            Track apiTrack = new Track();
            JSONObject jsonObject = new JSONObject(json);
            JSONObject track = jsonObject.getJSONObject("track");
            String name = track.getString("name");
            apiTrack.setName(name);
            int duration = track.getInt("duration");
            apiTrack.setDuration(duration);

            apiTrack.setPlayCnt(track.optInt("userplaycount"));

            apiTrack.setLoved(track.getInt("userloved") % 2 != 0);


            apiTrack.setArtist(track.getJSONObject("artist").getString("name"));

            try {
                JSONObject tags = track.getJSONObject("toptags");
                if (tags.has("tag")) {
                    JSONArray tagsArr = tags.getJSONArray("tag");
                    StringBuilder builder = new StringBuilder();
                    for (int i = 0; i < tagsArr.length(); i++) {
                        builder.append(((JSONObject) tagsArr.get(i)).get("name")).append(",");
                    }
                    builder.deleteCharAt(builder.length() - 1);
                    apiTrack.setTags(builder.toString());
                }
            } catch (JSONException e) {
            }

            if (track.has("wiki")) {
                JSONObject wiki = track.getJSONObject("wiki");
                apiTrack.setSummary(wiki.getString("summary"));
                apiTrack.setContent(wiki.getString("content"));
            }

            if (track.has("album")) {
                JSONObject albumObject = track.getJSONObject("album");
                apiTrack.setAlbum(albumObject.getString("title"));
                if (albumObject.has("image")) {
                    apiTrack.setAlbumImg(((JSONObject) albumObject.getJSONArray("image").get(3)).getString("#text"));
                }
            }

            Log.w("PARSED TRACK=", "" + apiTrack);

            return new ApiResponse<>(apiTrack);

        } catch (JSONException e) {
            e.printStackTrace();
            return new ApiResponse<>(null, "Net error");
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

    public static ContentValues getTrackContentValues(Track track) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TrackTable.COLUMN_NAME, track.getName());
        contentValues.put(TrackTable.COLUMN_ARTIST, track.getArtist());
        contentValues.put(TrackTable.COLUMN_ARTIST_IMG, track.getArtistImg());
        contentValues.put(TrackTable.COLUMN_ALBUM, track.getAlbum());
        contentValues.put(TrackTable.COLUMN_ALBUM_IMG, track.getAlbumImg());
        contentValues.put(TrackTable.COLUMN_TAGS, track.getTags());
        contentValues.put(TrackTable.COLUMN_USERLOVED, track.isLoved());
        contentValues.put(TrackTable.COLUMN_SUMMARY, track.getSummary());
        contentValues.put(TrackTable.COLUMN_CONTENT, track.getContent());
        contentValues.put(TrackTable.COLUMN_PLAYCNT, track.getPlayCnt());
        return contentValues;
    }


}
