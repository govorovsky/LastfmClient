package com.techpark.lastfmclient.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.techpark.lastfmclient.R;
import com.techpark.lastfmclient.api.artist.Artist;
import com.techpark.lastfmclient.api.release.Release;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ReleasesAdapter extends BaseAdapter {
    private Context mContext;
    private ReleasesList mReleasesList;
    private LayoutInflater layoutInflater;

    public ReleasesAdapter(Context c, ReleasesList list) {
        this.layoutInflater = LayoutInflater.from(c);
        this.mContext = c;
        this.mReleasesList = list;
    }

    public void setReleases(ReleasesList releases) {
        this.mReleasesList = releases;
    }

    @Override
    public int getCount() {
        if (mReleasesList == null)
            return 0;
        return mReleasesList.getReleases().size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        ReleaseHolder holder = null;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.release_item, parent, false);
            holder = new ReleaseHolder();
            holder.image = (ImageView) convertView.findViewById(R.id.release_icon);
            holder.artist = (TextView) convertView.findViewById(R.id.release_band);
            holder.release_name = (TextView) convertView.findViewById(R.id.release_name);
            holder.artist_image = (CircleImageView) convertView.findViewById(R.id.release_band_icon);
            convertView.setTag(holder);
        }

        if (holder == null) {
            holder = (ReleaseHolder) convertView.getTag();
        }

        ReleasesList.ReleaseWrapper r = mReleasesList.getReleases().get(pos);

        holder.artist.setText(r.getArtist().getArtistName());
        String[] date = r.getDate().split(" ");
        holder.release_name.setText(r.getReleaseName() + date[0]);

        Picasso.with(mContext).load(
                r.getImage(Release.ImageSize.EXTRALARGE)
        ).into(holder.image);

        Picasso.with(mContext).load(
                r.getArtist().getImage(Artist.ImageSize.LARGE)
        ).into(holder.artist_image);

        return convertView;
    }

    private class ReleaseHolder {
        ImageView image;
        TextView release_name;
        TextView artist;
        CircleImageView artist_image;
    }
}
