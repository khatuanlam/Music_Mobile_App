package com.example.music_mobile_app.retrofit.mydatabase;

import com.example.music_mobile_app.model.mydatabase.Song;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.GET;

public interface SongService {
    @GET("/Song")
    Call<List<Song>> getAllSongs();
}
