package com.techpark.lastfmclient.providers;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;

/**
 * Created by max on 16/11/14.
 */
public class ArtistsProvider implements IProvider {
    private final Context mContext;

    public class Actions {
        public static final int GET = 1;
    }

    public ArtistsProvider(Context context) {
        this.mContext = context;
    }

    @Override
    public void execMethod(int methodId, Bundle extraData) {
        switch(methodId) {
            case Actions.GET:
                String artist = extraData.getString("artist");
                getArtist(artist);
                break;
        }
    }

    private void getArtist(String artist) {
        ContentResolver resolver = mContext.getContentResolver();
        //TODO
    }
}
