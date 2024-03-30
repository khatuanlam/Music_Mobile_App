package com.example.music_mobile_app.retrofit.mydatabase;

import com.example.music_mobile_app.model.mydatabase.Song;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SongService {
    @GET("/Song")
    Call<List<Song>> getAllSongs();

    @GET("/Song/{id}")
    Call<Song> getSongById(@Path("id") long id);

    @GET("/Song/Album/{id}")
    Call<List<Song>> getAllSongsFromAlbum(@Path("id") long id);

    @GET("/Song/Playlist/{id}")
    Call<List<Song>> getAllSongsFromPlaylist(@Path("id") long id);
    @GET("/Song/TopPopularity")
    Call<List<Song>> getTopPopularSongs(@Query("page") int page, @Query("size") int size);
}
