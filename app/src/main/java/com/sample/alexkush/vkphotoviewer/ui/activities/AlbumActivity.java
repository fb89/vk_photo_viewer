package com.sample.alexkush.vkphotoviewer.ui.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.sample.alexkush.vkphotoviewer.R;
import com.sample.alexkush.vkphotoviewer.model.Album;
import com.sample.alexkush.vkphotoviewer.tools.GalleryManager;
import com.sample.alexkush.vkphotoviewer.ui.activities.parents.DrawerActivity;
import com.sample.alexkush.vkphotoviewer.ui.fragments.AlbumFragment;
import com.sample.alexkush.vkphotoviewer.ui.fragments.AlbumsFragment;

public class AlbumActivity extends DrawerActivity implements AlbumsFragment.Callbacks {

    @Override
    protected Fragment createDrawerFragment() {
        int userId = GalleryManager.getInstance(this).getCurrentUser().getId();
        return AlbumsFragment.newInstance(userId);
    }

    @Override
    protected Fragment createMainFragment() {
        return new AlbumFragment();
    }

    @Override
    public void onAlbumSelected(Album album) {
        if (mDrawer != null)
            mDrawer.closeDrawers();
        GalleryManager.getInstance(this).setCurrentAlbum(album);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment oldAlbum = fm.findFragmentById(R.id.flMainContainer);
        Fragment newAlbum = new AlbumFragment();
        if (oldAlbum != null)
            ft.remove(oldAlbum);
        ft.add(R.id.flMainContainer, newAlbum);
        ft.commit();
    }
}
