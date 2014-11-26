package com.techpark.lastfmclient.activities;

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
import com.techpark.lastfmclient.fragments.MainListFragment;
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
        if (getSupportFragmentManager().findFragmentByTag(tag) == null) {
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

    @Override
    protected void onNavItemSelected(int id) {
        /* TODO fragments logic here */
        switch (id) {
            case NavDrawerConstants.PROFILE:
                setFragment(new UserProfileFragment(), "test", true);
                /* fragment creation */
                break;

            case NavDrawerConstants.LOG_OUT:
                logOut();
                break;
        }
    }


    private List<NavDrawerItem> createMenu() {
        return new ArrayList<>(Arrays.asList(
                NavMenuHeader.getInstance(NavDrawerConstants.PROFILE, User.EMPTY_USER),
                NavMenuSection.getInstance(100, "RECOMMENDATIONS"),
                NavMenuItem.getInstance(101, "Music"),
                NavMenuItem.getInstance(102, "Albums"),
                NavMenuItem.getInstance(103, "New Releases"),
                NavMenuSection.getInstance(105, "Events"),
                NavMenuItem.getInstance(104, "My Events"),
                NavMenuItem.getInstance(106, "Your Recommendations"),
                NavMenuItem.getInstance(107, "Events Near Me"),
                NavMenuSection.getInstance(108, "PROFILE"),
                NavMenuItem.getInstance(NavDrawerConstants.LOG_OUT, "Log out")
        ));
    }

    @Override
    public void onBackStackChanged() {
        shouldDisplayHomeUp();
    }

    @Override
    public boolean onNavigateUp() {
        getSupportFragmentManager().popBackStack();
        return true;
    }

    @Override
    public void setLogo(int resId) {
        getActionBar().setLogo(getResources().getDrawable(resId));
    }

    @Override
    public void setActionBarFade(int alpha) {
        mActionBarDrawable.setAlpha(alpha);
    }


}
