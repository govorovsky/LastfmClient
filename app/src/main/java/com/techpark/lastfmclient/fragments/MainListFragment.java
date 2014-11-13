package com.techpark.lastfmclient.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.techpark.lastfmclient.R;
import com.techpark.lastfmclient.adapters.MusicAdapter;
import com.techpark.lastfmclient.adapters.RecommendedArtistList;
import com.techpark.lastfmclient.api.ApiQuery;
import com.techpark.lastfmclient.api.user.UserGetRecommendedArtists;
import com.techpark.lastfmclient.api.user.UserHelpers;
import com.techpark.lastfmclient.tasks.ApiQueryTask;

import org.json.JSONException;


public class MainListFragment extends Fragment implements LoaderManager.LoaderCallbacks<String> {
    private GridView mRecommended;
    private RecommendedArtistList artistList = new RecommendedArtistList();
    private MusicAdapter mAdapter = null;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_list_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecommended = (GridView) view.findViewById(R.id.grid_recommended);
        mAdapter = new MusicAdapter(getActivity());
        mRecommended.setAdapter(mAdapter);
        mRecommended.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

        ApiQuery query = new UserGetRecommendedArtists(sk);
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
