package com.techpark.lastfmclient.api.library;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.techpark.lastfmclient.adapters.LibraryArtistsList;
import com.techpark.lastfmclient.api.ApiResponse;
import com.techpark.lastfmclient.db.LibraryTable;

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

    public static ApiResponse<LibraryArtistsList> getArtistsListFromJson(String json) {
        try {
            Log.e("JSON=", json);
            JSONObject resp = new JSONObject(json);
            JSONObject artists = resp.getJSONObject("artists");
            JSONObject attr;
            try {
                attr = artists.getJSONObject("@attr");
            } catch (JSONException e) {
                e.printStackTrace();
                return new ApiResponse<>(null, "No artists");
            }
            int totalPages = attr.getInt("totalPages");
            int totalArtists = attr.getInt("total");
            JSONArray artistArr = artists.getJSONArray("artist");
            LibraryArtistsList al = new LibraryArtistsList();
            for (int i = 0; i < artistArr.length(); i++) {
                JSONObject artist = ((JSONObject) artistArr.get(i));
                String name = artist.getString("name");
                int playcnt = artist.getInt("playcount");
                JSONObject imageObj = (JSONObject) artist.getJSONArray("image").get(4);
                String img = imageObj.getString("#text");
                String url = artist.getString("url");
                LibArtist libArtist = new LibArtist(name, img, playcnt, url);
                al.add(libArtist);
            }
            al.setTotalPages(totalPages);
            al.setTotalArtists(totalArtists);
            Log.d("library artists", " " + al);
            return new ApiResponse<>(al);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static LibraryArtistsList getArtistsListFromCursor(Cursor cursor) {
        LibraryArtistsList list = new LibraryArtistsList();
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(1);
                int playcnt = cursor.getInt(2);
                String img = cursor.getString(3);
                String url = cursor.getString(4);
                list.add(new LibArtist(name, img, playcnt, url));
            } while (cursor.moveToNext());
        }
        return list;
    }

    public static ContentValues getLibraryArtistContentValues(LibArtist libArtist) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(LibraryTable.COLUMN_NAME, libArtist.getName());
        contentValues.put(LibraryTable.COLUMN_COVER, libArtist.getImg());
        contentValues.put(LibraryTable.COLUMN_URL, libArtist.getUrl());
        contentValues.put(LibraryTable.COLUMN_PLAYCNT, libArtist.getPlaycnt());
        return contentValues;
    }
}
