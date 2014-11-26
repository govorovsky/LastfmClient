package com.techpark.lastfmclient.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.techpark.lastfmclient.R;

/**
 * Created by Andrew Govorovsky on 24.11.14.
 */
public class UserProfileFragment extends BaseFragment {

    private static final String TITLE = "Profile";

    @Override
    protected FragmentConf getFragmentConf() {
        FragmentConf conf = new FragmentConf();
        conf.setActionBarFade(FragmentConf.ActionBarState.TRANSPARENT);
        conf.setLogo(R.drawable.slogo_with_padding);
        conf.setTitle(TITLE);
        conf.setLayout(R.layout.user_profile_fragment);
        return conf;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}
