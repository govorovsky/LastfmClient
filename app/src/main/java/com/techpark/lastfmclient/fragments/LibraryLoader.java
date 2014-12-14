package com.techpark.lastfmclient.fragments;

import android.content.Context;
import android.database.Cursor;

import com.techpark.lastfmclient.adapters.LibraryArtistsList;
import com.techpark.lastfmclient.api.ApiQuery;
import com.techpark.lastfmclient.api.ApiResponse;
import com.techpark.lastfmclient.api.library.LibraryGetArtists;
import com.techpark.lastfmclient.api.library.LibraryHelpers;
import com.techpark.lastfmclient.api.user.UserHelpers;
import com.techpark.lastfmclient.db.LibraryTable;
import com.techpark.lastfmclient.network.NetworkUtils;

import java.io.IOException;

/**
 * Created by Andrew Govorovsky on 13.12.14.
 */
public class LibraryLoader extends BaseLoader<LibraryArtistsList> {

    private ApiResponse.Type type;
    private int page;
    private String username;

    public LibraryLoader(Context context, ApiResponse.Type type, String username) {
        this(context, type, username, 1);
    }

    public LibraryLoader(Context activity, ApiResponse.Type type, String username, int page) {
        super(activity);
        this.type = type;
        this.page = page;
        this.username = username;
    }

    @Override
    protected ApiResponse<LibraryArtistsList> load() {
        if (type == ApiResponse.Type.CACHE) {
            Cursor c = getContext().getContentResolver().query(LibraryTable.CONTENT_URI, null, null, null, null);
            LibraryArtistsList l = LibraryHelpers.getArtistsListFromCursor(c);
            c.close();
            return new ApiResponse<>(l, type);
        } else {
            ApiQuery query = new LibraryGetArtists(username, 4, page);
            query.prepare();
            try {
                String resp = NetworkUtils.httpRequest(query);
                ApiResponse<LibraryArtistsList> ar = LibraryHelpers.getArtistsListFromJson(resp);
                if (!ar.getError().isEmpty()) {
                    return new ApiResponse<>(null, ar.getError());
                }
                LibraryArtistsList list = ar.getData();
                return new ApiResponse<>(list, type);
            } catch (IOException e) {
                e.printStackTrace();
                return new ApiResponse<>(null, "Network error");
            }
        }
    }
}
