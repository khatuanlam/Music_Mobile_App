package com.example.music_mobile_app.manager.Service;

import com.example.music_mobile_app.model.Album;
import com.example.music_mobile_app.model.Albums;
import com.example.music_mobile_app.model.Artist;

import com.example.music_mobile_app.model.Page;
import com.example.music_mobile_app.model.Playlist;
import com.example.music_mobile_app.model.SavedTrack;
import com.example.music_mobile_app.model.Track;
import com.example.music_mobile_app.model.Tracks;


//import kaaes.spotify.webapi.android.models.Track;
import java.util.List;

import retrofit2.Call;

import retrofit2.http.*;


public interface SpotifyApiService {

    // ALBUM
    @GET("albums/{id}")
    Call<Album> getAlbum( @Path("id") String albumId);
    @GET("albums/{id}/tracks")
    Call <Page<Track>> getTrackFromAlbum(@Path("id")String albumID);



    // ARTISTS
    @GET("artists/{id}")
    Call<Artist> getArtist(@Path("id") String artistId);
    @GET("artists/{id}/top-tracks")
    Call <Tracks> getArtistTopTrack(@Path("id") String artistId);

    @GET("artists/{id}/albums")
    Call<Album> getArtistAlbums(@Path("id") String artistId);
    @GET("me/tracks")
    Call<Page<SavedTrack>> getMyTrack(@Query("limit") int limit, @Query("offset") int offset);

    //TRACK
    @GET("tracks/{id}")
    Call<Track> getTrack(@Path("id") String trackId);
    @GET("tracks/")
    Call<Tracks> getTracks(@Query("ids") String trackId);

    @GET("playlists/{id}")
    Call<Playlist> getPlaylist(
            @Path("id") String playlistId,
            @Query("fields") String fields
    );
    @GET("playlists/{id}/tracks")
    Call<Page<Track>> getPlaylistTracks(
            @Path("id") String playlistId,
            @Query("fields") String fields
    );
}

