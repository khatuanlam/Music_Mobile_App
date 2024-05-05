package com.example.music_mobile_app.manager;

import kaaes.spotify.webapi.android.models.Album;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Playlist;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.TrackSimple;
import kaaes.spotify.webapi.android.models.UserPrivate;

public class VariableManager {
    private static VariableManager variableManager;
    private Album album;
    private Playlist playlist;
    private Artist artist;
    private UserPrivate user;

    private Track track;

    public String baseImage = "https://i.scdn.co/image/ab67616d00001e02ff9ca10b55ce82ae553c8228";

    public static VariableManager getVariableManager() {
        return variableManager;
    }

    public static void setVariableManager(VariableManager variableManager) {
        VariableManager.variableManager = variableManager;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    public void setPlaylist(Playlist playlist) {
        this.playlist = playlist;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

    public VariableManager() {
        album = new Album();
        playlist = new Playlist();
        artist = new Artist();
        user = new UserPrivate();
    }

    public UserPrivate getUser() {
        return user;
    }

    public void setUser(UserPrivate user) {
        this.user = user;
    }

    public static VariableManager getInstance() {
        if (variableManager == null) {
            variableManager = new VariableManager();
        }
        return variableManager;
    }

    public Track getTrack() {
        return track;
    }

    public void setTrack(Track track) {
        this.track = track;
    }

}
