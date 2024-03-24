package com.example.music_mobile_app.model;

import android.media.Image;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Playlist {
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("followers")
    private Followers followers;
    @SerializedName("type")
    private String type;
    @SerializedName("uri")
    private String uri;
    @SerializedName("images")
    private List<Images> images;
    @SerializedName("tracks")
    private Tracks tracks;
    @SerializedName("public")
    private boolean ispublic;
    @SerializedName("colloborative")
    private Boolean collorative;

    public Playlist(String id, String name, Followers followers, String type, String uri, List<Images> images, Tracks tracks, boolean ispublic, Boolean collorative) {
        this.id = id;
        this.name = name;
        this.followers = followers;
        this.type = type;
        this.uri = uri;
        this.images = images;
        this.tracks = tracks;
        this.ispublic = ispublic;
        this.collorative = collorative;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Followers getFollowers() {
        return followers;
    }

    public void setFollowers(Followers followers) {
        this.followers = followers;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public List<Images> getImages() {
        return images;
    }

    public void setImages(List<Images> images) {
        this.images = images;
    }

    public Tracks getTracks() {
        return tracks;
    }

    public void setTracks(Tracks tracks) {
        this.tracks = tracks;
    }

    public boolean isIspublic() {
        return ispublic;
    }

    public void setIspublic(boolean ispublic) {
        this.ispublic = ispublic;
    }

    public Boolean getCollorative() {
        return collorative;
    }

    public void setCollorative(Boolean collorative) {
        this.collorative = collorative;
    }
}
