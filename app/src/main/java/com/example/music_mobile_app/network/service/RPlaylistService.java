package com.example.music_mobile_app.network.service;

import com.example.music_mobile_app.model.mydatabase.Playlist;
import com.example.music_mobile_app.retrofit.mydatabase.model.AddPlaylistBody;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RPlaylistService {
    @GET("/Playlist")
    Call<List<Playlist>> getAllPlaylists();

    @DELETE("/Playlist/DeleteSong/{idPlaylist}/{idSong}")
    Call<Playlist> deleteSongFromPlaylist(@Path("idPlaylist") long idPlaylist, @Path("idSong") long idSong);

    @POST("/Playlist/AddSong/{idPlaylist}/{idSong}")
    Call<Playlist> postSongToPlaylist(@Path("idPlaylist") long idPlaylist, @Path("idSong") long idSong);

    @POST("/Playlist/{idUser}")
    Call<Playlist> addPlaylistToUser(@Path("idUser") long idUser, @Body AddPlaylistBody addPlaylistBody);

    @GET("/Playlist")
    Call<List<Playlist>> getAllPlaylistsByIdUser(@Query("idUser") long id);

    @GET("/Playlist/{id}")
    Call<Playlist> getPlaylistById(@Path("id") long id);
}
