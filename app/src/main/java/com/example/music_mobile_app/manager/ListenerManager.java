package com.example.music_mobile_app.manager;

import kaaes.spotify.webapi.android.models.Album;
import kaaes.spotify.webapi.android.models.Artist;

public class ListenerManager {
    public interface AlbumCompleteListener {
        void onComplete(Album album);

        void onError(Throwable error);
    }
    public interface ArtistCompleteListener {
        void onComplete(Artist artist);
        void onError(Throwable error);
    }

    public static interface TrackAdapterListener {
        void onDeleteTrackClicked(String trackId);
        int findTrackPositionById(String trackId);
    }
    public static interface AlbumAdapterListener {
        void onDeleteAlbumClicked(String albumId);
        int findAlbumPositionById(String albumID);
    }
}
