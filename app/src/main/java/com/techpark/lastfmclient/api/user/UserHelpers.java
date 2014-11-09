package com.techpark.lastfmclient.api.user;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.techpark.lastfmclient.db.UsersTable;

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

    public static ContentValues getContentValues(User user) {
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

    public static String getUserSession(Context context) {
        return context.getSharedPreferences(PREF_STORAGE_FILE, Context.MODE_PRIVATE).getString(PREF_SESSION_KEY, null);
    }

    public static SharedPreferences getUserSessionPrefs(Context context) {
        return context.getSharedPreferences(PREF_STORAGE_FILE, Context.MODE_PRIVATE);
    }

}
