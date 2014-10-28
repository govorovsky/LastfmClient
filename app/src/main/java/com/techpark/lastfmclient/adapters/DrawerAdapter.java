package com.techpark.lastfmclient.adapters;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by andrew on 29.10.14.
 */
public class DrawerAdapter extends ArrayAdapter<DrawerItem> {

    Context context;
    List<DrawerItem> drawerItemList;
    int layoutResID;

    public DrawerAdapter(Context context, int resource, List<DrawerItem> list) {
        super(context, resource);
        this.context = context;
        this.drawerItemList = list;
        this.layoutResID = resource;
    }
}
