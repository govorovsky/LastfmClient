package com.techpark.lastfmclient.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.techpark.lastfmclient.R;
import com.techpark.lastfmclient.adapters.EventsAdapter;
import com.techpark.lastfmclient.adapters.EventsList;
import com.techpark.lastfmclient.adapters.RecommendedAdapter;
import com.techpark.lastfmclient.adapters.RecommendedArtistList;
import com.techpark.lastfmclient.adapters.ReleasesAdapter;
import com.techpark.lastfmclient.adapters.ReleasesList;
import com.techpark.lastfmclient.api.artist.Artist;
import com.techpark.lastfmclient.api.event.EventHelpers;
import com.techpark.lastfmclient.api.release.ReleaseHelpers;
import com.techpark.lastfmclient.api.user.UserHelpers;
import com.techpark.lastfmclient.db.NewReleasesTable;
import com.techpark.lastfmclient.db.RecommendedArtistsTable;
import com.techpark.lastfmclient.db.UpcomingEventsTable;
import com.techpark.lastfmclient.services.ServiceHelper;
import com.techpark.lastfmclient.views.ExpandedGridView;


public class MainListFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private RecommendedArtistList mArtistList = null;
    private ReleasesList mReleasesList = null;
    private EventsList mUpcomingEventsList = null;

    private class LoadersNum {
        final static int RECOMMENDED = 0;
        final static int NEW_RELEASES = 1;
        final static int UPCOMING_EVENTS = 2;
    }

    //TODO: in styles
    public class DisplayParams {
        final static int GRID_NUM_ITEMS = 4;
        final static int GRID_EVENTS_COLUMNS = 1; //default 2
    }

    private RelativeLayout recommendedLayout;
    private RelativeLayout releasesLayout;
    private RelativeLayout eventsLayout;

    @Override
    protected FragmentConf getFragmentConf() {
        FragmentConf conf = new FragmentConf();
        conf.setActionBarFade(FragmentConf.ActionBarState.VISIBLE);
        conf.setLogo(R.drawable.logo_with_padding);
        conf.setTitle("");
        conf.setLayout(R.layout.main_list_fragment);
        return conf;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recommendedLayout = (RelativeLayout) view.findViewById(R.id.recommended);
        ((TextView) recommendedLayout.findViewById(R.id.label)).setText("Recommended Music");

        releasesLayout = (RelativeLayout) view.findViewById(R.id.releases);
        ((TextView) releasesLayout.findViewById(R.id.label)).setText("New Releases");

        eventsLayout = (RelativeLayout) view.findViewById(R.id.events);
        ((TextView)eventsLayout.findViewById(R.id.label)).setText("Upcoming Events");

        mArtistList = new RecommendedArtistList();
        ExpandedGridView grid_recommended = (ExpandedGridView) recommendedLayout.findViewById(R.id.grid);
        grid_recommended.setExpanded(true);
        final RecommendedAdapter recommendedAdapter = new RecommendedAdapter(getActivity(), mArtistList);
        grid_recommended.setAdapter(recommendedAdapter);
        grid_recommended.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Artist a = (Artist) recommendedAdapter.getItem(position);
                fragmentDispatcher.setFragment(ArtistFragment.getInstance(a), ArtistFragment.TAG, true);
            }
        });

        mReleasesList = new ReleasesList();
        ExpandedGridView grid_releases = (ExpandedGridView) releasesLayout.findViewById(R.id.grid);
        grid_releases.setExpanded(true);
        grid_releases.setAdapter(new ReleasesAdapter(getActivity(), mReleasesList));

        mUpcomingEventsList = new EventsList();
        ExpandedGridView grid_events = (ExpandedGridView) eventsLayout.findViewById(R.id.grid);
        grid_events.setExpanded(true);
        grid_events.setNumColumns(DisplayParams.GRID_EVENTS_COLUMNS);
        grid_events.setAdapter(new EventsAdapter(getActivity(), mUpcomingEventsList));

        ServiceHelper helper = new ServiceHelper(getActivity());
        helper.getRecommendedArtists();
        helper.getNewReleases();
        helper.getUpcomingEvents();

        /* TODO add caching here, because user navigates through fragments and  we don't need to
            download data every time we instantiate main fragment.
         */
        Button more_recommended = (Button) recommendedLayout.findViewById(R.id.button_more);
        more_recommended.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               fragmentDispatcher.setFragment(new RecommendedMoreFragment(), RecommendedMoreFragment.TAG, true);
            }
        });

        Button more_releases = (Button) releasesLayout.findViewById(R.id.button_more);
        more_releases.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentDispatcher.setFragment(new NewReleasesMoreFragment(), NewReleasesMoreFragment.TAG, true);
            }
        });

        Button more_events = (Button) eventsLayout.findViewById(R.id.button_more);
        more_events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentDispatcher.setFragment(new UpcomingEventsMoreFragment(), UpcomingEventsMoreFragment.TAG, true);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().initLoader(LoadersNum.RECOMMENDED, null, this);
        getLoaderManager().initLoader(LoadersNum.NEW_RELEASES, null, this);
        getLoaderManager().initLoader(LoadersNum.UPCOMING_EVENTS, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        switch(i) {
            case LoadersNum.RECOMMENDED:
                return new CursorLoader(getActivity(),
                        RecommendedArtistsTable.CONTENT_URI,
                        null, null, null, null
                );
            case LoadersNum.NEW_RELEASES:
                return new CursorLoader(getActivity(),
                        NewReleasesTable.CONTENT_URI,
                        null, null, null, null
                );
            case LoadersNum.UPCOMING_EVENTS:
                return new CursorLoader(getActivity(),
                        UpcomingEventsTable.CONTENT_URI,
                        null, null, null, null
                );
            default:
                return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        if (cursor == null) {
            Toast.makeText(getActivity(), "Network is broken...", Toast.LENGTH_LONG).show();
            Log.e("onLoadFinished", "Network error");
            return;
        }

        switch(cursorLoader.getId()) {
            case LoadersNum.RECOMMENDED:
                recommendedLoadFinished(cursor);
                break;
            case LoadersNum.NEW_RELEASES:
                newreleasesLoadFinished(cursor);
                break;
            case LoadersNum.UPCOMING_EVENTS:
                upcomingeventsLoadFinished(cursor);
            default:
                break;
        }
    }

    private void upcomingeventsLoadFinished(Cursor cursor) {
        Log.d("upcomingeventsLoadFinished", "" + cursor.getCount());
        if (cursor.getCount() == 0) {
            eventsLayout.findViewById(R.id.grid).setVisibility(View.GONE);
            TextView messageView = (TextView) eventsLayout.findViewById(R.id.db_message);
            messageView.setVisibility(View.VISIBLE);
            messageView.setText("No upcoming events");
            return;
        }

        eventsLayout.findViewById(R.id.db_message).setVisibility(View.GONE);
        eventsLayout.findViewById(R.id.grid).setVisibility(View.VISIBLE);

        EventsAdapter adapter = (EventsAdapter)
                ((GridView) eventsLayout.findViewById(R.id.grid)).getAdapter();

        EventsList list = EventHelpers.getUpcomingEventsFromCursor(cursor, DisplayParams.GRID_NUM_ITEMS);
        Log.d("upcomingEventsLoadFinished", "" + list.getEvents().size());
        mUpcomingEventsList.getEvents().clear();
        mUpcomingEventsList.getEvents().addAll(list.getEvents());
        adapter.notifyDataSetChanged();
    }

    private void newreleasesLoadFinished(Cursor cursor) {
        if (cursor.getCount() == 0) {
            releasesLayout.findViewById(R.id.grid).setVisibility(View.GONE);
            TextView messageView = (TextView) releasesLayout.findViewById(R.id.db_message);
            messageView.setVisibility(View.VISIBLE);
            messageView.setText("No new releases");
            return;
        }

        releasesLayout.findViewById(R.id.db_message).setVisibility(View.GONE);
        releasesLayout.findViewById(R.id.grid).setVisibility(View.VISIBLE);

        ReleasesAdapter adapter = (ReleasesAdapter)
                ((GridView) releasesLayout.findViewById(R.id.grid)).getAdapter();

        ReleasesList list = ReleaseHelpers.getNewReleasesFromCursor(cursor, DisplayParams.GRID_NUM_ITEMS);
        mReleasesList.getReleases().clear();
        mReleasesList.getReleases().addAll(list.getReleases());
        adapter.notifyDataSetChanged();
    }

    private void recommendedLoadFinished(Cursor cursor) {
        if (cursor.getCount() == 0) {
            recommendedLayout.findViewById(R.id.grid).setVisibility(View.GONE);
            TextView messageView = (TextView) recommendedLayout.findViewById(R.id.db_message);
            messageView.setVisibility(View.VISIBLE);
            messageView.setText("No recommendations");
            Log.e("onLoadFinished", "Empty cursor");
            return;
        }

        recommendedLayout.findViewById(R.id.db_message).setVisibility(View.GONE);
        recommendedLayout.findViewById(R.id.grid).setVisibility(View.VISIBLE);

        RecommendedAdapter adapter = (RecommendedAdapter)
                ((GridView) recommendedLayout.findViewById(R.id.grid)).getAdapter();

        RecommendedArtistList list = UserHelpers.getRecommendedArtistsFromCursor(cursor, DisplayParams.GRID_NUM_ITEMS);
        mArtistList.getArtists().clear();
        mArtistList.getArtists().addAll(list.getArtists());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> stringLoader) {
        /* void */
    }
}
