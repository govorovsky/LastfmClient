package com.techpark.lastfmclient.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.techpark.lastfmclient.R;
import com.techpark.lastfmclient.api.library.LibArtist;

import java.util.List;

/**
 * Created by Andrew Govorovsky on 10.12.14.
 */
public class LibraryArtistsAdapter extends ArrayAdapter<LibArtist> {

    private Context mContext;
    private LayoutInflater mLayoutInflater;

    public LibraryArtistsAdapter(Context context, int resource, List<LibArtist> objects) {
        super(context, resource, objects);
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ArtistHolder artistHolder = null;
        LibArtist libArtist = getItem(position);

        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.lib_artist, parent, false);

            TextView name = (TextView) convertView.findViewById(R.id.artist_name);
            TextView plays = (TextView) convertView.findViewById(R.id.play_cnt);
            ImageView cover = (ImageView) convertView.findViewById(R.id.artist_pic);

            artistHolder = new ArtistHolder(plays, name, cover);

            convertView.setTag(artistHolder);

        }

        if (artistHolder == null) {
            artistHolder = (ArtistHolder) convertView.getTag();
        }

        artistHolder.playcnt.setText(libArtist.getPlaycnt() + " plays");
        artistHolder.name.setText(libArtist.getName());

        if (libArtist.getImg().isEmpty()) {
            artistHolder.cover.setImageResource(R.drawable.kar);
        } else {
            Picasso.with(mContext).load(libArtist.getImg()).into(artistHolder.cover);
        }
        return convertView;
    }

    private class ArtistHolder {
        private ArtistHolder(TextView playcnt, TextView name, ImageView cover) {
            this.playcnt = playcnt;
            this.name = name;
            this.cover = cover;
        }

        TextView playcnt;
        TextView name;
        ImageView cover;
    }
}
