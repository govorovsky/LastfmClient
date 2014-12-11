package com.techpark.lastfmclient.fragments;

import android.support.v4.app.LoaderManager;
import android.content.Context;
import android.support.v4.content.Loader;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.techpark.lastfmclient.R;
import com.techpark.lastfmclient.adapters.ReleasesAdapter;
import com.techpark.lastfmclient.adapters.ReleasesList;
import com.techpark.lastfmclient.api.ApiParamNames;
import com.techpark.lastfmclient.providers.ReleaseProvider;

/**
 * Created by max on 11/12/14.
 */
public class NewReleasesMoreFragment extends BaseFragment
        implements LoaderManager.LoaderCallbacks<ReleasesList>, AbsListView.OnScrollListener {

    private ReleasesList mReleasesList = null;
    private GridView mReleasesGrid = null;

    public static final String TITLE = "New Releases";
    public static final String TAG = NewReleasesMoreFragment.class.getSimpleName();

    @Override
    protected FragmentConf getFragmentConf() {
        FragmentConf conf = new FragmentConf();
        conf.setActionBarFade(FragmentConf.ActionBarState.VISIBLE);
        conf.setLogo(R.drawable.slogo_with_padding);
        conf.setTitle(TITLE);
        conf.setLayout(R.layout.more_fragment);
        return conf;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mReleasesList = new ReleasesList();

        mReleasesGrid = (GridView) view.findViewById(R.id.grid);
        mReleasesGrid.setAdapter(new ReleasesAdapter(getActivity(), mReleasesList));
        mReleasesGrid.setOnScrollListener(this);

        ((TextView) view.findViewById(R.id.label))
            .setText("New Releases");

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
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mReleasesList.getReleases().isEmpty()) {
            getLoaderManager().restartLoader(0, null, this);
        }
    }

    @Override
    public Loader<ReleasesList> onCreateLoader(int i, Bundle bundle) {
        int page = 0;
        if (bundle != null)
            page = bundle.getInt(ApiParamNames.API_PAGE);

        return new ReleaseLoader(getActivity(), page);
    }

    @Override
    public void onLoadFinished(Loader<ReleasesList> recommendedArtistListLoader, ReleasesList releasesList) {
        if (releasesList == null) {
            Log.d("releaseList is", "NULL");
            return;
        }

        ReleasesAdapter adapter = (ReleasesAdapter) mReleasesGrid.getAdapter();
        mReleasesList.getReleases().addAll(releasesList.getReleases());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<ReleasesList> recommendedArtistListLoader) {
        /* void */
    }

    private void loadMore() {
        Bundle bundle = new Bundle();
        bundle.putInt(ApiParamNames.API_PAGE, this.currentPage);
        if (currentPage > 0)
            return;
        getLoaderManager().restartLoader(0, bundle, this);
    }

    private int currentPage = 0;
    private int prevItemCount = 10;
    private boolean isLoading = false;

    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {
        /* void */
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