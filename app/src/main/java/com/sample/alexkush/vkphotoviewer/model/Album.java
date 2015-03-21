package com.sample.alexkush.vkphotoviewer.model;

import com.sample.alexkush.vkphotoviewer.parsers.JSONParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Album {
    private static final String JSON_ID = "id";
    private static final String JSON_THUMB_ID = "thumb_id";
    private static final String JSON_OWNER_ID = "owner_id";
    private static final String JSON_SIZE = "size";
    private static final String JSON_TITLE = "title";

    private int mId, mThumbId, mOwnerId;
    private int mSize;
    private String mTitle;
    private String mThumbSrc;

    private ArrayList<Photo> mPhotos;

    public Album(JSONObject json) throws JSONException {
        mId = json.getInt(JSON_ID);
        mThumbId = json.getInt(JSON_THUMB_ID);
        mOwnerId = json.getInt(JSON_OWNER_ID);
        mSize = json.getInt(JSON_SIZE);
        mTitle = json.getString(JSON_TITLE);
        mThumbSrc = JSONParser.getImageUrl(json.getJSONArray("sizes"), JSONParser.Sizes.X);
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public int getThumbId() {
        return mThumbId;
    }

    public void setThumbId(int thumbId) {
        mThumbId = thumbId;
    }

    public int getOwnerId() {
        return mOwnerId;
    }

    public void setOwnerId(int ownerId) {
        mOwnerId = ownerId;
    }

    public int getSize() {
        return mSize;
    }

    public void setSize(int size) {
        mSize = size;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getThumbSrc() {
        return mThumbSrc;
    }

    public void setThumbSrc(String thumbSrc) {
        mThumbSrc = thumbSrc;
    }

    public ArrayList<Photo> getPhotos() {
        return mPhotos;
    }

    public void setPhotos(ArrayList<Photo> photos) {
        mPhotos = photos;
    }
}
