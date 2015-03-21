package com.sample.alexkush.vkphotoviewer.ui.fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sample.alexkush.vkphotoviewer.R;
import com.sample.alexkush.vkphotoviewer.model.Photo;
import com.sample.alexkush.vkphotoviewer.tools.StorageUtils;
import com.sample.alexkush.vkphotoviewer.ui.activities.PhotoPagerActivity;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PhotoFragment extends Fragment {
    private static final String TAG = "PhotoFragment";

    public static final String EXTRA_PHOTO = "com.sample.alexkush.vkphotoviewer.ui.fragments.PhotoFragment.EXTRA_PHOTO";

    private Photo mPhoto;

    private ImageView mIvPhoto;

    public static PhotoFragment newInstance(Photo photo) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_PHOTO, photo);
        PhotoFragment fragment = new PhotoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);

        if (getArguments() != null)
            mPhoto = (Photo) getArguments().getSerializable(EXTRA_PHOTO);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo, container, false);
        mIvPhoto = (ImageView) view.findViewById(R.id.ivPhoto);
        mIvPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((PhotoPagerActivity) getActivity()).onClick(v);
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Picasso.with(getActivity()).load(mPhoto.getFullSrc()).priority(Picasso.Priority.HIGH).into(mIvPhoto);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.photo_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
            case R.id.menu_share:
                sharePhoto();
                return true;
            case R.id.menu_clear_shared:
                StorageUtils.deleteFolder(new File(Environment.getExternalStorageDirectory() + File.separator + StorageUtils.SHARE_FOLDER));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void sharePhoto() {
        new AsyncTask<Void, Void, Void>() {
            private ProgressDialog mProgressDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mProgressDialog = new ProgressDialog(getActivity());
                mProgressDialog.setTitle(getResources().getString(R.string.share_image));
                mProgressDialog.setMessage(getResources().getString(R.string.processing));
                mProgressDialog.setCancelable(false);
                mProgressDialog.show();
            }

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    if (StorageUtils.isExternalStorageWritable()) {
                        mIvPhoto.buildDrawingCache();
                        Bitmap bitmap = mIvPhoto.getDrawingCache();

                        Intent share = new Intent(Intent.ACTION_SEND);
                        share.setType("image/jpeg");
                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

                        File shared = StorageUtils.createFile(Environment.getExternalStorageDirectory() + File.separator + StorageUtils.SHARE_FOLDER, mPhoto.getId() + ".jpeg");
                        FileOutputStream fo = new FileOutputStream(shared);
                        fo.write(bytes.toByteArray());
                        fo.flush();
                        fo.close();
                        share.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///" + shared.getPath()));
                        startActivityForResult(Intent.createChooser(share, getResources().getString(R.string.share_image)), 1);
                        shared.deleteOnExit();
                    }
                } catch (IOException e) {
                    Log.e(TAG, "IOException: ", e);
                } catch (StorageUtils.FileAccessException e) {
                    Log.e(TAG, "FileAccessException: ", e);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                mProgressDialog.dismiss();
            }
        }.execute();
    }
}
