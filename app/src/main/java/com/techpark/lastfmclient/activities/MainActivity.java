package com.techpark.lastfmclient.activities;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.techpark.lastfmclient.R;
import com.techpark.lastfmclient.adapters.DrawerAdapter;
import com.techpark.lastfmclient.adapters.NavDrawerItem;
import com.techpark.lastfmclient.adapters.NavMenuHeader;
import com.techpark.lastfmclient.adapters.NavMenuItem;
import com.techpark.lastfmclient.adapters.NavMenuSection;
import com.techpark.lastfmclient.api.user.User;
import com.techpark.lastfmclient.fragments.LibraryMoreFragment;
import com.techpark.lastfmclient.fragments.MainListFragment;
import com.techpark.lastfmclient.fragments.NewReleasesMoreFragment;
import com.techpark.lastfmclient.fragments.RecentTracksMoreFragment;
import com.techpark.lastfmclient.fragments.RecommendedMoreFragment;
import com.techpark.lastfmclient.fragments.UpcomingEventsMoreFragment;
import com.techpark.lastfmclient.fragments.UserProfileFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by andrew on 28.10.14.
 */
public class MainActivity extends BaseNavDrawerActivity implements FragmentManager.OnBackStackChangedListener, FragmentDispatcher {


    private static final String TAG_NAME = "MainActivity";
    private Fragment curr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (savedInstanceState == null) {
            setFragment(new MainListFragment(), TAG_NAME, false);
        }

        getSupportFragmentManager().addOnBackStackChangedListener(this);
        shouldDisplayHomeUp();
    }


    private void shouldDisplayHomeUp() {
        boolean isStackEmpty = getSupportFragmentManager().getBackStackEntryCount() > 0;
        setUpEnabled(isStackEmpty);
        if (!isStackEmpty)
            mDrawerToggle.setHomeAsUpIndicator(R.drawable.back_with_padding);
//        getActionBar().setDisplayHomeAsUpEnabled(isStackEmpty);
    }


    public <T extends Fragment> boolean setFragment(T fragment, String tag, boolean addToBs) {
        curr = fragment;
        Fragment prev = getSupportFragmentManager().findFragmentByTag(tag);
        if (prev == null || !prev.isVisible()) {
            if (addToBs)
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment, tag).addToBackStack(null).commit();
            else
                getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, fragment, tag).commit();
            return true;
        }
        return false;
    }

    @Override
    protected NavDrawerConfiguration getNavDrawerConfiguration() {
        NavDrawerConfiguration configuration = new NavDrawerConfiguration();
        configuration.setMainLayout(R.layout.activity_main);
        configuration.setDrawerLayoutId(R.id.drawer_layout);
        configuration.setLeftDrawerId(R.id.left_drawer);
        configuration.setNavItems(createMenu());
//        configuration.setDrawerShadow(R.drawable.drawer_shadow);
        configuration.setDrawerOpenDesc(R.string.drawer_open); // unnecessary
        configuration.setDrawerCloseDesc(R.string.drawer_close);
        configuration.setBaseAdapter(new DrawerAdapter(this, R.layout.drawer_item, configuration.getNavItems()));
        return configuration;
    }


    public Fragment getVisibleFragment() {
        FragmentManager fm = getSupportFragmentManager();
        List<Fragment> fragments = fm.getFragments();
        for (Fragment f : fragments) {
            if (f != null && f.isVisible()) {
                return f;
            }
        }
        return null;
    }

    @Override
    protected void onNavItemSelected(int id) {
        NavMenuHeader header = (NavMenuHeader) navConf.getNavItems().get(0);
        switch (id) {
            case NavDrawerConstants.PROFILE:
                User u = User.EMPTY_USER;
                if (!header.getName().isEmpty()) {
                    u = new User(header.getName(), header.getFullName(), header.getAvatar(), "", -1, "", header.getPlays(), header.getSince());
                    u.setMostPlayedArtist(header.getPoster());
                }
                setFragment(UserProfileFragment.getInstance(u), "test", true);
                break;

            case NavDrawerConstants.LOG_OUT:
                logOut();
                break;

            case NavDrawerConstants.RECOMMENDED_MUSIC:
                setFragment(new RecommendedMoreFragment(), RecommendedMoreFragment.TAG, true);
                break;
            case NavDrawerConstants.UPCOMING_EVENTS:
                setFragment(new UpcomingEventsMoreFragment(), UpcomingEventsMoreFragment.TAG, true);
                break;
            case NavDrawerConstants.NEW_RELEASES:
                setFragment(new NewReleasesMoreFragment(), NewReleasesMoreFragment.TAG, true);
                break;

            case NavDrawerConstants.RECENT_TRACKS:
                setFragment(RecentTracksMoreFragment.getInstance(header.getName()), "t_more", true);
                break;
            case NavDrawerConstants.MY_LIBRARY:
                setFragment(LibraryMoreFragment.getInstance(header.getName()), "lib_more", true);
                break;

        }
    }


    private List<NavDrawerItem> createMenu() {
        return new ArrayList<>(Arrays.asList(
                NavMenuHeader.getInstance(NavDrawerConstants.PROFILE, User.EMPTY_USER),
                NavMenuSection.getInstance(1, "RECOMMENDATIONS"),
                NavMenuItem.getInstance(NavDrawerConstants.RECOMMENDED_MUSIC, "Music"),
                NavMenuItem.getInstance(NavDrawerConstants.NEW_RELEASES, "New Releases"),
                NavMenuSection.getInstance(105, "Events"),
                NavMenuItem.getInstance(NavDrawerConstants.UPCOMING_EVENTS, "Upcoming Events"),
                NavMenuSection.getInstance(108, "PROFILE"),
                NavMenuItem.getInstance(NavDrawerConstants.RECENT_TRACKS, "Recent Tracks"),
                NavMenuItem.getInstance(NavDrawerConstants.MY_LIBRARY, "My Library"),
                NavMenuItem.getInstance(NavDrawerConstants.LOG_OUT, "Log out")
        ));
    }

    @Override
    public void onBackStackChanged() {
        shouldDisplayHomeUp();
    }

    @Override
    public boolean onNavigateUp() {
        closeDrawer();
        getSupportFragmentManager().popBackStack();
        return true;
    }

    @Override
    public void setLogo(int resId) {
        getActionBar().setLogo(getResources().getDrawable(resId));
    }

    @Override
    public void setActionBarFade(int alpha) {
        mDrawerToggle.alphaStart = alpha; // kostil'
        mActionBarDrawable.setAlpha(alpha);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public int getActionBarFade() {
        return mActionBarDrawable.getAlpha();
    }


}
