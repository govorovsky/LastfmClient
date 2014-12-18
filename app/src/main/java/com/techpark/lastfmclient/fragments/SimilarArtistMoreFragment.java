package com.techpark.lastfmclient.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.GridView;

import com.techpark.lastfmclient.R;
import com.techpark.lastfmclient.adapters.SimilarArtistAdapter;
import com.techpark.lastfmclient.api.ApiParamNames;
import com.techpark.lastfmclient.api.artist.Artist;

import java.util.ArrayList;

/**
 * Created by max on 18/12/14.
 */
public class SimilarArtistMoreFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<ArrayList<Artist>> {
    public static String BUNDLE_ATRIST = "artist";
    public static final String TAG = ArtistFragment.class.getSimpleName();

    private String artistName;

    private int itemCount = 4;

    private ArrayList<Artist> artistsList = new ArrayList<>();
    private SimilarArtistAdapter adapter;

    private MoreFragmentScrollListener scrollListener = new MoreFragmentScrollListener(itemCount) {
        @Override
        void loadMore() {
            Bundle args = new Bundle();
            args.putInt(ApiParamNames.API_PAGE, currentPage);
            getLoaderManager().restartLoader(0, args, SimilarArtistMoreFragment.this);
        }
    };
    private boolean isPaused = false;

    public static LibraryMoreFragment getInstance(String artistName) {
        LibraryMoreFragment fragment = new LibraryMoreFragment();
        Bundle args = new Bundle();
        args.putString(BUNDLE_ATRIST, artistName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        artistName = getArguments().getString(BUNDLE_ATRIST);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        GridView gridView = (GridView) view.findViewById(R.id.similar_list);
        adapter = new SimilarArtistAdapter(getActivity(), R.layout.similar_artist, artistsList);
        gridView.setAdapter(adapter);
        gridView.setOnScrollListener(scrollListener);
    }

    @Override
    protected FragmentConf getFragmentConf() {
        FragmentConf conf = new FragmentConf();
        conf.setLayout(R.layout.more_library_fragment);
        conf.setLogo(R.drawable.slogo_with_padding);
        conf.setActionBarFade(FragmentConf.ActionBarState.VISIBLE);
        conf.setTitle("Similar Artists for " + artistName);
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
    public Loader<ArrayList<Artist>> onCreateLoader(int i, Bundle args) {
        int page = args.getInt(ApiParamNames.API_PAGE, 1);
        if (page == 1) // we have first page in cache
            return new SimilarArtistLoder(getActivity(), artistName);
        return null;
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Artist>> artistLoder, ArrayList<Artist> data) {

        if (isPaused && !scrollListener.isLoading) { // to prevent unnecessary onLoadFinished triggering after onPause
            isPaused = false;
            return;
        }

        artistsList.addAll(data);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Artist>> apiResponseLoader) {
        /* void */
    }
}

