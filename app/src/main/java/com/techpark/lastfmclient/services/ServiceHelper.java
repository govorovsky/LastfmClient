package com.techpark.lastfmclient.services;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.techpark.lastfmclient.db.ArtistsTable;
import com.techpark.lastfmclient.db.NewReleasesTable;
import com.techpark.lastfmclient.db.RecommendedArtistsTable;
import com.techpark.lastfmclient.db.TrackTable;
import com.techpark.lastfmclient.db.UpcomingEventsTable;
import com.techpark.lastfmclient.db.UsersTable;
import com.techpark.lastfmclient.providers.ArtistsProvider;
import com.techpark.lastfmclient.providers.EventsProvider;
import com.techpark.lastfmclient.db.LibraryTable;
import com.techpark.lastfmclient.db.RecentTracksTable;
import com.techpark.lastfmclient.providers.LibraryArtistsProvider;
import com.techpark.lastfmclient.providers.RecentTracksProvider;
import com.techpark.lastfmclient.providers.RecommendedProvider;
import com.techpark.lastfmclient.providers.ReleaseProvider;
import com.techpark.lastfmclient.providers.TrackProvider;
import com.techpark.lastfmclient.providers.UsersProvider;

/**
 * Created by Andrew Gov on 08.11.14.
 */
public class ServiceHelper {
    private Context mContext;

    public ServiceHelper(Context context) {
        this.mContext = context;
    }

    public void getUser(String username) {
        Intent intent = new Intent(mContext, ServiceProcessor.class);
        intent.putExtra(ServiceProcessor.PROVIDER, ServiceProcessor.Providers.USERS_PROVIDER);
        intent.putExtra(ServiceProcessor.METHOD, UsersProvider.Actions.GET);

        Bundle extras = new Bundle();
        extras.putString(UsersProvider.BUNDLE_USERNAME, username);
        intent.putExtras(extras);
        mContext.startService(intent);
    }

    public void getRecommendedArtists() {
        Intent intent = new Intent(mContext, ServiceProcessor.class);
        intent.putExtra(ServiceProcessor.PROVIDER, ServiceProcessor.Providers.RECOMMENDED_PROVIDER);
        intent.putExtra(ServiceProcessor.METHOD, RecommendedProvider.Actions.GET);

        mContext.startService(intent);
    }

    public void getNewReleases() {
        Intent intent = new Intent(mContext, ServiceProcessor.class);
        intent.putExtra(ServiceProcessor.PROVIDER, ServiceProcessor.Providers.NEW_RELEASES_PROVIDER);
        intent.putExtra(ServiceProcessor.METHOD, ReleaseProvider.Actions.GET);

        mContext.startService(intent);
    }

    public void getUpcomingEvents() {
        Intent intent = new Intent(mContext, ServiceProcessor.class);
        intent.putExtra(ServiceProcessor.PROVIDER, ServiceProcessor.Providers.UPCOMING_EVENTS_PROVIDER);
        intent.putExtra(ServiceProcessor.METHOD, EventsProvider.Actions.UPCOMING);

        mContext.startService(intent);
    }

    public void getRecentTracks(String username, int limit) {
        Intent intent = new Intent(mContext, ServiceProcessor.class);
        intent.putExtra(ServiceProcessor.PROVIDER, ServiceProcessor.Providers.RECENT_TRACKS_PROVIDER);
        intent.putExtra(ServiceProcessor.METHOD, RecentTracksProvider.Actions.GET);

        Bundle extras = new Bundle();
        extras.putString(RecentTracksProvider.BUNDLE_USERNAME, username);
        extras.putInt(RecentTracksProvider.BUNDLE_LIMIT, limit);
        intent.putExtras(extras);

        mContext.startService(intent);
    }

    public void getTrack(String track, String artist, String username) {
        Intent intent = new Intent(mContext, ServiceProcessor.class);
        intent.putExtra(ServiceProcessor.PROVIDER, ServiceProcessor.Providers.TRACK_PROVIDER);
        intent.putExtra(ServiceProcessor.METHOD, TrackProvider.Actions.GET);

        Bundle extras = new Bundle();
        extras.putString(TrackProvider.BUNDLE_USERNAME, username);
        extras.putString(TrackProvider.BUNDLE_TRACK_ARTIST, artist);
        extras.putString(TrackProvider.BUNDLE_TRACK_NAME, track);
        intent.putExtras(extras);

        mContext.startService(intent);
    }

    public void freeDataBase() {
        ContentResolver resolver = mContext.getContentResolver();
        resolver.delete(UsersTable.CONTENT_URI, null, null);
        resolver.delete(ArtistsTable.CONTENT_URI, null, null);
        resolver.delete(RecommendedArtistsTable.CONTENT_URI, null, null);
        resolver.delete(NewReleasesTable.CONTENT_URI, null, null);
        resolver.delete(LibraryTable.CONTENT_URI, null, null);
        resolver.delete(RecentTracksTable.CONTENT_URI, null, null);
        resolver.delete(TrackTable.CONTENT_URI, null, null);
        resolver.delete(UpcomingEventsTable.CONTENT_URI, null, null);
    }

    public void getLibraryArtists(String username, int limit) {
        Intent intent = new Intent(mContext, ServiceProcessor.class);
        intent.putExtra(ServiceProcessor.PROVIDER, ServiceProcessor.Providers.LIBRARY_PROVIDER);
        intent.putExtra(ServiceProcessor.METHOD, LibraryArtistsProvider.Actions.GET);

        Bundle extras = new Bundle();
        extras.putString(LibraryArtistsProvider.BUNDLE_USERNAME, username);
        extras.putInt(LibraryArtistsProvider.BUNDLE_LIMIT, limit);
        intent.putExtras(extras);

        mContext.startService(intent);
    }

    public void getArtist(String artist, boolean info) {
        Intent intent = new Intent(mContext, ServiceProcessor.class);
        intent.putExtra(ServiceProcessor.PROVIDER, ServiceProcessor.Providers.ARIST_PROVIDER);

        int method = (info) ? ArtistsProvider.Actions.INFO : ArtistsProvider.Actions.GET;
        intent.putExtra(ServiceProcessor.METHOD, method);

        Bundle extras = new Bundle();
        extras.putString(ArtistsProvider.BUNDLE_ARTIST, artist);
        intent.putExtras(extras);

        mContext.startService(intent);
    }
}
