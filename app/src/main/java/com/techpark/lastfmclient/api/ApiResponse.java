package com.techpark.lastfmclient.api;

import com.techpark.lastfmclient.api.auth.AuthHelpers;

/**
 * Created by Andrew Gov on 14.11.14.
 */
public class ApiResponse<T> {

    private T data;
    private String error; /* TODO enum status */

    public ApiResponse(T data) {
        this(data, "");
    }

    public ApiResponse(T data, String error) {
        this.data = data;
        this.error = error;
    }

    public T getData() {
        return data;
    }

    public String getError() {
        return error;
    }
}
