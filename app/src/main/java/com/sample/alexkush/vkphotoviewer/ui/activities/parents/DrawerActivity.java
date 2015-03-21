package com.sample.alexkush.vkphotoviewer.ui.activities.parents;

import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.MenuItem;

import com.sample.alexkush.vkphotoviewer.R;

public abstract class DrawerActivity extends VkActivity {

    protected Fragment mDrawerFragment;
    protected Fragment mMainFragment;

    protected DrawerLayout mDrawer;
    protected ActionBarDrawerToggle mDrawerToggle;

    protected abstract Fragment createDrawerFragment();

    protected abstract Fragment createMainFragment();

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.fragment_drawer);
        FragmentManager fm = getSupportFragmentManager();
        mMainFragment = fm.findFragmentById(R.id.flMainContainer);
        if (mMainFragment == null) {
            mMainFragment = createMainFragment();
            fm.beginTransaction().add(R.id.flMainContainer, mMainFragment)
                    .commit();
        }
        mDrawerFragment = fm.findFragmentById(R.id.flDrawerContainer);
        if (mDrawerFragment == null) {
            mDrawerFragment = createDrawerFragment();
            fm.beginTransaction().add(R.id.flDrawerContainer, mDrawerFragment)
                    .commit();
        }
        initDrawer();
    }

    private void initDrawer() {
        mDrawer = (DrawerLayout) findViewById(R.id.dlDrawer);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawer, R.drawable.ic_drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.setDrawerListener(mDrawerToggle);
        mDrawer.setDrawerShadow(R.drawable.drawer_shadow, Gravity.START);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle != null && mDrawerToggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }

    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (mDrawerToggle != null)
            mDrawerToggle.syncState();
    }
}
