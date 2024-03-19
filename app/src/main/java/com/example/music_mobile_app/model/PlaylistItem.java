package com.example.music_mobile_app.model;

import java.util.List;

import kaaes.spotify.webapi.android.models.Image;

public class PlaylistItem {

    public String id;
    public List<Image> images;
    public String name;

    public PlaylistItem(String id, List<Image> images, String name) {
        this.id = id;
        this.images = images;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}
