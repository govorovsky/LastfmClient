package com.techpark.lastfmclient.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;

/**
 * Created by Andrew Govorovsky on 08.12.14.
 */
public class StretchedListView extends LinearLayout {

    private StretchedView<LinearLayout> stretchedView;

    public StretchedListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        stretchedView = new StretchedView<LinearLayout>(this);
        setOrientation(LinearLayout.VERTICAL);
    }

    public void setAdapter(ListAdapter adapter) {
        stretchedView.setAdapter(adapter);
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        stretchedView.setOnItemClickListener(onItemClickListener);
    }
}
