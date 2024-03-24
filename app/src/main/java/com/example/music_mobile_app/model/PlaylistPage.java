package com.example.music_mobile_app.model;

public class PlaylistPage {

    public Page<Playlist> PlaylistPage;
    public PlaylistPage(Page<Playlist> playlistPage) {
        PlaylistPage = playlistPage;
    }

    public Page<Playlist> getPlaylistPage() {
        return PlaylistPage;
    }

    public void setPlaylistPage(Page<Playlist> playlistPage) {
        PlaylistPage = playlistPage;
    }
}
