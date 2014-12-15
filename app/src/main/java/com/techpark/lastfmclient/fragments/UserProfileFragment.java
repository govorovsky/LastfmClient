package com.techpark.lastfmclient.fragments;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.techpark.lastfmclient.R;
import com.techpark.lastfmclient.adapters.LibraryArtistsAdapter;
import com.techpark.lastfmclient.adapters.LibraryArtistsList;
import com.techpark.lastfmclient.adapters.RecentTracksAdapter;
import com.techpark.lastfmclient.adapters.RecentTracksList;
import com.techpark.lastfmclient.api.library.LibraryHelpers;
import com.techpark.lastfmclient.api.track.RecentTrack;
import com.techpark.lastfmclient.api.user.User;
import com.techpark.lastfmclient.api.user.UserHelpers;
import com.techpark.lastfmclient.db.LibraryTable;
import com.techpark.lastfmclient.db.RecentTracksTable;
import com.techpark.lastfmclient.db.UsersTable;
import com.techpark.lastfmclient.views.NotifyingScrollView;
import com.techpark.lastfmclient.views.StretchedGridView;
import com.techpark.lastfmclient.views.StretchedListView;


/**
 * Created by Andrew Govorovsky on 24.11.14.
 */
public class UserProfileFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<Cursor>, NotifyingScrollView.OnScrollChangedListener {

    private static final String TAG = UserProfileFragment.class.getSimpleName();

    private static final String TITLE = "Profile";

    private static final String BUNDLE_USERNAME = "username";
    private static final String BUNDLE_USER = "user";

    private static final int USER_LOADER = 0;
    private static final int TRACKS_LOADER = 1;
    private static final int LIBRARY_LOADER = 2;

    private StretchedListView recentTracksView;
    private RecentTracksList recentTracks = new RecentTracksList();
    private RecentTracksAdapter recentTracksAdapter;

    private StretchedGridView artistsView;
    private LibraryArtistsList libArtists = new LibraryArtistsList();
    private LibraryArtistsAdapter artistsAdapter;


    private String mUsername;
    private User mUser;

    private NotifyingScrollView notifyingScrollView;
    private int newAlpha = -1;


    @Override
    protected FragmentConf getFragmentConf() {
        FragmentConf conf = new FragmentConf();
        conf.setActionBarFade(FragmentConf.ActionBarState.TRANSPARENT);
        conf.setLogo(R.drawable.slogo_with_padding);
        conf.setTitle(TITLE);
        conf.setLayout(R.layout.userprofile_fragment);
        return conf;
    }


    /* get info for custom user */
    public static UserProfileFragment getInstance(String username) {
        UserProfileFragment fragment = new UserProfileFragment();
        Bundle args = new Bundle();
        args.putString(BUNDLE_USERNAME, username);
        fragment.setArguments(args);
        return fragment;
    }

    /* get info for logged user */
    public static UserProfileFragment getInstance(User user) {
        UserProfileFragment fragment = new UserProfileFragment();
        Bundle args = new Bundle();
        args.putSerializable(BUNDLE_USER, user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUser = (User) getArguments().getSerializable(BUNDLE_USER);

        if (mUser == null) {
            mUsername = getArguments().getString(BUNDLE_USERNAME);
        } else {
            mUsername = mUser.getName();
        }

        setHasOptionsMenu(true); // TODO: add user options

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        serviceHelper.getRecentTracks(mUsername, 4);
        serviceHelper.getLibraryArtists(mUsername, 4);

        recentTracksView = (StretchedListView) view.findViewById(R.id.recent_tracks);
        artistsView = (StretchedGridView) view.findViewById(R.id.artists_grid);

        recentTracksAdapter = new RecentTracksAdapter(getActivity(), R.layout.recenttrack_item, recentTracks);
        recentTracksView.setAdapter(recentTracksAdapter);
        recentTracksView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RecentTrack t = recentTracksAdapter.getItem(position);
                fragmentDispatcher.setFragment(TrackFragment.getInstance(t.getArtist(), t.getName(), mUsername), "track", true);
            }
        });

        artistsAdapter = new LibraryArtistsAdapter(getActivity(), R.layout.lib_artist, libArtists);
        artistsView.setAdapter(artistsAdapter);

        notifyingScrollView = (NotifyingScrollView) view.findViewById(R.id.scroll);
        notifyingScrollView.setListener(this);

        RelativeLayout moreTracks = (RelativeLayout) view.findViewById(R.id.label_tracks);
        moreTracks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentDispatcher.setFragment(RecentTracksMoreFragment.getInstance(mUsername), "more_t", true);
            }
        });

        RelativeLayout moreLib = (RelativeLayout) view.findViewById(R.id.label_library);
        moreLib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentDispatcher.setFragment(LibraryMoreFragment.getInstance(mUsername), "more_l", true);
            }
        });


        if (mUser != null && mUser != User.EMPTY_USER) { // ok we already have info for this user, display it
            updateHeader(view);
            updateLabels(view);
        } else { // or download it

        }
    }

    private void updateLabels(View view) {
        TextView label_lib = (TextView) view.findViewById(R.id.label_library_text);
        String firstName = mUser.getFullname().split(" ")[0];
        if (!firstName.isEmpty()) {
            label_lib.setText(firstName + "'s Library");
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (newAlpha != -1) fragmentDispatcher.setActionBarFade(newAlpha);
        getLoaderManager().initLoader(TRACKS_LOADER, null, this);
        getLoaderManager().initLoader(LIBRARY_LOADER, null, this);
    }

    private void updateHeader(View view) {
        ImageView header = (ImageView) view.findViewById(R.id.image_header);
        Picasso.with(getActivity()).load(mUser.getMostPlayedArtist()).into(header);

        TextView fullname = (TextView) view.findViewById(R.id.full_name);
        fullname.setText(mUser.getFullname());

        TextView plays = (TextView) view.findViewById(R.id.play_cnt);
        plays.setText(mUser.getPlaycount() + " plays since " + mUser.getRegistered());

        ImageView avatar = (ImageView) view.findViewById(R.id.avatar);
        Picasso.with(getActivity()).load(mUser.getAvatar()).into(avatar);


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "Activity created");
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case TRACKS_LOADER:
                return new CursorLoader(getActivity(), RecentTracksTable.CONTENT_URI, null, null, null, null);
            case LIBRARY_LOADER:
                return new CursorLoader(getActivity(), LibraryTable.CONTENT_URI, null, null, null, null);

            case USER_LOADER:
                return new CursorLoader(getActivity(),
                        Uri.withAppendedPath(UsersTable.CONTENT_URI_ID_USER, mUsername), null, null, null, null);
            default:
                return null;

        }
    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d(TAG, "Loader onFinished");
        switch (loader.getId()) {
            case TRACKS_LOADER:
                RecentTracksList l = UserHelpers.getRecentTracksFromCursor(data);
                if (!l.isEmpty()) {
                    recentTracks.clear();
                    recentTracks.addAll(l);
                    recentTracksAdapter.notifyDataSetChanged();
                }
                break;
            case LIBRARY_LOADER:
                LibraryArtistsList libArt = LibraryHelpers.getArtistsListFromCursor(data);
                if (!libArt.isEmpty()) {
                    libArtists.clear();
                    libArtists.addAll(libArt);
                    artistsAdapter.notifyDataSetChanged();
                }
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onScrollChanged(ScrollView from, int l, int r, int oldl, int oldt) {
        final int headerHeight = getActivity().findViewById(R.id.header).getHeight() - getActivity().getActionBar().getHeight();
        final float ratio = (float) Math.min(Math.max(r, 0), headerHeight) / headerHeight;
        newAlpha = (int) (ratio * 255);
        fragmentDispatcher.setActionBarFade(newAlpha);
    }
}
