package com.techpark.lastfmclient.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.techpark.lastfmclient.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by andrew on 29.10.14.
 */
public class DrawerAdapter extends ArrayAdapter<NavDrawerItem> {

    private List<NavDrawerItem> drawerItemList;
    private LayoutInflater layoutInflater;
    private Context context;

    public DrawerAdapter(Context context, int resource, List<NavDrawerItem> list) {
        super(context, resource, list);
        this.drawerItemList = list;
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        Log.d("adapter", "init");
    }

    @Override

    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        NavDrawerItem menuItem = this.getItem(position);

        switch (menuItem.getType()) {
            case NavMenuItem.ITEM_TYPE:
                view = getSimpleView(convertView, parent, menuItem, R.layout.drawer_item);
                break;
            case NavMenuSection.SECTION_TYPE:
                view = getSimpleView(convertView, parent, menuItem, R.layout.drawer_section);
                break;

            case NavMenuHeader.HEADER_TYPE:
                view = getHeaderView(convertView, parent, menuItem);
                break;
            default:
                break;
        }
        return view;
    }

    private View getHeaderView(View convertView, ViewGroup parent, NavDrawerItem menuItem) {
        NavMenuHeader navMenuHeader = (NavMenuHeader) menuItem;
        NavHeaderHolder holder = null;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.drawer_header, parent, false);
            TextView fullname = (TextView) convertView.findViewById(R.id.full_name);
            TextView since = (TextView) convertView.findViewById(R.id.since_date);
            ImageView poster = (ImageView) convertView.findViewById(R.id.poster);
            CircleImageView avatar = (CircleImageView) convertView.findViewById(R.id.avatar);

            holder = new NavHeaderHolder();
            holder.fullname = fullname;
            holder.since = since;
            holder.poster = poster;
            holder.avatar = avatar;

            convertView.setTag(holder);
        }
        if (holder == null) {
            holder = (NavHeaderHolder) convertView.getTag();
        }

        holder.fullname.setText(navMenuHeader.getFullName());
        holder.since.setText(navMenuHeader.getSince());
        holder.poster.setImageResource(R.drawable.gunit); /* TODO !! */

        String img = navMenuHeader.getAvatar();

        if (img.isEmpty()) {
            Picasso.with(context).load(R.drawable.empty_avatar).into(holder.avatar);
        } else {
            Picasso.with(context).load(img).into(holder.avatar);
        }

        return convertView;
    }

    private View getSimpleView(View convertView, ViewGroup parent, NavDrawerItem menuItem, int layout) {
        NavTextHolder holder = null;
        if (convertView == null) {
            convertView = layoutInflater.inflate(layout, parent, false);
            TextView label = (TextView) convertView.findViewById(R.id.label);
            holder = new NavTextHolder();
            holder.textView = label;

            convertView.setTag(holder);
        }
        if (holder == null) {
            holder = (NavTextHolder) convertView.getTag();
        }

        holder.textView.setText(menuItem.getLabel());

        return convertView;
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    private class NavTextHolder {
        TextView textView;
    }

    private class NavHeaderHolder {
        ImageView poster;
        TextView fullname;
        CircleImageView avatar;
        TextView since;
    }

    @Override
    public boolean isEnabled(int position) {
        return getItem(position).isEnabled();
    }
}
