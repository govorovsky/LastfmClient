package com.techpark.lastfmclient.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
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
public class RecentTracksMoreFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<ApiResponse<RecentTracksList>>, AbsListView.OnScrollListener {

    private static final String BUNDLE_USERNAME = "username";

    private RecentTracksAdapter adapter;
    private RecentTracksList list = new RecentTracksList();


    private View mProgress;
    private ProgressBar mProgressBar;
    private TextView mStatus;
    private ListView mListView;
    private Button mRetryButton;

    private String username;
    private boolean isPaused = false;


    private int currentPage = 2;
    private int prevItemCount = 4;
    private boolean isLoading = false;

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
            currentPage = savedInstanceState.getInt("page");
            prevItemCount = savedInstanceState.getInt("prev");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("list", list);
        outState.putInt("page", currentPage);
        outState.putInt("prev", prevItemCount);
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

        mListView = (ListView) view.findViewById(R.id.tracks_list);
        adapter = new RecentTracksAdapter(getActivity(), R.layout.recenttrack_item, list);
        mListView.setAdapter(adapter);
        mListView.setOnScrollListener(this);
        mProgress = LayoutInflater.from(getActivity()).inflate(R.layout.progress_footer, null);
        mProgressBar = (ProgressBar) mProgress.findViewById(R.id.progress_bar);
        if (isLoading) mProgressBar.setVisibility(View.VISIBLE);
        mStatus = (TextView) mProgress.findViewById(R.id.status);
        mListView.addFooterView(mProgress);
        mRetryButton = (Button) mProgress.findViewById(R.id.retry);
        mRetryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mStatus.setVisibility(View.INVISIBLE);
                mRetryButton.setVisibility(View.GONE);
                loadMore();
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
        if (isPaused && !isLoading) { // to prevent unnecessary onLoadFinished triggering after onPause
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

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    private void loadMore() {
        Bundle args = new Bundle();
        args.putInt(ApiParamNames.API_PAGE, currentPage);
        getLoaderManager().restartLoader(0, args, this);
//        Log.e("LOADING PAGE", currentPage + " ");
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//        Log.e("LOADING=", " " + isLoading);

        if (totalItemCount < prevItemCount)
            return;

        if (isLoading && (totalItemCount > prevItemCount)) {
            isLoading = false;
            prevItemCount = totalItemCount;
            currentPage++;
        }

        if (!isLoading && (firstVisibleItem + visibleItemCount == totalItemCount)) {
            isLoading = true;
//            Log.e("FIRST VISIBLE INDEX", firstVisibleItem + " ");
//            Log.e("VISIBLE CNT", visibleItemCount + " ");
//            Log.e("TOTAL", totalItemCount + " ");
//            Log.e("prev item", prevItemCount + " ");
            loadMore();
        }
    }
}
