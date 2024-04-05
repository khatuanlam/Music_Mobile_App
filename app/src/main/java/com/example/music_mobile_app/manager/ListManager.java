package com.example.music_mobile_app.manager;


import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.models.AlbumSimple;
import kaaes.spotify.webapi.android.models.Playlist;
import kaaes.spotify.webapi.android.models.PlaylistSimple;
import kaaes.spotify.webapi.android.models.PlaylistTrack;
import kaaes.spotify.webapi.android.models.Track;

public class ListManager {

    private static ListManager listManager;

    private List<PlaylistSimple> playlistList;
    private List<PlaylistTrack> playlistTracks;


    private List<Track> recentlyTracks;

    private List<Track> recommendTracks;

    private List<Track> topTracks;
    private List<AlbumSimple> albums;
    private List<Track> albumTracks;


    public List<AlbumSimple> getAlbums() {
        return albums;
    }

    public List<Track> getAlbumTracks() {
        return albumTracks;
    }

    public void setAlbumTracks(List<Track> albumTracks) {
        this.albumTracks = albumTracks;
    }

    public static ListManager getInstance() {
        if (listManager == null) {
            listManager = new ListManager();
        }
        return listManager;
    }

    public ListManager() {
        playlistList = new ArrayList<>();
        playlistTracks = new ArrayList<>();
        recentlyTracks = new ArrayList<>();
        recommendTracks = new ArrayList<>();
        topTracks = new ArrayList<>();
        albums = new ArrayList<>();
        albumTracks = new ArrayList<>();
    }

    public List<AlbumSimple> getFavoriteAlbums() {
        return albums;
    }

    public void setAlbums(List<AlbumSimple> albums) {
        this.albums = albums;
    }

    public List<PlaylistSimple> getPlaylistList() {
        return playlistList;
    }
    public List<PlaylistTrack> getPlaylistTracks() {return playlistTracks;}

    public void setPlaylistList(List<PlaylistSimple> playlistList) {
        this.playlistList = playlistList;
    }

    public void setPlaylistTracks(List<PlaylistTrack> playlistTracks) {
        this.playlistTracks = playlistTracks;
    }

    public static void setListManager(ListManager listManager) {
        ListManager.listManager = listManager;
    }

    public List<Track> getTopTracks() {
        return topTracks;
    }

    public void setTopTracks(List<Track> topTracks) {
        this.topTracks = topTracks;
    }


    public List<Track> getRecentlyTracks() {
        return recentlyTracks;
    }

    public void setRecentlyTracks(List<Track> recentlyTracks) {
        this.recentlyTracks = recentlyTracks;
    }

    public List<Track> getRecommendTracks() {
        return recommendTracks;
    }

    public void setRecommendTracks(List<Track> recommendTracks) {
        this.recommendTracks = recommendTracks;
    }

    public static ListManager getListManager() {
        return listManager;
    }

}
