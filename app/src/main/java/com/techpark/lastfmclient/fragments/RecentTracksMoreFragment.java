package com.techpark.lastfmclient.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.techpark.lastfmclient.R;
import com.techpark.lastfmclient.adapters.RecentTracksAdapter;
import com.techpark.lastfmclient.adapters.RecentTracksList;
import com.techpark.lastfmclient.api.ApiParamNames;
import com.techpark.lastfmclient.api.ApiResponse;
import com.techpark.lastfmclient.api.track.RecentTrack;

import java.util.Collection;

/**
 * Created by Andrew Govorovsky on 08.12.14.
 */
public class RecentTracksMoreFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<ApiResponse<RecentTracksList>> {

    private static final String BUNDLE_USERNAME = "username";

    public static final String TAG = RecentTracksMoreFragment.class.getSimpleName();

    private RecentTracksAdapter adapter;
    private RecentTracksList list = new RecentTracksList();

    private int itemCount = 15 + 1; // plus footer view!

    private View mProgress;
    private ProgressBar mProgressBar;
    private TextView mStatus;
    private ListView mListView;
    private Button mRetryButton;

    private String username;
    private boolean isPaused = false;


    private MoreFragmentScrollListener scrollListener = new MoreFragmentScrollListener(itemCount) {
        @Override
        void loadMore() {
            Bundle args = new Bundle();
            args.putInt(ApiParamNames.API_PAGE, currentPage);
            getLoaderManager().restartLoader(0, args, RecentTracksMoreFragment.this);
//            Log.e("LOADING PAGE", currentPage + " ");
            mProgressBar.setVisibility(View.VISIBLE);
        }
    };

    public static RecentTracksMoreFragment getInstance(String username) {
        RecentTracksMoreFragment fragment = new RecentTracksMoreFragment();
        Bundle args = new Bundle();
        args.putString(BUNDLE_USERNAME, username);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        username = getArguments().getString(BUNDLE_USERNAME);
        if (savedInstanceState != null) {
            list.addAll((Collection<RecentTrack>) savedInstanceState.getSerializable("list"));
            scrollListener.currentPage = savedInstanceState.getInt("page");
            scrollListener.prevItemCount = savedInstanceState.getInt("prev");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("list", list);
        outState.putInt("page", scrollListener.currentPage);
        outState.putInt("prev", scrollListener.prevItemCount);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected FragmentConf getFragmentConf() {
        FragmentConf conf = new FragmentConf();
        conf.setLayout(R.layout.more_tracks_fragment);
        conf.setLogo(R.drawable.slogo_with_padding);
        conf.setActionBarFade(FragmentConf.ActionBarState.VISIBLE);
        conf.setTitle("Recent Tracks");
        return conf;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mProgress = LayoutInflater.from(getActivity()).inflate(R.layout.progress_footer, null);
        mListView = (ListView) view.findViewById(R.id.tracks_list);
        adapter = new RecentTracksAdapter(getActivity(), R.layout.recenttrack_item, list);
        mListView.addFooterView(mProgress, null, false);
        mListView.setAdapter(adapter);
        mListView.setOnScrollListener(scrollListener);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.e("CLICLEDD", "   ");
                RecentTrack t = adapter.getItem(position);
                fragmentDispatcher.setFragment(TrackFragment.getInstance(t.getArtist(), t.getName(), username), "track", true);
            }
        });
        mProgressBar = (ProgressBar) mProgress.findViewById(R.id.progress_bar);
        if (scrollListener.isLoading) mProgressBar.setVisibility(View.VISIBLE);
        mStatus = (TextView) mProgress.findViewById(R.id.status);
        mRetryButton = (Button) mProgress.findViewById(R.id.retry);
        mRetryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStatus.setVisibility(View.INVISIBLE);
                mRetryButton.setVisibility(View.GONE);
                scrollListener.loadMore();
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        if (list.isEmpty()) {
            getLoaderManager().restartLoader(0, new Bundle(), this);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public Loader<ApiResponse<RecentTracksList>> onCreateLoader(int id, Bundle args) {
        mStatus.setVisibility(View.GONE);
        int page = args.getInt(ApiParamNames.API_PAGE, 1);
        if (page == 1) // we have first page in cache
            return new RecentTracksLoader(getActivity(), ApiResponse.Type.CACHE, username);
        return new RecentTracksLoader(getActivity(), ApiResponse.Type.API, username, page);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isPaused = true;
    }

    @Override
    public void onLoadFinished(Loader<ApiResponse<RecentTracksList>> loader, ApiResponse<RecentTracksList> data) {
//        Log.e("TRIGGERED!", "!!!");
        if (isPaused && !scrollListener.isLoading) { // to prevent unnecessary onLoadFinished triggering after onPause
            isPaused = false;
            return;
        }
        mProgressBar.setVisibility(View.GONE);
        if (data.getError().isEmpty()) {
            if (data.getData().isEmpty()) {
                showMsg("No tracks yet");
                return;
            }
            list.addAll(data.getData());
            adapter.notifyDataSetChanged();
        } else {
            showRetry();
            showMsg(data.getError());
            Log.e("ERRROR!!!", data.getError());
        }
    }

    private void showRetry() {
        mRetryButton.setVisibility(View.VISIBLE);
    }

    private void showMsg(String msg) {
        mStatus.setText(msg);
        mStatus.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<ApiResponse<RecentTracksList>> loader) {

    }

}
