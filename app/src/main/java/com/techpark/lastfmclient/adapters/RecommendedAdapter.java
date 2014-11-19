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
    private RecommendedArtistList mArtistList = null;
    private LayoutInflater layoutInflater = null;

    public RecommendedAdapter(Context c, RecommendedArtistList artists) {
        this.mContext = c;
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
            //convertView = layoutInflater.inflate(R.layout.recommended_item, parent, false);
            convertView = layoutInflater.inflate(R.layout.recommended_item, null);
            holder = new ArtistHolder(convertView);
            convertView.setTag(holder);
        }

        if (holder == null) {
            holder = (ArtistHolder) convertView.getTag();
        }

        holder.band.setText(mArtistList.getArtists().get(pos).getArtistName());

        Picasso.with(mContext).load(
                mArtistList.getArtists().get(pos).getImage(Artist.ImageSize.MEGA)
        ).into(holder.image);

        Artist afirst = mArtistList.getArtists().get(pos).getSimilarFirst();
        Artist asecond = mArtistList.getArtists().get(pos).getSimilarSecond();

        StringBuilder similar_label = new StringBuilder("Similar to ")
                                            .append(afirst.getArtistName());

        if (asecond != null) {
            similar_label.append(" and ").append(asecond.getArtistName());
        }

        holder.similar_band.setText(similar_label.toString());
        Picasso.with(mContext).load(
                afirst.getImage(Artist.ImageSize.LARGE)
        ).into(holder.similar_first);

        if (asecond != null) {
            Picasso.with(mContext).load(
                    asecond.getImage(Artist.ImageSize.LARGE)
            ).into(holder.similar_second);
        } else {
            holder.similar_second.setVisibility(View.GONE);
        }

        return convertView;
    }

    private class ArtistHolder implements View.OnClickListener {
        ImageView image;
        TextView band;
        TextView similar_band;
        CircleImageView similar_first;
        CircleImageView similar_second;

        private boolean clickFlag = false;


        ArtistHolder(View v) {
            this.image = (ImageView) v.findViewById(R.id.band_icon);
            this.band = (TextView) v.findViewById(R.id.band_name);
            this.similar_band = (TextView) v.findViewById(R.id.band_similar);
            this.similar_first = (CircleImageView) v.findViewById(R.id.band_similar_first);
            this.similar_second = (CircleImageView) v.findViewById(R.id.band_similar_second);

            this.similar_band.setOnClickListener(this);
        }

        //TODO + redirect for click on images
        @Override
        public void onClick(View view) {
            /*
            Log.d("onClick", view.toString());
            this.clickFlag = !this.clickFlag;
            ViewGroup.LayoutParams params = view.getLayoutParams();
            params.height = (this.clickFlag) ? ViewGroup.LayoutParams.MATCH_PARENT : 22;
            view.setLayoutParams(params);
            */
        }
    }
}
