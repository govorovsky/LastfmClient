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
import com.techpark.lastfmclient.api.artist.Artist;

import java.util.List;

/**
 * Created by max on 18/12/14.
 */
public class SimilarArtistAdapter extends ArrayAdapter<Artist> {

    private Context mContext;
    private LayoutInflater mLayoutInflater;

    public SimilarArtistAdapter(Context context, int resource, List<Artist> objects) {
        super(context, resource, objects);
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ArtistHolder artistHolder = null;
        Artist artist = getItem(position);

        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.similar_artist, parent, false);

            TextView name = (TextView) convertView.findViewById(R.id.artist_name);
            ImageView cover = (ImageView) convertView.findViewById(R.id.artist_pic);

            artistHolder = new ArtistHolder(name, cover);
            convertView.setTag(artistHolder);
        }

        if (artistHolder == null) {
            artistHolder = (ArtistHolder) convertView.getTag();
        }

        artistHolder.name.setText(artist.getArtistName());

        if (artist.getImage(Artist.ImageSize.MEGA) != null) {
            Picasso.with(mContext).load(
                    artist.getImage(Artist.ImageSize.MEGA)
            ).into(artistHolder.cover);
        } else {
            //artistHolder.cover.setImageResource(R.drawable.kar);
        }
        return convertView;
    }

    private class ArtistHolder {
        private ArtistHolder(TextView name, ImageView cover) {
            this.name = name;
            this.cover = cover;
        }

        TextView name;
        ImageView cover;
    }
}
