package com.techpark.lastfmclient.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.text.Layout;
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
import com.techpark.lastfmclient.adapters.MusicAdapter;
import com.techpark.lastfmclient.adapters.RecommendedArtistList;
import com.techpark.lastfmclient.api.ApiQuery;
import com.techpark.lastfmclient.api.music.GetRecommended;
import com.techpark.lastfmclient.api.user.UserHelpers;
import com.techpark.lastfmclient.tasks.ApiQueryTask;

import org.json.JSONException;


public class MainListFragment extends Fragment implements LoaderManager.LoaderCallbacks<String> {
    private RecommendedArtistList artistList = new RecommendedArtistList();
    private MusicAdapter mAdapter = null;

    private RelativeLayout recommendedLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_list_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recommendedLayout = (RelativeLayout) view.findViewById(R.id.recommended);
        ((TextView)recommendedLayout.findViewById(R.id.label)).setText("Recommended Music");

        mAdapter = new MusicAdapter(getActivity());
        GridView grid = (GridView) recommendedLayout.findViewById(R.id.grid);

        grid.setAdapter(mAdapter);
        //TODO: url to page with artist
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getActivity(), "" + i, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // to prevent loader callbacks called twice, we need to call initLoader after onStart()
        // see http://stackoverflow.com/questions/11293441/android-loadercallbacks-onloadfinished-called-twice
        getLoaderManager().initLoader(1, null, this);
    }

    @Override
    public Loader<String> onCreateLoader(int i, Bundle bundle) {
        String sk = UserHelpers.getUserSession(getActivity());
        if (sk == null)
            return null;

        ApiQuery query = new GetRecommended(sk);
        query.prepare();
        return new ApiQueryTask(getActivity(), query);
    }

    @Override
    public void onLoadFinished(Loader<String> stringLoader, String data) {
        if (data != null && artistList.getArtists().isEmpty()) { //need to check if we already get artists
            try {
                artistList = UserHelpers.getRecommendedArtistsFromJSON(data);
                mAdapter.setArtists(artistList);
                mAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                Toast.makeText(getActivity(), "Exception while parsing music", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<String> stringLoader) {
        Log.d("Fragment", "Loader reset");
    }
}
