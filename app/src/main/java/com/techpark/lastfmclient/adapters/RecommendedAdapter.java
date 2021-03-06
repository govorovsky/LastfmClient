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
import com.techpark.lastfmclient.fragments.ArtistFragment;
import com.techpark.lastfmclient.fragments.BaseFragment;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecommendedAdapter extends BaseAdapter {
    private Context mContext;
    private RecommendedArtistList mArtistList = null;
    private LayoutInflater layoutInflater = null;

    public RecommendedAdapter(Context context, RecommendedArtistList artists) {
        this.mContext = context;
        this.layoutInflater = LayoutInflater.from(mContext);
        this.mArtistList = artists;
    }

    @Override
    public int getCount() {
        if (mArtistList == null)
            return 0;
        return mArtistList.getArtists().size();
    }

    @Override
    public Object getItem(int pos) {
        if (mArtistList == null)
            return null;
        return mArtistList.getArtists().get(pos);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {
        ArtistHolder holder = null;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.recommended_item, null);
            holder = new ArtistHolder(convertView);
            convertView.setTag(holder);
        }

        if (holder == null) {
            holder = (ArtistHolder) convertView.getTag();
        }

        holder.band.setText(mArtistList.getArtists().get(pos).getArtistName());

        String image_artist_path = mArtistList.getArtists().get(pos).getImage(Artist.ImageSize.MEGA);
        if (!image_artist_path.equals(""))
            Picasso.with(mContext).load(image_artist_path).into(holder.image);

        Artist afirst = mArtistList.getArtists().get(pos).getSimilarFirst();
        Artist asecond = mArtistList.getArtists().get(pos).getSimilarSecond();

        StringBuilder similar_label = new StringBuilder("Similar to ")
                                            .append(afirst.getArtistName());

        if (asecond != null) {
            similar_label.append(" and ").append(asecond.getArtistName());
        }

        holder.similar_band.setText(similar_label.toString());
        image_artist_path = afirst.getImage(Artist.ImageSize.LARGE);
        if (image_artist_path != null && !image_artist_path.equals(""))
            Picasso.with(mContext).load(image_artist_path).into(holder.similar_first);

        if (asecond != null) {
            holder.similar_second.setVisibility(View.VISIBLE);
            image_artist_path = asecond.getImage(Artist.ImageSize.LARGE);
            if (image_artist_path != null && !image_artist_path.equals(""))
                Picasso.with(mContext).load(image_artist_path).into(holder.similar_second);
        } else {
            holder.similar_second.setVisibility(View.GONE);
        }

        return convertView;
    }

    private class ArtistHolder {
        ImageView image;
        TextView band;
        TextView similar_band;
        CircleImageView similar_first;
        CircleImageView similar_second;

        ArtistHolder(View v) {
            this.image = (ImageView) v.findViewById(R.id.band_icon);
            this.band = (TextView) v.findViewById(R.id.band_name);
            this.similar_band = (TextView) v.findViewById(R.id.band_similar);
            this.similar_first = (CircleImageView) v.findViewById(R.id.band_similar_second);
            this.similar_second = (CircleImageView) v.findViewById(R.id.band_similar_first);
        }
    }
}
