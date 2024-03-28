package com.example.music_mobile_app.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Objects;

public class Album {



    @SerializedName("id")
    private String id;
    @SerializedName("total_tracks")
    private Integer total_tracks;
    @SerializedName("name")
    private String name;
    @SerializedName("album_type")
    private String album_type;
    @SerializedName("artists")
    private List<Artist> artists;
    @SerializedName("tracks")
    private Page<Track> tracks;
    @SerializedName("images")
    private List<Images> imageUrl;
    @SerializedName("href")
    private String href;
    @SerializedName("release_date")
    private String release_date;
    @SerializedName("type")
    private String type;
    @SerializedName("genres")
    private List<String> genres;

    public Album(String id, Integer total_tracks, String name, String album_type, List<Artist> artists, Page<Track> tracks, List<Images> imageUrl, String href, String release_date, String type, List<String> genres) {
        this.id = id;
        this.total_tracks = total_tracks;
        this.name = name;
        this.album_type = album_type;
        this.artists = artists;
        this.tracks = tracks;
        this.imageUrl = imageUrl;
        this.href = href;
        this.release_date = release_date;
        this.type = type;
        this.genres = genres;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getTotal_tracks() {
        return total_tracks;
    }

    public void setTotal_tracks(Integer total_tracks) {
        this.total_tracks = total_tracks;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlbum_type() {
        return album_type;
    }

    public void setAlbum_type(String album_type) {
        this.album_type = album_type;
    }

    public List<Artist> getArtists() {
        return artists;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }

    public Page<Track> getTracks() {
        return tracks;
    }

    public void setTracks(Page<Track> tracks) {
        this.tracks = tracks;
    }

    public List<Images> getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(List<Images> imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }
}
