package com.sample.alexkush.vkphotoviewer.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.GridLayoutAnimationController;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.sample.alexkush.vkphotoviewer.R;
import com.sample.alexkush.vkphotoviewer.model.Album;
import com.sample.alexkush.vkphotoviewer.model.Photo;
import com.sample.alexkush.vkphotoviewer.parsers.JSONParser;
import com.sample.alexkush.vkphotoviewer.tools.Converter;
import com.sample.alexkush.vkphotoviewer.tools.GalleryManager;
import com.sample.alexkush.vkphotoviewer.ui.activities.PhotoPagerActivity;
import com.squareup.picasso.Picasso;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import java.util.ArrayList;

public class AlbumFragment extends Fragment {
    private static final String TAG = "AlbumFragment";

    private Album mAlbum;

    private GridView mGvPhotos;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAlbum = GalleryManager.getInstance(getActivity()).getCurrentAlbum();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_album, container, false);
        mGvPhotos = (GridView) view.findViewById(R.id.gvItems);
        mGvPhotos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Photo photo = ((PhotosAdapter) mGvPhotos.getAdapter()).getItem(position);
                Intent intent = new Intent(getActivity(), PhotoPagerActivity.class);
                intent.putExtra(PhotoFragment.EXTRA_PHOTO, photo);
                startActivity(intent);
            }
        });
        ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle(mAlbum.getTitle());
        setupAdapter();
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requestPhotos();
    }

    private void setupAdapter() {
        if (getActivity() == null || mGvPhotos == null)
            return;

        if (mAlbum.getPhotos() != null) {
            mGvPhotos.setAdapter(new PhotosAdapter(mAlbum.getPhotos()));
        } else {
            mGvPhotos.setAdapter(null);
        }
        setAnimation();
    }

    private void setAnimation() {
        Animation animation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f);
        animation.setDuration(200);
        GridLayoutAnimationController controller = new GridLayoutAnimationController(
                animation, 0.5f, 0.5f);
        mGvPhotos.setLayoutAnimation(controller);
    }

    private void requestPhotos() {
        VKRequest albumsRequest = new VKRequest("photos.get", VKParameters.from(VKApiConst.USER_ID, mAlbum.getOwnerId(), VKApiConst.ALBUM_ID, mAlbum.getId(), "photo_sizes", 1, "extended", 1));
        albumsRequest.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                Log.d(TAG, response.json.toString());
                mAlbum.setPhotos(JSONParser.parseAlbum(response.json));
                setupAdapter();
            }

            @Override
            public void onError(VKError error) {
                super.onError(error);
            }
        });
    }

    private class PhotosAdapter extends ArrayAdapter<Photo> {

        public PhotosAdapter(ArrayList<Photo> photos) {
            super(getActivity(), 0, photos);
        }

        private class ViewHolder {
            ImageView image;
            TextView date;
            TextView title;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = getActivity().getLayoutInflater().inflate(R.layout.photo_item, parent, false);
                holder.image = (ImageView) convertView.findViewById(R.id.ivPhoto);
                holder.date = (TextView) convertView.findViewById(R.id.tvPhotoDate);
                holder.title = (TextView) convertView.findViewById(R.id.tvPhotoName);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Photo photo = getItem(position);
            holder.image.setImageDrawable(null);
            holder.date.setText(Converter.formatDate(photo.getDate(), Converter.TimeUnit.SECOND));
            holder.title.setText(photo.getDescription());
            Picasso.with(getActivity()).load(photo.getThumbSrc()).into(holder.image);

            return convertView;
        }
    }
}
