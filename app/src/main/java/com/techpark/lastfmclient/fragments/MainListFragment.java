package com.techpark.lastfmclient.fragments;

import android.app.Activity;
import android.database.Cursor;
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
import android.widget.Button;
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
    private RecommendedArtistList mArtistList = null;

    private ServiceHelper mServiceHelper;
    private Activity mActivity;

    private RelativeLayout recommendedLayout;
    private RelativeLayout releasesLayout;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mActivity = activity;
    }

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

        releasesLayout = (RelativeLayout) view.findViewById(R.id.releases);
        ((TextView)releasesLayout.findViewById(R.id.label)).setText("New Releases");

        mArtistList = new RecommendedArtistList();
        GridView grid = (GridView) recommendedLayout.findViewById(R.id.grid);
        grid.setAdapter(new RecommendedAdapter(getActivity(), mArtistList));

        mServiceHelper = new ServiceHelper(getActivity());
        mServiceHelper.getRecommendedArtists();

        Button more_recommended = (Button) recommendedLayout.findViewById(R.id.button_more);
        more_recommended.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, new RecommendedMoreFragment())
                        .commit();
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
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(getActivity(),
                RecommendedArtistsTable.CONTENT_URI,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
       if (cursor == null) {
            Toast.makeText(getActivity(), "Network is broken...", Toast.LENGTH_LONG).show();
            Log.e("onLoadFinished", "Network error");
            return;
        }

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

       RecommendedArtistList list = UserHelpers.getRecommendedArtistsFromCursor(cursor, 4);
       mArtistList.getArtists().clear();
       mArtistList.getArtists().addAll(list.getArtists());
       adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> stringLoader) {
        /* void */
    }
}
