package com.techpark.lastfmclient.api;

import android.util.Log;

import com.techpark.lastfmclient.network.KeyValueHolder;

import java.nio.charset.Charset;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.LinkedList;

/**
 * Created by andrew on 28.10.14.
 */
public class ApiHelpers {

    /**
     *
     * @param query query to build
     * @param requireSig only POST requests require signature
     */
    public static void buildQuery(ApiQuery query, boolean requireSig) {

        KeyValueHolder params = query.getEntity();
        params.add(ApiParamNames.API_METHOD, query.getName())
                .add(ApiParamNames.API_KEY, ApiConstants.API_KEY);

        if (requireSig) {

            LinkedList<KeyValueHolder.Holder> temp = new LinkedList<>(params.getList());

            StringBuilder stringBuilder = new StringBuilder();
            Collections.sort(temp);

            for (KeyValueHolder.Holder holder : temp) {
                stringBuilder.append(holder.getKey()).append(holder.getVal());
            }
            stringBuilder.append(ApiConstants.API_SECRET);
            params.add(ApiParamNames.API_SIG, getMD5(stringBuilder.toString()));

        }
        params.add(ApiParamNames.API_FORMAT, "json");
        Log.d("SESSION", params.toString());
    }

    /**
     * @param string to be encoded
     * @return 32 bytes MD5 hash
     */
    private static String getMD5(String string) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(string.getBytes(Charset.forName("UTF-8")));
            StringBuilder stringBuilder = new StringBuilder();
            for (byte aByte : bytes) {
                stringBuilder.append(Integer.toHexString((aByte & 0xFF) | 0x100).substring(1, 3));
            }
            return stringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
        }
        return null;
    }
}
