package com.techpark.lastfmclient.fragments;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.techpark.lastfmclient.R;
import com.techpark.lastfmclient.adapters.RecommendedAdapter;
import com.techpark.lastfmclient.adapters.RecommendedArtistList;
import com.techpark.lastfmclient.api.ApiParamNames;

/**
 * Created by max on 22/11/14.
 */
public class RecommendedMoreFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<RecommendedArtistList>,
                    AbsListView.OnScrollListener
{
    private RecommendedArtistList mArtistList = null;
    private GridView mRecommendedGrid = null;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.music_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mArtistList = new RecommendedArtistList();

        mRecommendedGrid = (GridView) view.findViewById(R.id.grid);
        mRecommendedGrid.setAdapter(new RecommendedAdapter(getActivity(), mArtistList));
        mRecommendedGrid.setOnScrollListener(this);

        ((TextView) view.findViewById(R.id.label))
                .setText("Recommended Artists");

        Button backButton = (Button) view.findViewById(R.id.button_more);
        backButton.setText("BACK");

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity()
                        .getSupportFragmentManager()
                        .popBackStack();
            }
        });

        getActivity().getLoaderManager().initLoader(2, null, this);
    }

    @Override
    public Loader<RecommendedArtistList> onCreateLoader(int i, Bundle bundle) {
        int page = 1;
        if (bundle != null)
            page = bundle.getInt(ApiParamNames.API_PAGE);

        MusicListLoader loader = new MusicListLoader(getActivity(), page);
        loader.forceLoad();
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<RecommendedArtistList> recommendedArtistListLoader, RecommendedArtistList recommendedArtistList) {
        if (recommendedArtistList == null) {
            Log.d("recommendedArtist is", "NULL");
            return;
        }

        RecommendedAdapter adapter = (RecommendedAdapter) mRecommendedGrid.getAdapter();
        mArtistList.getArtists().addAll(recommendedArtistList.getArtists());

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<RecommendedArtistList> recommendedArtistListLoader) {
        /* void */
    }

    private void loadMore() {
        Bundle bundle = new Bundle();
        bundle.putInt(ApiParamNames.API_PAGE, this.currentPage);
        getActivity().getLoaderManager().restartLoader(2, bundle, this);
    }

    private int currentPage = 2;
    private int prevItemCount = 10;
    private boolean isLoading = false;

    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {
        Log.d("onScrollStateChanged", "" + scrollState);
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (totalItemCount < prevItemCount)
            return;

        if (isLoading && (totalItemCount > prevItemCount)) {
            isLoading = false;
            prevItemCount = totalItemCount;
            currentPage++;
        }

        if (!isLoading && (firstVisibleItem + visibleItemCount == totalItemCount)) {
            isLoading = true;
            loadMore();
        }
    }
}
