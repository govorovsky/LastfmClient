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

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecommendedAdapter extends BaseAdapter {
    private Context mContext;
    private RecommendedArtistList mArtistList;
    private LayoutInflater layoutInflater;

    public RecommendedAdapter(Context c) {
        this.layoutInflater = LayoutInflater.from(c);
        this.mContext = c;
    }

    public void setArtists(RecommendedArtistList artists) {
        this.mArtistList = artists;
    }

    @Override
    public int getCount() {
        if (mArtistList == null)
            return 0;
        return mArtistList.getArtists().size();
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
        ArtistHolder holder = null;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.gridview_item, parent, false);
            holder = new ArtistHolder();
            holder.image = (ImageView) convertView.findViewById(R.id.band_icon);
            holder.band = (TextView) convertView.findViewById(R.id.band_name);
            holder.similar_band = (TextView) convertView.findViewById(R.id.band_similar);
            holder.similar_first = (CircleImageView) convertView.findViewById(R.id.band_similar_first);
            holder.similar_second = (CircleImageView) convertView.findViewById(R.id.band_similar_second);
            convertView.setTag(holder);
        }

        if (holder == null) {
            holder = (ArtistHolder) convertView.getTag();
        }

        holder.band.setText(mArtistList.getArtists().get(pos).getArtistName());

        Picasso.with(mContext).load(
                mArtistList.getArtists().get(pos).getImage(Artist.IMAGE_MEGA)
        ).into(holder.image);

        Artist afirst = mArtistList.getArtists().get(pos).getSimilarFirst();
        Artist asecond = mArtistList.getArtists().get(pos).getSimilarSecond();

        holder.similar_band.setText("Similar to " + afirst.getArtistName() + " and " + asecond.getArtistName());
        Picasso.with(mContext).load(
                afirst.getImage(Artist.IMAGE_LARGE)
        ).into(holder.similar_first);

        Picasso.with(mContext).load(
                asecond.getImage(Artist.IMAGE_LARGE)
        ).into(holder.similar_second);

        return convertView;
    }

    private class ArtistHolder {
        ImageView image;
        TextView band;
        TextView similar_band;
        CircleImageView similar_first;
        CircleImageView similar_second;
    }
}
