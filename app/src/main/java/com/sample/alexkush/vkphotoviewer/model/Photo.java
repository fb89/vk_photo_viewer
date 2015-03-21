package com.sample.alexkush.vkphotoviewer.model;

import android.net.Uri;

import com.sample.alexkush.vkphotoviewer.parsers.JSONParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Photo implements Serializable {
    private static final String JSON_ID = "id";
    private static final String JSON_ALBUM_ID = "album_id";
    private static final String JSON_OWNER_ID = "owner_id";
    private static final String JSON_DATE = "date";
    private static final String JSON_DESCRIPTION = "text";
    private static final String JSON_LIKES = "likes";
    private static final String JSON_COUNT = "count";
    private static final String JSON_LAT = "lat";
    private static final String JSON_LONG = "long";

    private int mId, mAlbumId, mOwnerId;
    private int mLikesCount;
    private long mDate;
    private double mLat, mLong;
    private String mDescription;
    private String mThumbSrc, mFullSrc;
    // TODO geo

    public Photo(JSONObject json) throws JSONException {
        mId = json.getInt(JSON_ID);
        mAlbumId = json.getInt(JSON_ALBUM_ID);
        mOwnerId = json.getInt(JSON_OWNER_ID);
        mDate = json.getLong(JSON_DATE);
        if (json.has(JSON_LAT))
            mLat = json.getDouble(JSON_LAT);
        if (json.has(JSON_LONG))
            mLong = json.getDouble(JSON_LONG);
        mDescription = json.getString(JSON_DESCRIPTION);
        mThumbSrc = JSONParser.getImageUrl(json.getJSONArray("sizes"), JSONParser.Sizes.X);
        mFullSrc = JSONParser.getImageUrl(json.getJSONArray("sizes"), JSONParser.Sizes.W);
        mLikesCount = json.getJSONObject(JSON_LIKES).getInt(JSON_COUNT);
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public int getAlbumId() {
        return mAlbumId;
    }

    public void setAlbumId(int albumId) {
        mAlbumId = albumId;
    }

    public int getOwnerId() {
        return mOwnerId;
    }

    public void setOwnerId(int ownerId) {
        mOwnerId = ownerId;
    }

    public int getLikesCount() {
        return mLikesCount;
    }

    public void setLikesCount(int likesCount) {
        mLikesCount = likesCount;
    }

    public long getDate() {
        return mDate;
    }

    public void setDate(long date) {
        mDate = date;
    }

    public double getLat() {
        return mLat;
    }

    public void setLat(double lat) {
        mLat = lat;
    }

    public double getLong() {
        return mLong;
    }

    public void setLong(double aLong) {
        mLong = aLong;
    }

    public boolean hasGeodata() {
        return (mLat > 0 && mLong > 0) ? true : false;
    }

    public Uri getLocation(){
        return Uri.parse(String.format("geo:%s,%s",mLat,mLong));
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getThumbSrc() {
        return mThumbSrc;
    }

    public String getFullSrc() {
        return mFullSrc;
    }

    public void setThumbSrc(String thumbSrc) {
        mThumbSrc = thumbSrc;
    }
}
