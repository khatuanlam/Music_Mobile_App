package com.example.music_mobile_app.model;

import android.os.Parcel;
import android.os.Parcelable;

public class SongPlaylist {
    private String trackName;
    private String imageUrl;

    public SongPlaylist(String trackName, String imageUrl) {
        this.trackName = trackName;
        this.imageUrl = imageUrl;
    }

    public String getTrackName() {
        return trackName;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}