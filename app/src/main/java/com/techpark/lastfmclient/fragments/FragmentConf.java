package com.techpark.lastfmclient.fragments;

/**
 * Created by Andrew Govorovsky on 26.11.14.
 */

/**
 * Store configuration for fragment
 */
public class FragmentConf {

    private CharSequence title;
    private int logo;
    private int actionBarFade;
    private int layout;



    public static class ActionBarState {
        public static final int VISIBLE = 255;
        public static final int TRANSPARENT = 0;
    }


    public CharSequence getTitle() {
        return title;
    }

    public void setTitle(CharSequence title) {
        this.title = title;
    }

    public int getLogo() {
        return logo;
    }

    public void setLogo(int logo) {
        this.logo = logo;
    }

    public int getActionBarFade() {
        return actionBarFade;
    }

    public void setActionBarFade(int actionBarFade) {
        this.actionBarFade = actionBarFade;
    }

    public int getLayout() {
        return layout;
    }

    public void setLayout(int layout) {
        this.layout = layout;
    }
}
