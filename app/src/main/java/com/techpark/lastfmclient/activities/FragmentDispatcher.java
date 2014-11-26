package com.techpark.lastfmclient.activities;

import android.support.v4.app.Fragment;

/**
 * Created by Andrew Govorovsky on 26.11.14.
 */
public interface FragmentDispatcher {

    <T extends Fragment> boolean setFragment(T fragment, String tag, boolean addToBs);

    void setLogo(int resId);

    void setTitle(CharSequence title);

    void setActionBarFade(int alpha);

}
