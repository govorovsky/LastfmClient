package com.techpark.lastfmclient.views;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;

/**
 * Created by Andrew Govorovsky on 08.12.14.
 */
public class StretchedView<T extends ViewGroup> {
    protected T layout;
    private final DataSetObserver dataSetObserver;
    private ListAdapter adapter;
    private AdapterView.OnItemClickListener onItemClickListener;

    public StretchedView(T layout) {
        this.layout = layout;
        this.dataSetObserver = new DataSetObserver() {
            @Override
            public void onChanged() {
                syncDataFromAdapter();
                super.onChanged();
            }

            @Override
            public void onInvalidated() {
                syncDataFromAdapter();
                super.onInvalidated();
            }
        };

    }

    public void setAdapter(ListAdapter adapter) {
        ensureDataSetObserverIsUnregistered();

        this.adapter = adapter;
        if (this.adapter != null) {
            this.adapter.registerDataSetObserver(dataSetObserver);
        }
        syncDataFromAdapter();
    }

    protected void ensureDataSetObserverIsUnregistered() {
        if (this.adapter != null) {
            this.adapter.unregisterDataSetObserver(dataSetObserver);
        }
    }

    public Object getItemAtPosition(int position) {
        return adapter != null ? adapter.getItem(position) : null;
    }

    public void setSelection(int i) {
        layout.getChildAt(i).setSelected(true);
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public ListAdapter getAdapter() {
        return adapter;
    }

    public int getCount() {
        return adapter != null ? adapter.getCount() : 0;
    }

    private void syncDataFromAdapter() {
        layout.removeAllViews();
        if (adapter != null) {
            int count = adapter.getCount();
            for (int i = 0; i < count; i++) {
                View view = adapter.getView(i, null, layout);
                boolean enabled = adapter.isEnabled(i);
                if (enabled) {
                    final int position = i;
                    final long id = adapter.getItemId(position);
                    view.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            if (onItemClickListener != null) {
                                onItemClickListener.onItemClick(null, v, position, id);
                            }
                        }
                    });
                }
                add(view);
            }
        }
    }

    public void add(View view) {
        layout.addView(view);
    }
}
