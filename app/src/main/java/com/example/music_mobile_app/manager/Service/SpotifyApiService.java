package com.example.music_mobile_app.manager.Service;

import com.example.music_mobile_app.model.Album;
import com.example.music_mobile_app.model.Albums;
import com.example.music_mobile_app.model.Artist;

import com.example.music_mobile_app.model.Page;
import com.example.music_mobile_app.model.Playlist;
import com.example.music_mobile_app.model.SavedAlbum;
import com.example.music_mobile_app.model.SavedTrack;
import com.example.music_mobile_app.model.Track;
import com.example.music_mobile_app.model.Tracks;


//import kaaes.spotify.webapi.android.models.Track;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

import retrofit2.http.*;


public interface SpotifyApiService {

    // ALBUM
    @GET("albums/{id}")
    Call<Album> getAlbum( @Path("id") String albumId);
    @GET("albums/{id}/tracks")
    Call <Page<Track>> getTrackFromAlbum(@Path("id")String albumID);
    @GET("me/albums")
    Call<Page<SavedAlbum>> getMyAlbum(@Query("limit") int limit, @Query("offset") int offset);

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


    @DELETE("me/tracks")
    Call<Void> deleteTrack(@Query("ids") String TrackId);
    @DELETE("me/tracks")
    Call<Void> deleteTracks(@Body Map<String, Object> body);
    @DELETE("me/albums")
    Call<Void> removeFromMyAlbums(@Query("ids") String ids, @Body List<String> albumIds);


    @PUT("me/tracks")
    Call<Void> addToMyTracks(@Query("ids") String ids, @Body List<String> trackIds);
    @PUT("me/albums")
    Call<Void> addToMyAlbums(@Query("ids") String ids, @Body List<String> albumIds);



}

