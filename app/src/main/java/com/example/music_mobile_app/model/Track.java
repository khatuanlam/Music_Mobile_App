package com.example.music_mobile_app.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Track {
    @SerializedName("id")
    private String id;
    @SerializedName("artists")
    private List<Artist> artists;
    @SerializedName("album")
    private Album album;
    @SerializedName("name")
    private String name;
    @SerializedName("type")
    private String type;

    @SerializedName("href")
    private String href;
    @SerializedName("genres")
    private List<String> genres;

    public Track(String id, List<Artist> artists, Album album, String name, String type, String href, List<String> genres) {
        this.id = id;
        this.artists = artists;
        this.album = album;
        this.name = name;
        this.type = type;
        this.href = href;
        this.genres = genres;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Artist> getArtists() {
        return artists;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }
}
