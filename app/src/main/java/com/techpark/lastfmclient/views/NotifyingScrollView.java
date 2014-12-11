package com.techpark.lastfmclient.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by Andrew Govorovsky on 07.12.14.
 */
public class NotifyingScrollView extends ScrollView {

    public NotifyingScrollView(Context context) {
        super(context);
    }

    public NotifyingScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NotifyingScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setListener(OnScrollChangedListener listener) {
        this.listener = listener;
    }

    public interface OnScrollChangedListener {
        void onScrollChanged(ScrollView from, int l, int r, int oldl, int oldt);
    }

    private OnScrollChangedListener listener;

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (listener != null) {
            listener.onScrollChanged(this, l, t, oldl, oldt);
        }
    }
}
