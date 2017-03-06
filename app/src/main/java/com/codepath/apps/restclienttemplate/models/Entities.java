package com.codepath.apps.restclienttemplate.models;

import com.codepath.apps.restclienttemplate.MyDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by BuuPV on 3/4/2017.
 */
@Parcel
public class Entities extends BaseModel {
    public List<Medium> media = null;

    public Entities() {
        super();
    }

    public Entities(JSONObject object){
        super();

        try {
            this.media = Medium.fromJson(object.getJSONArray("media"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public List<Medium> getMedia() {
        return media;
    }

    public void setMedia(List<Medium> media) {
        this.media = media;
    }



}
