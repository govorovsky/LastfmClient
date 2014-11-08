package com.techpark.lastfmclient.activities;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.techpark.lastfmclient.R;
import com.techpark.lastfmclient.adapters.DrawerAdapter;
import com.techpark.lastfmclient.adapters.NavDrawerItem;
import com.techpark.lastfmclient.adapters.NavMenuHeader;
import com.techpark.lastfmclient.adapters.NavMenuItem;
import com.techpark.lastfmclient.adapters.NavMenuSection;
import com.techpark.lastfmclient.api.user.UserHelpers;
import com.techpark.lastfmclient.fragments.MainListFragment;
import com.techpark.lastfmclient.services.ServiceHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by andrew on 28.10.14.
 */
public class MainActivity extends BaseNavDrawerActivity {

    private ServiceHelper mServiceHelper;

    private static final String TAG_NAME = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mServiceHelper = new ServiceHelper(this);
        mServiceHelper.getUser("govorovsky");

        if (getSupportFragmentManager().findFragmentByTag(TAG_NAME) == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new MainListFragment(), TAG_NAME).commit();
        }
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
        configuration.setBaseAdapter(
                new DrawerAdapter(this, R.layout.drawer_item, configuration.getNavItems()));
        return configuration;
    }

    @Override
    protected void onNavItemSelected(int id) {
        /* TODO fragments logic here */
        switch (id) {
            case 101:
                /* fragment creation */
                break;

            case NavDrawerConstants.LOG_OUT:
                logOut();
                break;

        }

    }

    private void logOut() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private List<NavDrawerItem> createMenu() {
        NavDrawerItem[] menu = new NavDrawerItem[]{
                NavMenuHeader.getInstance(NavDrawerConstants.PROFILE, 12, "12.4.09", "Andrew Govorovsky", ""),
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
        };
        return Arrays.asList(menu);
    }
}
