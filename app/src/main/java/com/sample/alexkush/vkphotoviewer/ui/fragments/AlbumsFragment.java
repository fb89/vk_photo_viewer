package com.sample.alexkush.vkphotoviewer.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.sample.alexkush.vkphotoviewer.model.User;
import com.sample.alexkush.vkphotoviewer.parsers.JSONParser;
import com.sample.alexkush.vkphotoviewer.tools.GalleryManager;
import com.squareup.picasso.Picasso;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import java.util.ArrayList;

public class AlbumsFragment extends Fragment {
    private static final String TAG = "AlbumsFragment";

    public static final String EXTRA_USER_ID = "com.sample.alexkush.vkphotoviewer.ui.fragments.AlbumsFragment.EXTRA_USER_ID";
    public static final int MY_USER_ID = -1;

    private int mUserId = MY_USER_ID;
    private User mUser;
    private ArrayList<Album> mAlbums;

    private ImageView mIvAvatar;
    private TextView mTvUserName;
    private GridView mGvAlbums;

    private Callbacks mCallbacks;

    public interface Callbacks {
        void onAlbumSelected(Album album);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallbacks = (Callbacks) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    public static AlbumsFragment newInstance(int userId) {
        Bundle args = new Bundle();
        args.putInt(EXTRA_USER_ID, userId);
        AlbumsFragment fragment = new AlbumsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            mUserId = getArguments().getInt(EXTRA_USER_ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_albums, container, false);
        mIvAvatar = (ImageView) view.findViewById(R.id.ivAvatar);
        mTvUserName = (TextView) view.findViewById(R.id.tvUserName);
        mGvAlbums = (GridView) view.findViewById(R.id.gvItems);
        mGvAlbums.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Album album = ((AlbumsAdapter) mGvAlbums.getAdapter()).getItem(position);
                mCallbacks.onAlbumSelected(album);
            }
        });
        setupAdapter();
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requestUserData();
    }

    private void setupAdapter() {
        if (getActivity() == null || mGvAlbums == null)
            return;

        if (mAlbums != null) {
            mGvAlbums.setAdapter(new AlbumsAdapter(mAlbums));
        } else {
            mGvAlbums.setAdapter(null);
        }
        setAnimation();
    }

    private void setAnimation() {
        Animation animation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f);
        animation.setDuration(200);
        GridLayoutAnimationController controller = new GridLayoutAnimationController(
                animation, 0.5f, 0.5f);
        mGvAlbums.setLayoutAnimation(controller);
    }

    private void requestUserData() {
        User currentUser = GalleryManager.getInstance(getActivity()).getCurrentUser();
        if (currentUser != null && currentUser.getId() == mUserId) {
            mUser = currentUser;
            setUserUI();
            requestAlbums();
        } else {
            VKRequest userDataRequest;
            if (mUserId == MY_USER_ID)
                userDataRequest = VKApi.users().get(VKParameters.from(VKApiConst.FIELDS, "photo_100"));
            else
                userDataRequest = VKApi.users().get(VKParameters.from(VKApiConst.USER_ID, mUserId, VKApiConst.FIELDS, "photo_100"));

            userDataRequest.executeWithListener(new VKRequest.VKRequestListener() {
                @Override
                public void onComplete(VKResponse response) {
                    super.onComplete(response);
                    Log.d(TAG, response.json.toString());
                    mUser = JSONParser.parseUser(response.json);
                    if (mUser != null) {
                        if (mUserId == MY_USER_ID) {
                            GalleryManager.getInstance(getActivity()).setMyUser(mUser);
                            GalleryManager.getInstance(getActivity()).setCurrentUser(mUser);
                        } else
                            GalleryManager.getInstance(getActivity()).setCurrentUser(mUser);
                        setUserUI();
                        requestAlbums();
                    }
                }
            });
        }
    }

    private void setUserUI() {
        mTvUserName.setText(mUser.getName());
        Picasso.with(getActivity()).load(mUser.getAvatarSrc()).priority(Picasso.Priority.HIGH).into(mIvAvatar);
    }

    private void requestAlbums() {
        VKRequest albumsRequest = new VKRequest("photos.getAlbums", VKParameters.from("need_covers", 1, "photo_sizes", 1, "owner_id", mUser.getId()));
        albumsRequest.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                Log.d(TAG, response.json.toString());
                mAlbums = JSONParser.parseAlbums(response.json);
                setupAdapter();
            }
        });
    }

    private class AlbumsAdapter extends ArrayAdapter<Album> {

        public AlbumsAdapter(ArrayList<Album> albums) {
            super(getActivity(), 0, albums);
        }

        private class ViewHolder {
            ImageView image;
            TextView title;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = getActivity().getLayoutInflater().inflate(R.layout.album_item, parent, false);
                holder.image = (ImageView) convertView.findViewById(R.id.ivAlbum);
                holder.title = (TextView) convertView.findViewById(R.id.tvAlbumName);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Album album = getItem(position);
            holder.image.setImageDrawable(null);
            holder.title.setText(album.getTitle());
            Picasso.with(getActivity()).load(album.getThumbSrc()).into(holder.image);

            return convertView;
        }
    }
}
