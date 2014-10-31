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
     * @param string to be encoded
     * @return 32 bytes MD5 hash
     */
    static String getMD5(String string) {
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
