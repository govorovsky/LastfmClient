package com.techpark.lastfmclient.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.techpark.lastfmclient.R;
import com.techpark.lastfmclient.api.track.RecentTrack;

import java.util.List;

/**
 * Created by Andrew Govorovsky on 04.12.14.
 */
public class RecentTracksAdapter extends ArrayAdapter<RecentTrack> {

    private Context mContext;
    private List<RecentTrack> tracks;
    private LayoutInflater mLayoutInflater;

    public RecentTracksAdapter(Context context, int resource, List<RecentTrack> objects) {
        super(context, resource, objects);
        tracks = objects;
        mContext = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TrackHolder holder = null;
        RecentTrack track = getItem(position);

        if (convertView == null) {
            // set attachToRoot to false because we only want to initialize layoutparams
            convertView = mLayoutInflater.inflate(R.layout.recenttrack_item, parent, false);

            TextView name = (TextView) convertView.findViewById(R.id.track_name);
            TextView artist = (TextView) convertView.findViewById(R.id.artist_name);
            ImageView cover = (ImageView) convertView.findViewById(R.id.artist_pic);
            TextView time = (TextView) convertView.findViewById(R.id.track_time);
            View sep = convertView.findViewById(R.id.sep_up);


            holder = new TrackHolder(name, artist, cover, time, sep);
            convertView.setTag(holder);
        }

        if (holder == null) {
            holder = (TrackHolder) convertView.getTag();
        }

        holder.artist.setText(track.getArtist());
        holder.name.setText(track.getName());
        holder.time.setText(track.getDate());
        if (track.getImg().isEmpty()) {
            holder.cover.setImageResource(R.drawable.star);
        } else {
            Picasso.with(mContext).load(track.getImg()).into(holder.cover);
        }


        if (position == 0) {
//            holder.sep.setVisibility(View.VISIBLE);
        } else {
//            holder.sep.setVisibility(View.INVISIBLE);
        }


        return convertView;

    }

    private class TrackHolder {

        TrackHolder(TextView name, TextView artist, ImageView cover, TextView time, View sep) {
            this.name = name;
            this.artist = artist;
            this.cover = cover;
            this.time = time;
            this.sep = sep;
        }

        TextView name;
        TextView artist;
        ImageView cover;
        TextView time;
        View sep;

    }
}
