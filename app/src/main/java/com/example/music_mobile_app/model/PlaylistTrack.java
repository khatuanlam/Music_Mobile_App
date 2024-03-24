package com.example.music_mobile_app.model;

import com.google.gson.annotations.SerializedName;

public class PlaylistTrack {
    @SerializedName("added_at")
    private String  added_at;
    @SerializedName("is_local")
    private boolean is_local;
    @SerializedName("track")
    private Track track;

    public PlaylistTrack(String added_at, boolean is_local, Track track) {
        this.added_at = added_at;
        this.is_local = is_local;
        this.track = track;
    }

    public String getAdded_at() {
        return added_at;
    }

    public void setAdded_at(String added_at) {
        this.added_at = added_at;
    }

    public boolean isIs_local() {
        return is_local;
    }

    public void setIs_local(boolean is_local) {
        this.is_local = is_local;
    }

    public Track getTrack() {
        return track;
    }

    public void setTrack(Track track) {
        this.track = track;
    }
}
