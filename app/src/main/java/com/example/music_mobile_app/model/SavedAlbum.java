package com.example.music_mobile_app.model;

import com.google.gson.annotations.SerializedName;

public class SavedAlbum {
    @SerializedName("added_at")
    private String added_at;
    @SerializedName("album")
    private Album album;

    public String getAdded_at() {
        return added_at;
    }

    public void setAdded_at(String added_at) {
        this.added_at = added_at;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public SavedAlbum(String added_at, Album album) {
        this.added_at = added_at;
        this.album = album;
    }
}
