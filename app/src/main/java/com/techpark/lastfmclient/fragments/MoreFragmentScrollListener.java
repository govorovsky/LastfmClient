package com.techpark.lastfmclient.fragments;

import android.widget.AbsListView;

/**
 * Created by Andrew Govorovsky on 12.12.14.
 */
public abstract class MoreFragmentScrollListener implements AbsListView.OnScrollListener {

    int currentPage = 2;
    int prevItemCount;
    boolean isLoading = false;

    protected MoreFragmentScrollListener(int prevItemCount) {
        this.prevItemCount = prevItemCount;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (totalItemCount < prevItemCount)
            return;

        if (isLoading && (totalItemCount > prevItemCount)) {
            isLoading = false;
            prevItemCount = totalItemCount;
            currentPage++;
        }

        if (!isLoading && (firstVisibleItem + visibleItemCount == totalItemCount)) {
            isLoading = true;
//            Log.e("FIRST VISIBLE INDEX", firstVisibleItem + " ");
//            Log.e("VISIBLE CNT", visibleItemCount + " ");
//            Log.e("TOTAL", totalItemCount + " ");
//            Log.e("prev item", prevItemCount + " ");
            loadMore();
        }

    }
    abstract void loadMore();
}
