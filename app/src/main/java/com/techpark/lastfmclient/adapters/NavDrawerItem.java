package com.techpark.lastfmclient.adapters;

/**
 * Created by andrew on 29.10.14.
 */
public interface NavDrawerItem {
    int getId();
    int getType();
    boolean updateActionBarTitle();
    boolean isEnabled();
    String getLabel();
    boolean isUserItem();

}
