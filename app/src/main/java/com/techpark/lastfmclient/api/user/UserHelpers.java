package com.techpark.lastfmclient.api.user;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.util.Log;

import com.techpark.lastfmclient.adapters.RecommendedArtistList;
import com.techpark.lastfmclient.db.UsersTable;
import com.techpark.lastfmclient.api.artist.ArtistHelpers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Andrew Gov on 31.10.14.
 */
public class UserHelpers {

    public static final String PREF_NAME = "username";
    public static final String PREF_SESSION_KEY = "key";
    private static final String PREF_STORAGE_FILE = "user_data";

    private static final String LOG_TAG = UserHelpers.class.getName();

    public static User getUserFromJson(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONObject user = jsonObject.getJSONObject("user");
            String name = user.getString("name");
            String realname = user.getString("realname");
            String urlAvatar = ((JSONObject) user.getJSONArray("image").get(1)).getString("#text");
            String country = user.getString("country");
            int age;
            try {
                age = Integer.parseInt(user.getString("age"));
            } catch (NumberFormatException ex) {
                age = -1;
            }
            String sex = user.getString("gender");

            int playcnt;
            try {
                playcnt = Integer.parseInt(user.getString("playcount"));
            } catch (NumberFormatException ex) {
                playcnt = 0;
            }
            String registered = user.getJSONObject("registered").getString("#text");
            Log.d(LOG_TAG, name + " " + realname + " " + urlAvatar + " " + country + " " + age + " " + sex + " " + playcnt + " " + registered);

            return new User(name, realname, urlAvatar, country, age, sex, playcnt, registered);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static User getUserFromCursor(Cursor c) {
        if (c.moveToFirst()) {
            String name = c.getString(1);
            String realname = c.getString(2);
            String urlAvatar = c.getString(3);
            String country = c.getString(4);
            int age = c.getInt(5);
            String sex = c.getString(6);
            int playcnt = c.getInt(7);
            String registered = c.getString(8);

            Log.d(LOG_TAG, name + " " + realname + " " + urlAvatar + " " + country + " " + age + " " + sex + " " + playcnt + " " + registered);
            return new User(name, realname, urlAvatar, country, age, sex, playcnt, registered);
        }
        return null;
    }

    public static ContentValues getUserContentValues(User user) {
        ContentValues contentValues = new ContentValues(User.USER_SIZE);
        contentValues.put(UsersTable.COLUMN_NAME, user.getName());
        contentValues.put(UsersTable.COLUMN_AGE, user.getAge());
        contentValues.put(UsersTable.COLUMN_REALNAME, user.getFullname());
        contentValues.put(UsersTable.COLUMN_REGISTERED, user.getRegistered());
        contentValues.put(UsersTable.COLUMN_AVATAR, user.getAvatar());
        contentValues.put(UsersTable.COLUMN_COUNTRY, user.getCountry());
        contentValues.put(UsersTable.COLUMN_PLAYCOUNT, user.getPlaycount());
        contentValues.put(UsersTable.COLUMN_GENDER, user.getGender());
        return contentValues;
    }

    public static void saveUserSession(Context c, String session, String uname) {
        SharedPreferences preferences = c.getSharedPreferences(PREF_STORAGE_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PREF_SESSION_KEY, session);
        editor.putString(PREF_NAME, uname);
        editor.apply();
    }

    public static void clearUserSession(Context c) {
        saveUserSession(c, "", "");
    }

    public static String getUserSession(Context context) {
        return context.getSharedPreferences(PREF_STORAGE_FILE, Context.MODE_PRIVATE).getString(PREF_SESSION_KEY, "");
    }

    public static SharedPreferences getUserSessionPrefs(Context context) {
        return context.getSharedPreferences(PREF_STORAGE_FILE, Context.MODE_PRIVATE);
    }

    //TODO: write to db
    public static RecommendedArtistList getRecommendedArtistsFromJSON(/*@NotNull*/ String json) throws JSONException {
        RecommendedArtistList list = new RecommendedArtistList();
        JSONObject object = new JSONObject(json);

        JSONObject recommendations = object.getJSONObject("recommendations");
        JSONArray artists = recommendations.getJSONArray("artist");

        for (int i = 0; i < artists.length(); ++i) {
            RecommendedArtistList.RecommendedArtist r = new RecommendedArtistList.RecommendedArtist(
                    ArtistHelpers.getArtistFromJSON((JSONObject) artists.get(i))
            );

            JSONObject context = ((JSONObject) artists.get(i)).getJSONObject("context");
            JSONArray similars = context.getJSONArray("artist");

            r.setSimilarArtists(
                    ArtistHelpers.getArtistFromJSON((JSONObject) similars.get(0)),
                    ArtistHelpers.getArtistFromJSON((JSONObject) similars.get(1))
            );

            list.addArtist(r);
        }
        return list;
    }


}
