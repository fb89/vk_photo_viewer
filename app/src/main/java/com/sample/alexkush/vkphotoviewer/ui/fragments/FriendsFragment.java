package com.sample.alexkush.vkphotoviewer.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.GridLayoutAnimationController;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.sample.alexkush.vkphotoviewer.R;
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

public class FriendsFragment extends Fragment {
    private static final String TAG = "FriendsFragment";

    private User mMyUser;
    private ArrayList<User> mFriends;

    private ImageView mIvAvatar;
    private TextView mTvMyName;
    private GridView mGvFriends;

    private Callbacks mCallbacks;

    public interface Callbacks {
        void onUserSelected(User user);
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMyUser = GalleryManager.getInstance(getActivity()).getMyUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_albums, container, false);
        mIvAvatar = (ImageView) view.findViewById(R.id.ivAvatar);
        mIvAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMyUser != null)
                    mCallbacks.onUserSelected(mMyUser);
            }
        });
        mTvMyName = (TextView) view.findViewById(R.id.tvUserName);
        mGvFriends = (GridView) view.findViewById(R.id.gvItems);
        mGvFriends.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User user = ((UsersAdapter) mGvFriends.getAdapter()).getItem(position);
                mCallbacks.onUserSelected(user);
            }
        });
        setupAdapter();
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mMyUser != null) {
            setMyUserUI();
            requestFriends();
        } else {
            requestMyUserData();
        }
    }

    private void setupAdapter() {
        if (getActivity() == null || mGvFriends == null)
            return;

        if (mFriends != null) {
            mGvFriends.setAdapter(new UsersAdapter(mFriends));
        } else {
            mGvFriends.setAdapter(null);
        }
        setAnimation();
    }

    private void setAnimation() {
        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(500);
        GridLayoutAnimationController controller = new GridLayoutAnimationController(
                animation, 0.5f, 0.5f);
        mGvFriends.setLayoutAnimation(controller);
    }

    private void setMyUserUI() {
        mMyUser = GalleryManager.getInstance(getActivity()).getMyUser();
        mTvMyName.setText(mMyUser.getName());
        Picasso.with(getActivity()).load(mMyUser.getAvatarSrc()).priority(Picasso.Priority.HIGH).into(mIvAvatar);
    }

    private void requestFriends() {
        VKRequest userDataRequest = VKApi.friends().get(VKParameters.from(VKApiConst.USER_ID, mMyUser.getId(), VKApiConst.FIELDS, "photo_100"));
        userDataRequest.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                Log.d(TAG, response.json.toString());
                mFriends = JSONParser.parseUsers(response.json);
                setupAdapter();
            }
        });
    }

    private void requestMyUserData() {
        VKRequest userDataRequest = VKApi.users().get(VKParameters.from(VKApiConst.FIELDS, "photo_100"));
        userDataRequest.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                Log.d(TAG, response.json.toString());
                mMyUser = JSONParser.parseUser(response.json);
                if (mMyUser != null) {
                    GalleryManager.getInstance(getActivity()).setMyUser(mMyUser);
                    setMyUserUI();
                    requestFriends();
                }
            }
        });
    }

    private class UsersAdapter extends ArrayAdapter<User> {

        public UsersAdapter(ArrayList<User> users) {
            super(getActivity(), 0, users);
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
            User user = getItem(position);
            holder.image.setImageDrawable(null);
            holder.title.setText(user.getName());
            Picasso.with(getActivity()).load(user.getAvatarSrc()).into(holder.image);

            return convertView;
        }
    }
}
