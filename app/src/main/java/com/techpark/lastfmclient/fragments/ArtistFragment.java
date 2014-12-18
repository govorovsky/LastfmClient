package com.techpark.lastfmclient.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.techpark.lastfmclient.R;
import com.techpark.lastfmclient.adapters.SimilarArtistAdapter;
import com.techpark.lastfmclient.api.artist.Artist;
import com.techpark.lastfmclient.api.artist.ArtistHelpers;
import com.techpark.lastfmclient.db.ArtistsTable;
import com.techpark.lastfmclient.views.ExpandableTextView;
import com.techpark.lastfmclient.views.StretchedGridView;
import com.techpark.lastfmclient.views.TopCropImageView;

import java.util.ArrayList;

/**
 * Created by max on 17/12/14.
 */
public class ArtistFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<Cursor>
{
    public static final String TAG = ArtistFragment.class.getSimpleName();
    private static final String TITLE = "Artist Info";
    private static final String BUNDLE_ARTIST = "artist";

    private TextView mArtistName;
    private ExpandableTextView mArtistBio;
    private ProgressBar mProgressBar;
    private RelativeLayout mHeader;
    private RelativeLayout mArtistInfo;
    private RelativeLayout moreSimilars;
    private LinearLayout mTags;
    private TopCropImageView mCover;

    private StretchedGridView mSimilarGrid;
    private ArrayList<Artist> artistList = new ArrayList<>();
    private SimilarArtistAdapter adapter;

    private class LoadersNum {
        static final int ARTIST_LOADER = 0;

    }
    private Artist mArtist;

    private boolean isArtistLoaded = false;

    @Override
    protected FragmentConf getFragmentConf() {
        FragmentConf conf = new FragmentConf();
        conf.setActionBarFade(FragmentConf.ActionBarState.TRANSPARENT);
        conf.setLogo(R.drawable.slogo_with_padding);
        conf.setTitle(TITLE);
        conf.setLayout(R.layout.artist_fragment);
        return conf;
    }

    public static ArtistFragment getInstance(Artist artist) {
        Bundle arg = new Bundle();
        arg.putSerializable(BUNDLE_ARTIST, artist);
        ArtistFragment fragment = new ArtistFragment();
        fragment.setArguments(arg);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mArtist = (Artist) getArguments().getSerializable(BUNDLE_ARTIST);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        serviceHelper.getArtist(mArtist.getArtistName(), true);

        mHeader = (RelativeLayout) view.findViewById(R.id.header);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);

        if (!isArtistLoaded) {
            mProgressBar.setVisibility(View.VISIBLE);
            mHeader.setVisibility(View.GONE);
        } else {
            mHeader.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
        }

        mArtistName = (TextView) view.findViewById(R.id.artist_name);
        mTags = (LinearLayout) view.findViewById(R.id.tags);
        mCover = (TopCropImageView) view.findViewById(R.id.image_header);

        mArtistInfo = (RelativeLayout) view.findViewById(R.id.label_artist_bio);
        mArtistBio = (ExpandableTextView) view.findViewById(R.id.artist_biography);
        mArtistBio.setExpandHandler(mArtistInfo);
        mArtistBio.setStateTextView((TextView) view.findViewById(R.id.button_full_bio));

        adapter = new SimilarArtistAdapter(getActivity(), R.layout.similar_artist, artistList);
        mSimilarGrid = (StretchedGridView) view.findViewById(R.id.similar_artists_grid);
        mSimilarGrid.setAdapter(adapter);

        moreSimilars = (RelativeLayout) view.findViewById(R.id.label_artist_similar);
        moreSimilars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentDispatcher.setFragment(
                        SimilarArtistMoreFragment.getInstance(mArtist.getArtistName()),
                        SimilarArtistMoreFragment.TAG,
                        true
                );
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().initLoader(LoadersNum.ARTIST_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (id) {
            case LoadersNum.ARTIST_LOADER:
                Uri u = ArtistsTable.CONTENT_URI_ID_ARTIST.buildUpon().appendPath(mArtist.getArtistName()).build();
                return new CursorLoader(getActivity(), u, null, null, null, null);
            default:
                return null;
        }
    }

    private boolean fullInfo = false;
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.d(TAG, "Loader onFinished");
        switch (loader.getId()) {
            case LoadersNum.ARTIST_LOADER:
                if (!fullInfo) {
                    fullInfo = true;
                    return;
                }

                mArtist = ArtistHelpers.getArtistFromCursor(data);
                if (mArtist != null) {
                    isArtistLoaded = true;
                    artistInfoLoaded(mArtist);
                }
                break;
        }
    }

    private void artistInfoLoaded(final Artist artist) {
        mHeader.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);

        mArtistName.setText(artist.getArtistName());
        Picasso.with(getActivity()).load(
                artist.getImage(Artist.ImageSize.MEGA)
        ).into(mCover);

        ArrayList<String> tags = artist.getTags();
        LayoutInflater vi = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        for (String t : tags) {
            TextView v = (TextView) vi.inflate(R.layout.tag_item, mTags, false);
            v.setText(t);
            mTags.addView(v);
        }

        if (!artist.getBioSummary().isEmpty()) {
            mArtistBio.setText(Html.fromHtml(artist.getBioSummary()));
            mArtistBio.setMovementMethod(LinkMovementMethod.getInstance());
        } else {
            mArtistBio.setText("No info about this artist. You can help us resolve this upset fact!");
        }

        ArrayList<Artist> similars = ArtistHelpers.getSimilarArtists(artist, getActivity(), 4);
        artistList.addAll(similars);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        /* void */
    }
}
