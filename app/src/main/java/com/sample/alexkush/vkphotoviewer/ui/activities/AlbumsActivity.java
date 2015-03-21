package com.sample.alexkush.vkphotoviewer.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.sample.alexkush.vkphotoviewer.R;
import com.sample.alexkush.vkphotoviewer.model.Album;
import com.sample.alexkush.vkphotoviewer.model.User;
import com.sample.alexkush.vkphotoviewer.tools.GalleryManager;
import com.sample.alexkush.vkphotoviewer.ui.activities.parents.DrawerActivity;
import com.sample.alexkush.vkphotoviewer.ui.fragments.AlbumsFragment;
import com.sample.alexkush.vkphotoviewer.ui.fragments.FriendsFragment;

public class AlbumsActivity extends DrawerActivity implements AlbumsFragment.Callbacks, FriendsFragment.Callbacks {

    @Override
    protected Fragment createDrawerFragment() {
        return new FriendsFragment();
    }

    @Override
    protected Fragment createMainFragment() {
        int userId = getIntent().getIntExtra(AlbumsFragment.EXTRA_USER_ID, AlbumsFragment.MY_USER_ID);
        return AlbumsFragment.newInstance(userId);
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        GalleryManager.getInstance(this).setCurrentAlbum(null);
    }

    @Override
    public void onAlbumSelected(Album album) {
        GalleryManager.getInstance(this).setCurrentAlbum(album);
        Intent intent = new Intent(this, AlbumActivity.class);
        startActivity(intent);
    }

    @Override
    public void onUserSelected(User user) {
        if (mDrawer != null)
            mDrawer.closeDrawers();
        GalleryManager.getInstance(this).setCurrentUser(user);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment oldUserAlbums = fm.findFragmentById(R.id.flMainContainer);
        Fragment newUserAlbums = AlbumsFragment.newInstance(user.getId());
        if (oldUserAlbums != null)
            ft.remove(oldUserAlbums);
        ft.add(R.id.flMainContainer, newUserAlbums);
        ft.commit();
    }
}
