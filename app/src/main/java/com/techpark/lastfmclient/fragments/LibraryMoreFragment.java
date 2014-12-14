package com.techpark.lastfmclient.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;
import android.widget.GridView;

import com.techpark.lastfmclient.R;
import com.techpark.lastfmclient.adapters.LibraryArtistsAdapter;
import com.techpark.lastfmclient.adapters.LibraryArtistsList;
import com.techpark.lastfmclient.api.ApiParamNames;
import com.techpark.lastfmclient.api.ApiResponse;

/**
 * Created by Andrew Govorovsky on 12.12.14.
 */
public class LibraryMoreFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<ApiResponse<LibraryArtistsList>> {


    private static final String BUNDLE_USERNAME = "username";
    private int itemCount = 4;

    private String username;

    private LibraryArtistsList artistsList = new LibraryArtistsList();
    private LibraryArtistsAdapter adapter;

    private MoreFragmentScrollListener scrollListener = new MoreFragmentScrollListener(itemCount) {
        @Override
        void loadMore() {
            Bundle args = new Bundle();
            args.putInt(ApiParamNames.API_PAGE, currentPage);
            getLoaderManager().restartLoader(0, args, LibraryMoreFragment.this);
        }
    };
    private boolean isPaused = false;

    public static LibraryMoreFragment getInstance(String username) {
        LibraryMoreFragment fragment = new LibraryMoreFragment();
        Bundle args = new Bundle();
        args.putString(BUNDLE_USERNAME, username);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        username = getArguments().getString(BUNDLE_USERNAME);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        GridView gridView = (GridView) view.findViewById(R.id.lib_list);
        adapter = new LibraryArtistsAdapter(getActivity(), R.layout.lib_artist, artistsList);
        gridView.setAdapter(adapter);
        gridView.setOnScrollListener(scrollListener);

    }

    @Override
    protected FragmentConf getFragmentConf() {
        FragmentConf conf = new FragmentConf();
        conf.setLayout(R.layout.more_library_fragment);
        conf.setLogo(R.drawable.slogo_with_padding);
        conf.setActionBarFade(FragmentConf.ActionBarState.VISIBLE);
        conf.setTitle("My Library");
        return conf;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (artistsList.isEmpty()) {
            getLoaderManager().restartLoader(0, new Bundle(), this);
        }
    }

    @Override
    public Loader<ApiResponse<LibraryArtistsList>> onCreateLoader(int i, Bundle args) {
        int page = args.getInt(ApiParamNames.API_PAGE, 1);
        if (page == 1) // we have first page in cache
            return new LibraryLoader(getActivity(), ApiResponse.Type.CACHE, username);
        return new LibraryLoader(getActivity(), ApiResponse.Type.API, username, page);
    }

    @Override
    public void onLoadFinished(Loader<ApiResponse<LibraryArtistsList>> apiResponseLoader, ApiResponse<LibraryArtistsList> data) {

        if (isPaused && !scrollListener.isLoading) { // to prevent unnecessary onLoadFinished triggering after onPause
            isPaused = false;
            return;
        }
        if (data.getError().isEmpty()) {
            if (data.getData().isEmpty()) {
//                showMsg("No tracks yet");
                return;
            }
            artistsList.addAll(data.getData());
            adapter.notifyDataSetChanged();
        } else {
//            showRetry();
//            showMsg(data.getError());
            Log.e("ERRROR!!!", data.getError());
        }

    }

    @Override
    public void onLoaderReset(Loader<ApiResponse<LibraryArtistsList>> apiResponseLoader) {

    }
}
