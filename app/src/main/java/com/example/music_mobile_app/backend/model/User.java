package com.example.music_mobile_app.backend.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class User {
    @SerializedName("display_name")
    private String displayName;

    @SerializedName("images")
    private List<UserImage> images;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public List<UserImage> getImages() {
        return images;
    }

    public void setImages(List<UserImage> images) {
        this.images = images;
    }

}