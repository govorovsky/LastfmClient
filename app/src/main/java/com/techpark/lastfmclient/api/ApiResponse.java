package com.techpark.lastfmclient.api;


import com.techpark.lastfmclient.adapters.RecentTracksList;

/**
 * Created by Andrew Gov on 14.11.14.
 */
public class ApiResponse<T> {

    private T data;
    private String error; /* TODO enum status */
    private Type type;

    public ApiResponse(T data, Type type) {
        this.error = "";
        this.data = data;
        this.type = type;
    }


    public enum Type {
        CACHE, API
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

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
