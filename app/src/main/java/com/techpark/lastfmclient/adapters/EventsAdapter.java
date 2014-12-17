package com.techpark.lastfmclient.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import de.hdodenhof.circleimageview.CircleImageView;

import com.squareup.picasso.Picasso;
import com.techpark.lastfmclient.R;
import com.techpark.lastfmclient.api.artist.Artist;

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
            holder.title = (TextView) convertView.findViewById(R.id.event_name);
            holder.place = (TextView) convertView.findViewById(R.id.event_place);
            holder.attendance = (TextView) convertView.findViewById(R.id.event_attendance);
            holder.artist1 = (CircleImageView) convertView.findViewById(R.id.event_band1_icon);
            holder.artist2 = (CircleImageView) convertView.findViewById(R.id.event_band2_icon);
            holder.artist3 = (CircleImageView) convertView.findViewById(R.id.event_band3_icon);

            convertView.setTag(holder);
        }

        if (holder == null) {
            holder = (EventHolder) convertView.getTag();
        }

        EventsList.EventWrapper e = mEventsList.getEvents().get(pos);

        String[] date = e.getDate().split(" ");
        holder.month.setText(date[2]);
        holder.day.setText(date[1]);

        String[] places = e.getVenueLocation().split(";");
        StringBuilder place = new StringBuilder().append(e.getVenueName());
        for (String s : places)
            place.append(", ").append(s);
        holder.place.setText(place.toString());

        holder.title.setText(e.getTitle());

        if (e.getArtist().size() > 0) {
            Artist a = e.getArtist().get(0);
            holder.artist1.setVisibility(View.VISIBLE);
            Picasso.with(mContext).load(
                    a.getImage(Artist.ImageSize.LARGE)
            ).into(holder.artist1);

            Picasso.with(mContext).load(
                    a.getImage(Artist.ImageSize.MEGA)
            ).into(holder.image);
        } else {
            holder.artist1.setVisibility(View.GONE);
        }

        if (e.getArtist().size() > 1) {
            Artist a = e.getArtist().get(1);
            holder.artist2.setVisibility(View.VISIBLE);
            Picasso.with(mContext).load(
                    a.getImage(Artist.ImageSize.LARGE)
            ).into(holder.artist2);
        } else {
            holder.artist2.setVisibility(View.GONE);
        }

        if (e.getArtist().size() > 2) {
            Artist a = e.getArtist().get(2);
            holder.artist3.setVisibility(View.VISIBLE);
            Picasso.with(mContext).load(
                    a.getImage(Artist.ImageSize.LARGE)
            ).into(holder.artist3);
        } else {
            holder.artist3.setVisibility(View.GONE);
        }

        holder.attendance.setText("" + e.getAttendance());

        return convertView;
    }

    private class EventHolder {
        ImageView image;
        TextView month;
        TextView day;
        TextView place;
        TextView title;
        TextView attendance;

        CircleImageView artist1;
        CircleImageView artist2;
        CircleImageView artist3;
    }

}
