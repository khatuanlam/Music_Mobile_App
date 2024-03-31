package com.example.music_mobile_app.manager;

import java.util.List;

import kaaes.spotify.webapi.android.models.Album;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Track;

public class ListenerManager {
    public interface AlbumCompleteListener {
        void onComplete(Album album);

        void onError(Throwable error);
    }
    public interface ArtistCompleteListener {
        void onComplete(Artist artist);
        void onError(Throwable error);
    }
}
