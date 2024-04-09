package com.example.music_mobile_app.manager;

import android.widget.Button;

import com.spotify.android.appremote.api.SpotifyAppRemote;

import java.util.List;

import kaaes.spotify.webapi.android.models.Album;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistSimple;
import kaaes.spotify.webapi.android.models.Track;

public class ListenerManager {
    public interface AlbumCompleteListener {
        void onComplete(Album album, List<Track> trackList);

        void onError(Throwable error);
    }

    public interface ArtistCompleteListener {
        void onComplete(Artist artist);

        void onError(Throwable error);
    }

    public interface OnClickCompleteListener {
        void onItemClicked(String itemId);

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
}
