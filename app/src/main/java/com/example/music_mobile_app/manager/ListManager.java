package com.example.music_mobile_app.manager;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.models.AlbumSimple;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Playlist;
import kaaes.spotify.webapi.android.models.PlaylistSimple;
import kaaes.spotify.webapi.android.models.PlaylistTrack;
import kaaes.spotify.webapi.android.models.Track;

public class ListManager {

    private static ListManager listManager;

    private List<PlaylistSimple> playlistList;

    private List<Track> favoriteTracks;

    private List<Track> recommendTracks;

    private List<Track> topTracks;
    private List<AlbumSimple> albums;
    private List<Track> albumTracks;

    private List<Artist> followArtists;

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
        favoriteTracks = new ArrayList<>();
        recommendTracks = new ArrayList<>();
        topTracks = new ArrayList<>();
        albums = new ArrayList<>();
        albumTracks = new ArrayList<>();
        followArtists = new ArrayList<>();
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


    public void setPlaylistList(List<PlaylistSimple> playlistList) {
        this.playlistList = playlistList;
    }

    public List<Track> getTopTracks() {
        if (topTracks == null) {
            topTracks = new ArrayList<>();
        }
        return topTracks;
    }

    public void setTopTracks(List<Track> topTracks) {
        this.topTracks = topTracks;
    }

    public List<Track> getFavoriteTracks() {
        return favoriteTracks;
    }

    public void setFavoriteTracks(List<Track> favoriteTracks) {
        this.favoriteTracks = favoriteTracks;
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

    public List<Artist> getFollowArtists() {
        return followArtists;
    }

    public void setFollowArtists(List<Artist> followArtists) {
        if (followArtists == null) {
            followArtists = new ArrayList<>();
        }
        this.followArtists = followArtists;
    }

    public void clear() {
        setAlbumTracks(null);
        setTopTracks(null);
        setFavoriteTracks(null);
        setAlbumTracks(null);
        setFollowArtists(null);
        setPlaylistList(null);
    }
}
