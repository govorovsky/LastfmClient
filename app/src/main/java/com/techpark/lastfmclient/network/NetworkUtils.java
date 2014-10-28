package com.techpark.lastfmclient.network;

import android.util.Log;

import com.techpark.lastfmclient.api.ApiConstants;
import com.techpark.lastfmclient.api.ApiQuery;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by andrew on 28.10.14.
 */
public class NetworkUtils {

    private static final int READ_TIMEOUT = 10000;
    private static final int CONNECT_TIMEOUT = 15000;

    private static final String LOG_TAG = NetworkUtils.class.getName();

    public static String httpGet(String url) throws IOException {
        return httpRequest(url, null, null, Method.GET);
    }

    /**
     * @param query API query to perform
     * @return String response
     * @throws IOException
     */
    public static String httpRequest(ApiQuery query) throws IOException {
        return httpRequest(ApiConstants.API_ROOT, null, query.getEntity(), query.getMethod());
    }

    /**
     * @param url       dest
     * @param headers   http
     * @param urlParams if method is GET, params added to URL, else in POST body
     * @param method    request method
     * @return response of the server
     * @throws IOException
     */
    private static String httpRequest(String url, KeyValueHolder headers, KeyValueHolder urlParams, Method method) throws IOException {

        String postBody = "";

        if (urlParams != null) {
            StringBuilder builder = new StringBuilder(method == Method.POST ? postBody : url);
            builder.append("?");
            for (KeyValueHolder.Holder urlParam : urlParams) {
                builder.append(urlParam.getKey()).append('=').append(URLEncoder.encode(urlParam.getVal(), "UTF-8")).append('&');
            }
            builder.deleteCharAt(builder.length() - 1);
            if (method != Method.POST)
                url = builder.toString();
            else
                postBody = builder.toString();
        }

        Log.d("URL=", url);
        URL link = new URL(url);


        HttpsURLConnection urlConnection = (HttpsURLConnection) link.openConnection(); // need https always?
        urlConnection.setRequestMethod(method.name());

        if (headers != null) {
            for (KeyValueHolder.Holder header : headers) {
                urlConnection.setRequestProperty(header.getKey(), header.getVal());
            }
        }

        if (method == Method.POST) {
            urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
        }

        urlConnection.setReadTimeout(READ_TIMEOUT);
        urlConnection.setConnectTimeout(CONNECT_TIMEOUT);
//        urlConnection.connect();

        if (method == Method.POST) {
            urlConnection.setDoOutput(true);
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(urlConnection.getOutputStream());
            outputStreamWriter.write(postBody.substring(1));
            outputStreamWriter.close();
        }

        int code = urlConnection.getResponseCode();

        String resp = null;
        if (code == 200) {
            try (InputStream inputStream = urlConnection.getInputStream()) {
                resp = handleInputStream(inputStream);
            }
        }
        return resp;
    }

    private static String handleInputStream(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        StringBuilder builder = new StringBuilder();
        for (String resp; (resp = bufferedReader.readLine()) != null; builder.append(resp)) ;
        return builder.toString();
    }
}
