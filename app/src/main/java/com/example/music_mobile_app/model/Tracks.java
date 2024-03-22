package com.example.music_mobile_app.model;




import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Tracks  {
    private List<Track> tracks;
    public List<Track> getTracks() {
        return tracks;
    }
    public void setTracks(List<Track> tracks) {
        this.tracks = tracks;
    }
}
