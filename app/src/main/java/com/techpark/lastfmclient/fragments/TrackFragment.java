package com.techpark.lastfmclient.fragments;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.techpark.lastfmclient.R;
import com.techpark.lastfmclient.api.track.Track;
import com.techpark.lastfmclient.api.track.TrackHelpers;
import com.techpark.lastfmclient.db.TrackTable;
import com.techpark.lastfmclient.views.ExpandableTextView;
import com.techpark.lastfmclient.views.NotifyingScrollView;
import com.techpark.lastfmclient.views.TopCropImageView;

/**
 * Created by Andrew Govorovsky on 15.12.14.
 */
public class TrackFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<Cursor>, NotifyingScrollView.OnScrollChangedListener {

    private static final CharSequence TITLE = "Track Info";

    private LinearLayout mTags;

    private Track track;

    public static final String BUNDLE_ARTIST = "artist";
    public static final String BUNDLE_TRACK = "track";
    public static final String BUNDLE_USER = "user";

    private TextView mArtistName;
    private TextView mTrackName;
    private ExpandableTextView mWiki;
    private ProgressBar mProgressBar;
    private RelativeLayout mHeader;
    private RelativeLayout mLabelBio;

    private TopCropImageView mCover;

    private String artistname;
    private String trackname;
    private String username;

    private boolean isDataLoaded = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        artistname = args.getString(BUNDLE_ARTIST);
        trackname = args.getString(BUNDLE_TRACK);
        username = args.getString(BUNDLE_USER);
    }

    @Override
    protected FragmentConf getFragmentConf() {
        FragmentConf conf = new FragmentConf();
        conf.setActionBarFade(FragmentConf.ActionBarState.TRANSPARENT);
        conf.setLogo(R.drawable.slogo_with_padding);
        conf.setTitle(TITLE);
        conf.setLayout(R.layout.track_fragment);
        return conf;
    }

    public static TrackFragment getInstance(String artist, String track, String username) {
        Bundle arg = new Bundle();
        arg.putString(BUNDLE_ARTIST, artist);
        arg.putString(BUNDLE_TRACK, track);
        arg.putString(BUNDLE_USER, username);
        TrackFragment trackFragment = new TrackFragment();
        trackFragment.setArguments(arg);
        return trackFragment;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        serviceHelper.getTrack(trackname, artistname, username);


        mHeader = (RelativeLayout) view.findViewById(R.id.header);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress_bar);

        if (!isDataLoaded) {
            mProgressBar.setVisibility(View.VISIBLE);
            mHeader.setVisibility(View.GONE);
        } else {
            mHeader.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
        }

        NotifyingScrollView notifyingScrollView = (NotifyingScrollView) view.findViewById(R.id.scroll);
        notifyingScrollView.setListener(this);

        mTags = (LinearLayout) view.findViewById(R.id.tags);
        mArtistName = (TextView) view.findViewById(R.id.artist_name);
        mTrackName = (TextView) view.findViewById(R.id.track_name);
        mCover = (TopCropImageView) view.findViewById(R.id.image_header);
        mWiki = (ExpandableTextView) view.findViewById(R.id.wiki);
        mLabelBio = (RelativeLayout) view.findViewById(R.id.label_bio);
        mWiki.setExpandHandler(mLabelBio);
        mWiki.setStateTextView((TextView) view.findViewById(R.id.button_more_wiki));
    }

    @Override
    public void onResume() {
        super.onResume();
        getLoaderManager().initLoader(0, null, this);

    }

    private void setTags(String tag) {
        if (!tag.isEmpty()) {
            mTags.removeAllViews();
            LayoutInflater vi = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            String tags[] = tag.split(",");
            for (String t : tags) {
                TextView v = (TextView) vi.inflate(R.layout.tag_item, mTags, false);
                v.setText(t);
                mTags.addView(v);
            }
        }
    }

    private void updateViews(Track track) {
        mHeader.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);

        mArtistName.setText(track.getArtist());
        mTrackName.setText(track.getName());
        String cover = track.getAlbumImg().isEmpty() ? track.getArtistImg() : track.getAlbumImg();
        if (!track.getAlbumImg().isEmpty() || !track.getArtistImg().isEmpty()) {
            mCover.setScaleType(ImageView.ScaleType.MATRIX);
            Picasso.with(getActivity()).load(cover).into(mCover);
        } else {
            mCover.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Picasso.with(getActivity()).load(R.drawable.star).into(mCover);
        }
        setTags(track.getTags());

        if (!track.getContent().isEmpty()) { /* TODO Create expandable textview */
            mWiki.setText(Html.fromHtml(track.getContent()));
            mWiki.setMovementMethod(LinkMovementMethod.getInstance());
        } else {
            mWiki.setText("No info yet.");
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri u = TrackTable.CONTENT_URI_ID_TRACK.buildUpon().appendPath(artistname).appendPath(trackname).build();
        return new CursorLoader(getActivity(), u, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.getCount() != 0) {
            track = TrackHelpers.getTrackFromCursor(data);
            if (track != null) {
                isDataLoaded = true;
                updateViews(track);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onScrollChanged(ScrollView from, int l, int r, int oldl, int oldt) {
        changeActionBarFabe(r);
    }

//    static int height;
//
//
//
//    public static void expand(final View v) {
//        v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        v.getLayoutParams().height = height;
//        v.setVisibility(View.VISIBLE);
//        Animation a = new Animation() {
//            @Override
//            protected void applyTransformation(float interpolatedTime, Transformation t) {
//                v.getLayoutParams().height = interpolatedTime == 1
//                        ? ViewGroup.LayoutParams.WRAP_CONTENT
//                        : (int) ((targetHeight - height) * interpolatedTime + height);
//                Log.e("COMPUTED=", v.getLayoutParams().height + " ==");
//                v.requestLayout();
//            }
//
//            @Override
//            public boolean willChangeBounds() {
//                return true;
//            }
//        };
//
//        // 1dp/ms
//        a.setDuration((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density));
//        v.startAnimation(a);
//    }
//
//    public static void collapse(final View v) {
//        final int initialHeight = v.getMeasuredHeight();
//
//        Animation a = new Animation() {
//            @Override
//            protected void applyTransformation(float interpolatedTime, Transformation t) {
//                if (interpolatedTime == 1) {
////                    v.setVisibility(View.GONE);
//                } else {
//                    v.getLayoutParams().height = initialHeight - (int) ((initialHeight -height)* interpolatedTime);
//                    Log.e("Initial=", initialHeight+ " ==");
//                    Log.e("COMPUTED=", v.getLayoutParams().height + " ==");
//                    v.requestLayout();
//                }
//            }
//
//            @Override
//            public boolean willChangeBounds() {
//                return true;
//            }
//        };
//
//        // 1dp/ms
//        a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density));
//        v.startAnimation(a);
//    }

}
