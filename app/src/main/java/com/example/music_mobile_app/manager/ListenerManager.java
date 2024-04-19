package com.example.music_mobile_app.manager;

import android.widget.Button;

import com.spotify.android.appremote.api.SpotifyAppRemote;

import java.util.List;
import java.util.Objects;

import kaaes.spotify.webapi.android.models.Album;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistSimple;
import kaaes.spotify.webapi.android.models.Playlist;
import kaaes.spotify.webapi.android.models.PlaylistSimple;
import kaaes.spotify.webapi.android.models.Track;

public class ListenerManager {
    public interface AlbumCompleteListener {
        void onComplete(Album album, List<Track> trackList);

        void onError(Throwable error);
    }

    public interface OnGetCompleteListener {
        void onComplete(boolean type);

        void onError(Throwable error);
    }

    public interface OnCreatePlaylistCompleteListener {
        void onComplete(Playlist playlist);

        void onError(Throwable error);
    }

    public interface ListTrackOnCompleteListener {
        void onComplete(List<Track> trackList);

        void onError(Throwable error);
    }

    public interface OnFollowCompleteListener {
        void setFollowButtonState(Button button, boolean status);

        void initializeFollowButtonState(String artistId);

        void followArtist(String artistId);

        void unfollowArtists(String artistId);
    }

    public interface OnGetPlaylistCompleteListener {
        void onComplete(List<PlaylistSimple> playlistSimpleList);

        void onError(Throwable error);
    }
}
