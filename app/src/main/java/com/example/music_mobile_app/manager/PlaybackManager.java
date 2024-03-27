package com.example.music_mobile_app.manager;

import android.os.Parcelable;

import com.example.music_mobile_app.MainActivity;

import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;

public class PlaybackManager {

    private static PlaybackManager manager;

    private Track item;

    private Parcelable state;

    private SpotifyService spotifyService;

    public static PlaybackManager getInstance() {
        if (manager == null) {
            manager = new PlaybackManager();
        }
        return manager;
    }

    public static void setManager(PlaybackManager manager) {
        PlaybackManager.manager = manager;
    }

    public Track getItem() {
        return item;
    }

    public void setItem(Track item) {
        this.item = item;
    }

    public Parcelable getState() {
        return state;
    }

    public void setState(Parcelable state) {
        this.state = state;
    }
}
