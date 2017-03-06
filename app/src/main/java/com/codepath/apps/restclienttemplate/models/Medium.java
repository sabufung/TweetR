package com.codepath.apps.restclienttemplate.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by BuuPV on 3/5/2017.
 */
@Parcel
public class Medium {
    public Integer id;
    public String idStr;
    public List<Integer> indices = null;
    public String mediaUrl;
    public String mediaUrlHttps;
    public String url;
    public String displayUrl;
    public String expandedUrl;
    public String type;

    public Medium() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIdStr() {
        return idStr;
    }

    public void setIdStr(String idStr) {
        this.idStr = idStr;
    }

    public List<Integer> getIndices() {
        return indices;
    }

    public void setIndices(List<Integer> indices) {
        this.indices = indices;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public String getMediaUrlHttps() {
        return mediaUrlHttps;
    }

    public void setMediaUrlHttps(String mediaUrlHttps) {
        this.mediaUrlHttps = mediaUrlHttps;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDisplayUrl() {
        return displayUrl;
    }

    public void setDisplayUrl(String displayUrl) {
        this.displayUrl = displayUrl;
    }

    public String getExpandedUrl() {
        return expandedUrl;
    }

    public void setExpandedUrl(String expandedUrl) {
        this.expandedUrl = expandedUrl;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public Medium(JSONObject object) {
        super();

        try {
            this.id = object.getInt("id");
            this.type = object.getString("type");
            this.mediaUrl = object.getString("media_url");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static List<Medium> fromJson(JSONArray array) {
        ArrayList<Medium> medium = new ArrayList<Medium>(array.length());

        for (int i = 0; i < array.length(); i++) {
            JSONObject mediaJson = null;
            try {
                mediaJson = array.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }

            Medium media = new Medium(mediaJson);
//            tweet.save();
            medium.add(media);
        }

        return medium;
    }
}
