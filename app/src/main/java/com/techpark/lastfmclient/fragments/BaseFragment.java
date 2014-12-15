package com.techpark.lastfmclient.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.techpark.lastfmclient.activities.FragmentDispatcher;
import com.techpark.lastfmclient.services.ServiceHelper;


/**
 * Created by Andrew Govorovsky on 26.11.14.
 */
public abstract class BaseFragment extends Fragment {
    protected abstract FragmentConf getFragmentConf();

    protected FragmentDispatcher fragmentDispatcher;
    protected ServiceHelper serviceHelper;

    private FragmentConf conf;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        conf = getFragmentConf();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(conf.getLayout(), container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState != null) {
            fragmentDispatcher.setActionBarFade(savedInstanceState.getInt("fade"));
        } else {
            fragmentDispatcher.setActionBarFade(conf.getActionBarFade());
        }

        fragmentDispatcher.setTitle(conf.getTitle());
        fragmentDispatcher.setLogo(conf.getLogo());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("fade", fragmentDispatcher.getActionBarFade());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        serviceHelper = new ServiceHelper(activity);

        if (activity instanceof FragmentDispatcher) {
            fragmentDispatcher = (FragmentDispatcher) activity;
        } else {
            throw new IllegalStateException("Parent activity must implement fragment dispatching");
        }
    }
}
