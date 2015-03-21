package com.sample.alexkush.vkphotoviewer.ui.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sample.alexkush.vkphotoviewer.R;
import com.sample.alexkush.vkphotoviewer.model.Album;
import com.sample.alexkush.vkphotoviewer.model.Photo;
import com.sample.alexkush.vkphotoviewer.tools.Converter;
import com.sample.alexkush.vkphotoviewer.tools.GalleryManager;
import com.sample.alexkush.vkphotoviewer.ui.activities.parents.VkActivity;
import com.sample.alexkush.vkphotoviewer.ui.fragments.PhotoFragment;

import java.util.ArrayList;

public class PhotoPagerActivity extends VkActivity implements View.OnClickListener {
    private ArrayList<Photo> mPhotos;
    private Photo mCurrentPhoto;
    private ActionBar mActionBar;

    private ViewPager mViewPager;
    private LinearLayout mLlInfoPanel;
    private TextView mTvPhotoDescription, mTvPhotoDate, mTvPhotoLikesCount;
    private ImageView mIvOpenMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_pager);
        mViewPager = (ViewPager) findViewById(R.id.vpPager);
        mLlInfoPanel = (LinearLayout) findViewById(R.id.llInfoPanel);
        mTvPhotoDescription = (TextView) findViewById(R.id.tvPhotoDescription);
        mTvPhotoDate = (TextView) findViewById(R.id.tvPhotoDate);
        mTvPhotoLikesCount = (TextView) findViewById(R.id.tvPhotoLikesCount);
        mIvOpenMap = (ImageView) findViewById(R.id.ivOpenMap);
        mIvOpenMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(mCurrentPhoto.getLocation());
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });

        Album currentAlbum = GalleryManager.getInstance(this).getCurrentAlbum();
        mPhotos = currentAlbum.getPhotos();
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setTitle(currentAlbum.getTitle());

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setViewPager();
    }

    private void setViewPager() {
        FragmentManager fm = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                Photo photo = mPhotos.get(position);
                return PhotoFragment.newInstance(photo);
            }

            @Override
            public int getCount() {
                return mPhotos.size();
            }
        });
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                setPhotoData(position);
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mViewPager.setOnSystemUiVisibilityChangeListener(
                    new View.OnSystemUiVisibilityChangeListener() {
                        @Override
                        public void onSystemUiVisibilityChange(int vis) {
                            if ((vis & View.SYSTEM_UI_FLAG_LOW_PROFILE) != 0) {
                                mActionBar.hide();
                                mLlInfoPanel.setVisibility(View.GONE);
                            } else {
                                mActionBar.show();
                                mLlInfoPanel.setVisibility(View.VISIBLE);
                            }
                        }
                    });
            mViewPager.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
            mActionBar.hide();
        }

        // TODO equals
        Photo photo = (Photo) getIntent().getSerializableExtra(PhotoFragment.EXTRA_PHOTO);
        for (int i = 0; i < mPhotos.size(); i++) {
            Photo photo2 = mPhotos.get(i);
            if (photo2.getId() == photo.getId()) {
                mViewPager.setCurrentItem(i);
                setPhotoData(i);
            }
        }
    }

    private void setPhotoData(int currentFragmentPosition) {
        Photo photo = mPhotos.get(currentFragmentPosition);
        mCurrentPhoto = photo;
        mTvPhotoDate.setText(Converter.formatDate(photo.getDate(), Converter.TimeUnit.SECOND));
        mTvPhotoDescription.setText(photo.getDescription());
        mTvPhotoLikesCount.setText(String.valueOf(photo.getLikesCount()));
        mActionBar.setSubtitle(String.format("%d из %d", currentFragmentPosition + 1, mPhotos.size()));
        if (photo.hasGeodata())
            mIvOpenMap.setVisibility(View.VISIBLE);
        else
            mIvOpenMap.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            final int vis = mViewPager.getSystemUiVisibility();
            if ((vis & View.SYSTEM_UI_FLAG_LOW_PROFILE) != 0) {
                mViewPager.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            } else {
                mViewPager.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
            }
        }
    }
}
