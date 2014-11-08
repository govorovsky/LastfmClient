package com.techpark.lastfmclient.fragments;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.techpark.lastfmclient.R;
import com.techpark.lastfmclient.adapters.ArtistList;
import com.techpark.lastfmclient.adapters.MusicAdapter;
import com.techpark.lastfmclient.api.ApiQuery;
import com.techpark.lastfmclient.api.music.GetRecommended;
import com.techpark.lastfmclient.api.user.UserHelpers;
import com.techpark.lastfmclient.tasks.ApiQueryTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainListFragment extends Fragment implements LoaderManager.LoaderCallbacks<String> {
    private GridView mRecommended;
    private ArtistList artistList = new ArtistList();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_list_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecommended = (GridView) view.findViewById(R.id.grid_recommended);
        mRecommended.setAdapter(new MusicAdapter(getActivity()));
        mRecommended.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getActivity(), "" + i, Toast.LENGTH_SHORT).show();
            }
        });

        Loader loader = getActivity().getLoaderManager().getLoader(1);
        if (loader != null) {
            //show progress bar
            getActivity().getLoaderManager().initLoader(1, null, this);
        } else {
            getActivity().getLoaderManager().restartLoader(1, null, this);
        }
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
        //progressbar
        if (data != null) {
            try {
                JSONObject object = new JSONObject(data);
                if (!object.isNull("error")) {
                    Toast.makeText(getActivity(), "Error while loading music", Toast.LENGTH_LONG).show();
                } else {
                    JSONObject recommendations = object.getJSONObject("recommendations");
                    JSONArray artists = recommendations.getJSONArray("artist");

                    for (int i = 0; i < artists.length(); ++i) {
                        JSONArray images = ((JSONObject) artists.get(i)).getJSONArray("image");
                        artistList.addArtist(new ArtistList.Artist(
                                ((JSONObject) artists.get(i)).getString("name"),
                                ((JSONObject) artists.get(i)).getString("url"),
                                images.getJSONObject(4).getString("#text")
                        ));
                    }
                }
            } catch (JSONException e) {
                Toast.makeText(getActivity(), "Exception while parsing music", Toast.LENGTH_LONG).show();
            }
        }
        getLoaderManager().destroyLoader(1);

       // stop progressbar
        ((MusicAdapter) mRecommended.getAdapter()).setArtists(artistList);
        ((MusicAdapter) mRecommended.getAdapter()).notifyDataSetChanged();
        mRecommended.setAdapter(mRecommended.getAdapter());
    }

    @Override
    public void onLoaderReset(Loader<String> stringLoader) {
        Toast.makeText(getActivity(), "Loader reset", Toast.LENGTH_LONG).show();
    }
}
