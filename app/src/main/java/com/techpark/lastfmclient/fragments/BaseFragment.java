package com.techpark.lastfmclient.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.techpark.lastfmclient.activities.FragmentDispatcher;

import javax.xml.parsers.FactoryConfigurationError;

/**
 * Created by Andrew Govorovsky on 26.11.14.
 */
public abstract class BaseFragment extends Fragment {
    protected abstract FragmentConf getFragmentConf();

    protected FragmentDispatcher fragmentDispatcher;

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

        fragmentDispatcher.setTitle(conf.getTitle());
        fragmentDispatcher.setActionBarFade(conf.getActionBarFade());
        fragmentDispatcher.setLogo(conf.getLogo());
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (activity instanceof FragmentDispatcher) {
            fragmentDispatcher = (FragmentDispatcher) activity;
        } else {
            throw new IllegalStateException("Parent activity must implement fragment dispatching");
        }
    }
}
