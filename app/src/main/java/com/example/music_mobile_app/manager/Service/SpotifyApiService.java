package com.example.music_mobile_app.manager.Service;

import com.example.music_mobile_app.model.Album;
import com.example.music_mobile_app.model.Artist;

import com.example.music_mobile_app.model.Track;


//import kaaes.spotify.webapi.android.models.Track;
import retrofit2.Call;

import retrofit2.http.*;


public interface SpotifyApiService {
    @GET("albums/{id}")
    Call<Album> getAlbum(@Header("Authorization") String token, @Path("id") String albumId);
    @GET("artists/{id}")
    Call<Artist> getArtist(@Path("id") String artistId, @Header("Authorization") String authToken);
    @GET("tracks/{id}")
    Call<Track> getTrack(@Path("id") String trackId);

}

