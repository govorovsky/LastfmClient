package com.techpark.lastfmclient.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.techpark.lastfmclient.R;

import java.util.List;

/**
 * Created by andrew on 29.10.14.
 */
public class DrawerAdapter extends ArrayAdapter<NavDrawerItem> {

    private List<NavDrawerItem> drawerItemList;
    private LayoutInflater layoutInflater;

    public DrawerAdapter(Context context, int resource, List<NavDrawerItem> list) {
        super(context, resource, list);
        this.drawerItemList = list;
        this.layoutInflater = LayoutInflater.from(context);
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
//            ImageButton avatar = (ImageButton) convertView.findViewById(R.id.avatar);
            holder = new NavHeaderHolder();
//            holder.avatar = avatar;
            holder.fullname = fullname;
            holder.since = since;

            convertView.setTag(holder);
        }
        if (holder == null) {
            holder = (NavHeaderHolder) convertView.getTag();
        }

        holder.fullname.setText(navMenuHeader.getFullName());
        holder.since.setText(navMenuHeader.getSince());
//        holder.p.setText(navMenuHeader.getFullName());

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
        RelativeLayout poster;
        TextView fullname;
        ImageButton avatar;
        TextView since;
    }

    @Override
    public boolean isEnabled(int position) {
        return getItem(position).isEnabled();
    }
}
