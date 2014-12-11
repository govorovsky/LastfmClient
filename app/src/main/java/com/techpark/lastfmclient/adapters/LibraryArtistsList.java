package com.techpark.lastfmclient.adapters;

import com.techpark.lastfmclient.api.library.LibArtist;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Andrew Govorovsky on 10.12.14.
 */
public class LibraryArtistsList extends ArrayList<LibArtist> {

    private int totalPages;
    private int totalArtists;

    public LibraryArtistsList() {
    }

    public LibraryArtistsList(Collection<? extends LibArtist> collection) {
        super(collection);
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }


    public void setTotalArtists(int totalArtists) {
        this.totalArtists = totalArtists;
    }

    public int getTotalArtists() {
        return totalArtists;
    }
}
