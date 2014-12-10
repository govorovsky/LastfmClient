package com.techpark.lastfmclient.views;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ListAdapter;

/**
 * Created by Andrew Govorovsky on 10.12.14.
 */
public class StretchedGridView extends GridLayout {

    int width;
    float padding;
    private StretchedView<GridLayout> gridLayoutStretchedView;

    public StretchedGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
        padding = 10 / (metrics.densityDpi / 160f);
        width = metrics.widthPixels;
        gridLayoutStretchedView = new StretchedView<GridLayout>(this) {
            int i = 0;

            @Override
            public void add(View view) {
                GridLayout.LayoutParams params = new LayoutParams();
                params.width = (int) (width / 2 - padding / 2 + 1);
                if (i % 2 == 0) // TODO !! consider using compat GridLayout
                params.rightMargin = (int) padding;
                params.bottomMargin = (int) padding;
//                params.setMargins(10,10,10,10);
                view.setLayoutParams(params);
                layout.addView(view);
                i++;
            }
        };
    }

    public void setAdapter(ListAdapter adapter) {
        gridLayoutStretchedView.setAdapter(adapter);
    }
}
