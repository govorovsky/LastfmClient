package com.techpark.lastfmclient.fragments;

import android.app.Service;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.techpark.lastfmclient.R;
import com.techpark.lastfmclient.adapters.RecommendedAdapter;
import com.techpark.lastfmclient.adapters.RecommendedArtistList;
import com.techpark.lastfmclient.api.user.UserHelpers;
import com.techpark.lastfmclient.db.RecommendedArtistsTable;
import com.techpark.lastfmclient.services.ServiceHelper;


public class MainListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private RecommendedArtistList artistList = new RecommendedArtistList();

    private ServiceHelper mServiceHelper;

    private RelativeLayout recommendedLayout;
    private RelativeLayout releasesLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_list_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Log.d("MainListFragment", "onViewCreated, begin");
        super.onViewCreated(view, savedInstanceState);

        recommendedLayout = (RelativeLayout) view.findViewById(R.id.recommended);
        ((TextView)recommendedLayout.findViewById(R.id.label)).setText("Recommended Music");

        releasesLayout = (RelativeLayout) view.findViewById(R.id.releases);
        ((TextView)releasesLayout.findViewById(R.id.label)).setText("New Releases");

        GridView grid = (GridView) recommendedLayout.findViewById(R.id.grid);
        //TODO: need here?
        grid.setAdapter(new RecommendedAdapter(getActivity()));

        Log.d("MainListFragment", "onViewCreated, before init ServiceHelper");
        mServiceHelper = new ServiceHelper(getActivity());
        Log.d("MainListFragment", "onViewCreated, before getRecommendedArtists()");
        mServiceHelper.getRecommendedArtists();
        Log.d("MainListFragment", "onViewCreated, end");
    }

    @Override
    public void onResume() {
        super.onResume();
        // to prevent loader callbacks called twice, we need to call initLoader after onStart()
        // see http://stackoverflow.com/questions/11293441/android-loadercallbacks-onloadfinished-called-twice
        getLoaderManager().initLoader(1, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(getActivity(),
                Uri.withAppendedPath(RecommendedArtistsTable.CONTENT_URI_ID_RECOMMENDED,
                        UserHelpers.getUserSessionPrefs(getActivity()).getString(UserHelpers.PREF_NAME, "")),
                        null,
                        null,
                        null,
                        null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        if (cursor == null) {
            Toast.makeText(getActivity(), "Network is broken...", Toast.LENGTH_LONG).show();
            return;
        }

        RecommendedAdapter adapter = (RecommendedAdapter)
                ((GridView) recommendedLayout.findViewById(R.id.grid)).getAdapter();

        artistList = UserHelpers.getRecommendedArtistsFromCursor(cursor, 4);
        adapter.setArtists(artistList);

        ((GridView) recommendedLayout.findViewById(R.id.grid))
            .setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    //TODO: make different for all images (similar and main)
                    Toast.makeText(getActivity(), "" + i, Toast.LENGTH_SHORT).show();
                }
            });

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> stringLoader) {
        Log.d("Fragment", "Loader reset");
    }
}
