package com.techpark.lastfmclient.adapters;

import com.techpark.lastfmclient.api.track.RecentTrack;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Andrew Govorovsky on 03.12.14.
 */
public class RecentTracksList extends ArrayList<RecentTrack> {
    private int totalPages;

    public RecentTracksList(){

    }

    public RecentTracksList(Collection<? extends RecentTrack> collection) {
        super(collection);
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
