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

import java.util.ArrayList;

public class MusicAdapter extends BaseAdapter {
    private Context mContext;
    private ArtistList mArtistList;
    private LayoutInflater layoutInflater;

    public MusicAdapter(Context c) {
        this.layoutInflater = LayoutInflater.from(c);
        this.mContext = c;
    }

    public void setArtists(ArtistList artists) {
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
        MusicHolder holder = null;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.gridview_item, parent, false);
            holder = new MusicHolder();
            holder.image = (ImageView) convertView.findViewById(R.id.band_icon);
            holder.band = (TextView) convertView.findViewById(R.id.band_name);
            //holder.similar_band = (TextView) convertView.findViewById(R.id.b)
            convertView.setTag(holder);
        }

        if (holder == null) {
            holder = (MusicHolder) convertView.getTag();
        }

        String img = mArtistList.getArtists().get(pos).getImage();
        String name = mArtistList.getArtists().get(pos).getName();
        ArrayList<String> similar = mArtistList.getArtists().get(pos).getSimilar();

        holder.band.setText(name);
        //holder.similar_band.setText("Similar to " +  similar.get(0) + " and " + similar.get(1));
        Picasso.with(mContext).load(img).into(holder.image);

        return convertView;
    }

    private class MusicHolder {
        ImageView image;
        TextView band;
        TextView similar_band;
    }
}
