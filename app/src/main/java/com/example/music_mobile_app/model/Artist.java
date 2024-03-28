package com.example.music_mobile_app.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Artist {
@SerializedName("id")
    private String id;
@SerializedName("genres")
    private List<String> genres;
@SerializedName("followers")
private Followers followers;
@SerializedName("images")
    private List<Images> images;
@SerializedName("name")
    private String name;
@SerializedName("uri")
    private String uri;
@SerializedName("type")
    private String type;
@SerializedName("href")
    private String href;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public Followers getFollowers() {
        return followers;
    }

    public void setFollowers(Followers followers) {
        this.followers = followers;
    }

    public List<Images> getImages() {
        return images;
    }

    public void setImages(List<Images> images) {
        this.images = images;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public Artist(String id, List<String> genres, Followers followers, List<Images> images, String name, String uri, String type, String href) {
        this.id = id;
        this.genres = genres;
        this.followers = followers;
        this.images = images;
        this.name = name;
        this.uri = uri;
        this.type = type;
        this.href = href;
    }
}