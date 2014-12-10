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
import com.techpark.lastfmclient.api.event.Event;
import com.techpark.lastfmclient.api.release.Release;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by max on 27/11/14.
 */
public class EventsAdapter extends BaseAdapter {
    private Context mContext;
    private EventsList mEventsList;
    private LayoutInflater layoutInflater;

    public EventsAdapter(Context c, EventsList list) {
        this.layoutInflater = LayoutInflater.from(c);
        this.mContext = c;
        this.mEventsList = list;
    }

    public void setEvents(EventsList events) {
        this.mEventsList = events;
    }

    @Override
    public int getCount() {
        if (mEventsList == null)
            return 0;
        return mEventsList.getEvents().size();
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
        EventHolder holder = null;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.event_item, parent, false);
            holder = new EventHolder();
            holder.image = (ImageView) convertView.findViewById(R.id.event_icon);
            holder.month = (TextView) convertView.findViewById(R.id.event_month);
            holder.day = (TextView) convertView.findViewById(R.id.event_day);
            holder.artists = (TextView) convertView.findViewById(R.id.event_name);
            holder.place = (TextView) convertView.findViewById(R.id.event_place);
            holder.artists_images = null; //TODO
            convertView.setTag(holder);
        }

        if (holder == null) {
            holder = (EventHolder) convertView.getTag();
        }

        EventsList.EventWrapper e = mEventsList.getEvents().get(pos);

        String[] date = e.getDate().split(" ");

        if (holder.month == null)
            Log.d("EventsAdapter", "NULL");

        holder.month.setText(date[2]);
        holder.day.setText(date[1]);
        holder.place.setText(e.getVenueName() + "; " + e.getVenueLocation());

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < e.getArtists().size() - 1; ++i) {
            Artist a = e.getArtist().get(i);
            builder.append(a.getArtistName()).append(", ");

            //Picasso.with(mContext).load(
            //        a.getImage(Artist.ImageSize.SMALL)
            //).into(holder.artists_images.get(i));
        }
        builder.append(e.getArtist().get(e.getArtist().size() - 1).getArtistName());
        holder.artists.setText(builder.toString());

        //Picasso.with(mContext).load(
        //        e.getArtist().get(e.getArtist().size() - 1).getImage(Artist.ImageSize.SMALL)
        //).into(holder.artists_images.get(e.getArtist().size() - 1));

        Picasso.with(mContext).load(
                e.getImage(Event.ImageSize.EXTRALARGE)
        ).into(holder.image);

        return convertView;
    }

    private class EventHolder {
        ImageView image;
        TextView month;
        TextView day;
        TextView place;
        TextView artists;
        ArrayList<CircleImageView> artists_images = new ArrayList<>(3);
    }

}
