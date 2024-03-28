package com.example.music_mobile_app.model;

import com.google.gson.annotations.SerializedName;

public class SavedTrack {
    @SerializedName("added_at")
    private String added_at;
    @SerializedName("track")
    private Track track;

    public SavedTrack(String added_at, Track track) {
        this.added_at = added_at;
        this.track = track;
    }

    public String getAdded_at() {
        return added_at;
    }

    public void setAdded_at(String added_at) {
        this.added_at = added_at;
    }

    public Track getTrack() {
        return track;
    }

    public void setTrack(Track track) {
        this.track = track;
    }
}
