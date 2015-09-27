package com.ttn.googlehackathon.activity;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.ttn.googlehackathon.R;
import com.ttn.googlehackathon.adaptor.NavDrawerListAdapter;
import com.ttn.googlehackathon.models.NavDrawerItem;
import com.ttn.googlehackathon.utility.AppConstant;

import java.util.ArrayList;

public class HomeNavigationActiviy extends ActionBarActivity {


    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;
    private TypedArray navMenuIconsActive;
    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private boolean loggedInDrawer;
    private CharSequence mDrawerTitle;
    private int fragmentFlag;
    private boolean isPressedAgain;
    private Toolbar mToolbar;
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_navigation);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mTitle=getTitle();
        mDrawerTitle = getTitle();
        makeDrawer();

    }

    private void makeDrawer() {

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

        // load slide menu items
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

        // nav drawer icons from resources
        navMenuIcons = getResources().obtainTypedArray(R.array.nav_drawer_icons);
        navMenuIconsActive = getResources().obtainTypedArray(R.array.nav_drawer_icons_active);


        if (navDrawerItems == null)
            navDrawerItems = new ArrayList<NavDrawerItem>();
        else
            navDrawerItems.clear();

        // adding nav drawer items to array
        // Home
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0],
                navMenuIcons.getResourceId(0, -1), navMenuIconsActive.getResourceId(0, -1)));
        // Find People
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1],
                navMenuIcons.getResourceId(1, -1), navMenuIconsActive.getResourceId(1, -1)));

//                if (isLoggedIn())
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2],
                navMenuIcons.getResourceId(2, -1), navMenuIconsActive.getResourceId(2, -1)));

        navMenuIcons.recycle();

        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

        // setting the nav drawer list adapter
        adapter = new NavDrawerListAdapter(getApplicationContext(), navDrawerItems);
        mDrawerList.setAdapter(adapter);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.ic_drawer, //nav menu toggle icon
                R.string.app_name, // nav drawer open - description for accessibility
                R.string.app_name // nav drawer close - description for accessibility
        ) {
            public void onDrawerClosed(View view) {
                getSupportActionBar().setTitle(mTitle);
                // calling onPrepareOptionsMenu() to show action bar icons
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getSupportActionBar().setTitle(mDrawerTitle);
                // calling onPrepareOptionsMenu() to hide action bar icons
                invalidateOptionsMenu();
            }
        };

        adapter.selectItem(1);

        mDrawerLayout.setDrawerListener(mDrawerToggle);

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (isLoggedIn() && !loggedInDrawer) {
            makeDrawer();
        }
    }

    @Override
    public void onBackPressed() {

        if (fragmentFlag == 2) {
            if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
                mDrawerLayout.closeDrawer(mDrawerList);
                return;
            }
            if (!isPressedAgain) {
                Toast.makeText(HomeNavigationActiviy.this, "Press back again to exit.", Toast.LENGTH_LONG).show();
                isPressedAgain = true;
            } else {
                super.onBackPressed();
            }
        } else {
            adapter.selectItem(1);
            displayView(1);
        }
    }

    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            Toast.makeText(this, "" + getResources().getString(R.string.message_network_not_available), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    boolean isLoggedIn() {
        SharedPreferences sharedPreference = getSharedPreferences(AppConstant.PREF_LOGIN_FILE_NAME, MODE_PRIVATE);
        return !sharedPreference.getString("user_id", "-1").equalsIgnoreCase("-1");
    }

    private void displayView(int position) {
        if (isLoggedIn()) {
            loggedInDrawer = true;
        }
        if (!isLoggedIn() &&
                position == 6) {
            position = position + 1;
        }
        // update the main content by replacing fragments
        Fragment fragment = null;
        switch (position) {
            case 0:
                SharedPreferences sharedPreference = getSharedPreferences(AppConstant.PREF_LOGIN_FILE_NAME, MODE_PRIVATE);
                if (sharedPreference.getString("user_id", "-1").equalsIgnoreCase("-1")) {
                    Intent intent2 = new Intent(HomeNavigationActiviy.this, LoginActivity.class);
                    intent2.putExtra("from", "Drawer");
                    startActivity(intent2);
                    return;
                } else {
                    Intent intent2 = new Intent(HomeNavigationActiviy.this, MyProfileScreen.class);
                    startActivity(intent2);
                }
                mDrawerLayout.closeDrawer(mDrawerList);
                break;
            case 7:
                Intent intent1 = new Intent(HomeNavigationActiviy.this, AboutAppActivity.class);
                intent1.putExtra("url", "http://google.com/");
                startActivity(intent1);
                mDrawerLayout.closeDrawer(mDrawerList);
                break;
            case 9:
                mDrawerLayout.closeDrawer(mDrawerList);
                break;
        }
    }

    private class SlideMenuClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // display view for selected nav drawer item
            adapter.selectItem(position);
            if (!isOnline())
                return;
            displayView(position);
        }
    }
}