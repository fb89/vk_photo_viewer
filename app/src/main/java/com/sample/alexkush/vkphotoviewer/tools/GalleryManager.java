package com.sample.alexkush.vkphotoviewer.tools;

import android.content.Context;

import com.sample.alexkush.vkphotoviewer.model.Album;
import com.sample.alexkush.vkphotoviewer.model.User;

public class GalleryManager {
    private static GalleryManager sGalleryManager;

    private Context mAppContext;
    private User mMyUser;
    private User mCurrentUser;
    private Album mCurrentAlbum;

    private GalleryManager(Context appContext) {
        mAppContext = appContext;
    }

    public static GalleryManager getInstance(Context context) {
        if (sGalleryManager == null)
            sGalleryManager = new GalleryManager(context.getApplicationContext());
        return sGalleryManager;
    }

    public User getMyUser() {
        return mMyUser;
    }

    public void setMyUser(User myUser) {
        mMyUser = myUser;
    }

    public User getCurrentUser() {
        return mCurrentUser;
    }

    public void setCurrentUser(User currentUser) {
        mCurrentUser = currentUser;
    }

    public Album getCurrentAlbum() {
        return mCurrentAlbum;
    }

    public void setCurrentAlbum(Album currentAlbum) {
        mCurrentAlbum = currentAlbum;
    }
}
