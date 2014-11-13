package com.techpark.lastfmclient.api.auth;

import com.techpark.lastfmclient.api.ApiResponse;

import org.json.JSONObject;

/**
 * Created by Andrew Gov on 14.11.14.
 */
public class AuthHelpers {
    public static ApiResponse<Auth> parseAuthFormJson(String json) {
        try {

            JSONObject object = new JSONObject(json);
            if (!object.isNull("error")) {
                return new ApiResponse<>(new Auth(null, null), "ERROR!"); /* TODO parse error codes */
            } else {
                JSONObject session = object.getJSONObject("session");
                String name = session.getString("name");
                String key = session.getString("key");
                return new ApiResponse<>(new Auth(key, name));
            }
        } catch (Exception e) {
            return new ApiResponse<>(new Auth(null, null), "json error!");
        }
    }

}
