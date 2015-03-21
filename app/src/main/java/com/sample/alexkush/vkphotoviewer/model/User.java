package com.sample.alexkush.vkphotoviewer.model;

import org.json.JSONException;
import org.json.JSONObject;

public class User {
    private static final String JSON_ID = "id";
    private static final String JSON_FIRST_NAME = "first_name";
    private static final String JSON_LAST_NAME = "last_name";
    private static final String JSON_AVATAR_SRC = "photo_100";

    private int mId;
    private String mFirstName;
    private String mLastName;
    private String mAvatarSrc;

    public User(JSONObject json) throws JSONException {
        mId = json.getInt(JSON_ID);
        mFirstName = json.getString(JSON_FIRST_NAME);
        mLastName = json.getString(JSON_LAST_NAME);
        mAvatarSrc = json.getString(JSON_AVATAR_SRC);
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getFirstName() {
        return mFirstName;
    }

    public void setFirstName(String firstName) {
        mFirstName = firstName;
    }

    public String getLastName() {
        return mLastName;
    }

    public void setLastName(String lastName) {
        mLastName = lastName;
    }

    public String getAvatarSrc() {
        return mAvatarSrc;
    }

    public void setAvatarSrc(String avatarSrc) {
        mAvatarSrc = avatarSrc;
    }

    public String getName() {
        return mFirstName + " " + mLastName;
    }
}
