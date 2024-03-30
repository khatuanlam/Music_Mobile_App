package com.example.music_mobile_app.retrofit.mydatabase;

import com.example.music_mobile_app.model.mydatabase.Album;
import com.example.music_mobile_app.model.mydatabase.Playlist;
import com.example.music_mobile_app.model.mydatabase.Song;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface PlaylistService {
    @GET("/Playlist")
    Call<List<Playlist>> getAllPlaylists();

    @GET("/Playlist/{id}")
    Call<Playlist> getPlaylistById(@Path("id") long id);
}
