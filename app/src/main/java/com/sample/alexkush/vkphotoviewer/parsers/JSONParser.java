package com.sample.alexkush.vkphotoviewer.parsers;

import com.sample.alexkush.vkphotoviewer.model.Album;
import com.sample.alexkush.vkphotoviewer.model.Photo;
import com.sample.alexkush.vkphotoviewer.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JSONParser {
    private static final String RESPONSE = "response";

    public static ArrayList<Album> parseAlbums(JSONObject albumsObj) {
        ArrayList<Album> albums = new ArrayList<>();
        try {
            JSONArray array = albumsObj.getJSONObject(RESPONSE).getJSONArray("items");
            for (int i = 0; i < array.length(); i++)
                albums.add(new Album(array.getJSONObject(i)));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return albums;
    }

    public static ArrayList<Photo> parseAlbum(JSONObject albumObj) {
        ArrayList<Photo> photos = new ArrayList<>();
        try {
            JSONArray array = albumObj.getJSONObject(RESPONSE).getJSONArray("items");
            for (int i = 0; i < array.length(); i++)
                photos.add(new Photo(array.getJSONObject(i)));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return photos;
    }

    public static ArrayList<User> parseUsers(JSONObject albumsObj) {
        ArrayList<User> users = new ArrayList<>();
        try {
            JSONArray array = albumsObj.getJSONObject(RESPONSE).getJSONArray("items");
            for (int i = 0; i < array.length(); i++)
                users.add(new User(array.getJSONObject(i)));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return users;
    }

    public static User parseUser(JSONObject usersObj) {
        User user = null;
        try {
            JSONArray array = usersObj.getJSONArray(RESPONSE);
            user = new User(array.getJSONObject(0));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return user;
    }

    public enum Sizes {
        S("s"),
        M("m"),
        X("x"),
        O("o"),
        P("p"),
        Q("q"),
        Y("y"),
        Z("z"),
        W("w");

        String mSize;

        Sizes(String size) {
            mSize = size;
        }

        String getSize() {
            return mSize;
        }
    }

    public static String getImageUrl(JSONArray sizesArray, Sizes maxSize) {
        Sizes[] sizesAll = Sizes.values();
        int maxSizeIndex = 0;
        for (int i = sizesAll.length - 1; i > 0; i--) {
            Sizes currentSize = sizesAll[i];
            if (currentSize == maxSize)
                maxSizeIndex = i;
        }

        for (int i = maxSizeIndex; i > 0; i--) {
            Sizes currentSize = sizesAll[i];
            for (int j = 0; j < sizesArray.length(); j++) {
                try {
                    JSONObject jsonObject = sizesArray.getJSONObject(j);
                    if (jsonObject.getString("type").equals(currentSize.getSize()))
                        return jsonObject.getString("src");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return "https://vk.com/images/camera_200.gif";
    }
}
