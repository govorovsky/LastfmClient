package com.techpark.lastfmclient.activities;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.techpark.lastfmclient.R;
import com.techpark.lastfmclient.adapters.NavDrawerItem;
import com.techpark.lastfmclient.adapters.NavMenuHeader;
import com.techpark.lastfmclient.api.user.User;
import com.techpark.lastfmclient.api.user.UserHelpers;
import com.techpark.lastfmclient.db.UsersTable;
import com.techpark.lastfmclient.services.ServiceHelper;

import java.util.List;

/**
 * Created by andrew on 29.10.14.
 */
public abstract class BaseNavDrawerActivity extends FragmentActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private DrawerLayout mDrawerLayout;
    private ServiceHelper mServiceHelper;

    private ListView mDrawerList;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

    protected NavDrawerConfiguration navConf;
    protected Drawable mActionBarDrawable;
    protected MyActionBarDrawerToggle mDrawerToggle;

    protected abstract NavDrawerConfiguration getNavDrawerConfiguration();

    protected abstract void onNavItemSelected(int id);


    protected String user;
    protected String session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        navConf = getNavDrawerConfiguration();


        if (savedInstanceState != null) {
            user = savedInstanceState.getString(LoginActivity.USERNAME_BUNDLE);
            session = savedInstanceState.getString(LoginActivity.SESSION_BUNDLE);
        } else {
            user = getIntent().getExtras().getString(LoginActivity.USERNAME_BUNDLE);
            session = getIntent().getExtras().getString(LoginActivity.SESSION_BUNDLE);
        }
        Log.e("USER=", user);
        if (user.isEmpty()) {
            logOut();
        }

        mServiceHelper = new ServiceHelper(this);
        mServiceHelper.getUser(user);

//        requestWindowFeature(Window.FEATURE_NO_TITLE); // remove title bar

        setContentView(navConf.getMainLayout());

        mTitle = mDrawerTitle = "";
        setTitle(mTitle); // dirty :(

        mDrawerLayout = (DrawerLayout) findViewById(navConf.getDrawerLayoutId());
        mDrawerList = (ListView) findViewById(navConf.getLeftDrawerId());
        mDrawerList.setAdapter(navConf.getBaseAdapter());
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());


//        mDrawerLayout.setDrawerShadow(navConf.getDrawerShadow(), GravityCompat.START);

        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        mActionBarDrawable = getResources().getDrawable(R.drawable.ab_background);
        mActionBarDrawable.setAlpha(255); // may it be visible
        getActionBar().setBackgroundDrawable(mActionBarDrawable);


        mDrawerToggle = new MyActionBarDrawerToggle();
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        createUserLoader(user);

    }


    protected void setUpEnabled(boolean enabled) {
        mDrawerToggle.setDrawerIndicatorEnabled(!enabled);
    }


    private Loader createUserLoader(String username) {
        Bundle b = new Bundle();
        b.putString(LoginActivity.USERNAME_BUNDLE, username);
        return getSupportLoaderManager().restartLoader(0, b, this);
    }


    protected void logOut() {
        dropDatabase();
        UserHelpers.clearUserSession(this);
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    protected void dropDatabase() {
        mServiceHelper.freeDataBase();
    }

    protected int getDrawerIcon() {
        return R.drawable.ic_ab_burger;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (navConf.getActionMenuItemsToHideWhenDrawerOpen() != null) {
            boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
            for (int iItem : navConf.getActionMenuItemsToHideWhenDrawerOpen()) {
                menu.findItem(iItem).setVisible(!drawerOpen);
            }
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mDrawerToggle.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
                mDrawerLayout.closeDrawer(mDrawerList);
            } else {
                mDrawerLayout.openDrawer(mDrawerList);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected DrawerLayout getDrawerLayout() {
        return mDrawerLayout;
    }

    protected ActionBarDrawerToggle getDrawerToggle() {
        return mDrawerToggle;
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }


    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
            mDrawerLayout.closeDrawer(Gravity.LEFT);
        } else {
            super.onBackPressed();
        }
    }

    public void selectItem(int position) {

        if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
            mDrawerLayout.closeDrawer(mDrawerList);
        }

        NavDrawerItem selectedItem = navConf.getNavItems().get(position);

        onNavItemSelected(selectedItem.getId());
        mDrawerList.setItemChecked(position, true);

        if (selectedItem.updateActionBarTitle()) {
            setTitle(selectedItem.getLabel());
        }

    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(LoginActivity.SESSION_BUNDLE, session);
        outState.putString(LoginActivity.USERNAME_BUNDLE, user);
        super.onSaveInstanceState(outState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String user = bundle.getString(LoginActivity.USERNAME_BUNDLE);
        return new CursorLoader(this,
                Uri.withAppendedPath(UsersTable.CONTENT_URI_ID_USER, user),
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        if (cursor != null) {
            updateNavMenuHeader(cursor);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void updateNavMenuHeader(Cursor cursor) {
        User u = UserHelpers.getUserFromCursor(cursor);
        if (u != null) {
            List<NavDrawerItem> l = navConf.getNavItems();
            l.set(0, NavMenuHeader.getInstance(NavDrawerConstants.PROFILE, u));
            navConf.getBaseAdapter().notifyDataSetChanged();
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    class MyActionBarDrawerToggle extends ActionBarDrawerToggle {

        int alphaStart = -1;

        public MyActionBarDrawerToggle() {
            super(BaseNavDrawerActivity.this, BaseNavDrawerActivity.this.mDrawerLayout, BaseNavDrawerActivity.this.navConf.getDrawerOpenDesc(), BaseNavDrawerActivity.this.navConf.getDrawerCloseDesc());
        }

        @Override
        public void onDrawerClosed(View view) {
//                getActionBar().setTitle(mTitle);
            alphaStart = -1;
            invalidateOptionsMenu();
        }

        @Override
        public void onDrawerOpened(View drawerView) {
//                getActionBar().setTitle(mDrawerTitle);
            invalidateOptionsMenu();
        }

        @Override
        public void onDrawerStateChanged(int newState) {
            super.onDrawerStateChanged(newState);
            if (alphaStart == -1) {
                alphaStart = mActionBarDrawable.getAlpha(); // get initial alpha
            }
        }

        @Override
        public void onDrawerSlide(View drawerView, float slideOffset) {
            super.onDrawerSlide(drawerView, slideOffset);
            if (alphaStart >= 0)
                mActionBarDrawable.setAlpha(((int) Math.max(alphaStart, Math.min(255f, 255f * (slideOffset + alphaStart / 255f))))); // TODO need more smooth!
//            Log.e("ALPHA START=", "" + alphaStart);
        }
    }

    void closeDrawer() {
        if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
            mDrawerLayout.closeDrawer(Gravity.LEFT);
        }
    }
}

