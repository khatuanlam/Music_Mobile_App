package com.example.music_mobile_app.manager;

import com.spotify.android.appremote.api.SpotifyAppRemote;

import java.util.List;

import kaaes.spotify.webapi.android.models.Album;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Track;

public class ListenerManager {
    public interface AlbumCompleteListener {
        void onComplete(Album album, List<Track> trackList);

        void onError(Throwable error);
    }

    public interface OnConnectionCompleteListener {
        void onConnected();

        void onFailure(Throwable throwable);
    }

    public interface ArtistCompleteListener {
        void onComplete(Artist artist);

        void onError(Throwable error);
    }

    public interface PlaylistCompleteListener {
        void onComplete();

        void onError(Throwable error);
    }

    public interface ListTrackOnCompleteListener {
        void onComplete(List<Track> trackList);

        void onError(Throwable error);
    }
}
