package com.techpark.lastfmclient.fragments;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.techpark.lastfmclient.R;
import com.techpark.lastfmclient.adapters.EventsAdapter;
import com.techpark.lastfmclient.adapters.EventsList;
import com.techpark.lastfmclient.api.ApiParamNames;

/**
 * Created by max on 17/12/14.
 */
public class UpcomingEventsMoreFragment extends BaseFragment
    implements LoaderManager.LoaderCallbacks<EventsList>, AbsListView.OnScrollListener
{
    private EventsList mEventsList = null;
    private GridView mEventsGrid = null;

    public static final String TITLE = "Upcoming Events";
    public static final String TAG = UpcomingEventsMoreFragment.class.getSimpleName();

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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mEventsList = new EventsList();

        mEventsGrid = (GridView) view.findViewById(R.id.grid);
        mEventsGrid.setNumColumns(MainListFragment.DisplayParams.GRID_EVENTS_COLUMNS);
        mEventsGrid.setAdapter(new EventsAdapter(getActivity(), mEventsList));
        mEventsGrid.setOnScrollListener(this);

        ((TextView) view.findViewById(R.id.label)).setText(TITLE);

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
        if (mEventsList.getEvents().isEmpty()) {
            getLoaderManager().restartLoader(0, null, this);
        }
    }


    @Override
    public Loader<EventsList> onCreateLoader(int id, Bundle bundle) {
        int page = 1;
        if (bundle != null)
            page = bundle.getInt(ApiParamNames.API_PAGE);

        return new EventsListLoader(getActivity(), page);
    }

    @Override
    public void onLoadFinished(Loader<EventsList> loader, EventsList data) {
        if (data == null) {
            return;
        }

        EventsAdapter adapter = (EventsAdapter) mEventsGrid.getAdapter();
        mEventsList.getEvents().addAll(data.getEvents());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<EventsList> loader) {
        /* void */
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        /* void */
    }

    private int currentPage = 2;
    private int prevItemCount = 10;
    private boolean isLoading = false;

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

    private void loadMore() {
        Bundle bundle = new Bundle();
        bundle.putInt(ApiParamNames.API_PAGE, this.currentPage);
        getLoaderManager().restartLoader(0, bundle, this);
    }

}