package com.example.music_mobile_app.retrofit.mydatabase;

import com.example.music_mobile_app.model.mydatabase.Album;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RAlbumService {
    @GET("/Album")
    Call<List<Album>> getAllAlbums();

    @GET("/Album/{id}")
    Call<Album> getAlbumById(@Path("id") long id);

    @GET("/Album/Top")
    Call<List<Album>> getTopPopularAlbums(@Query("page") int page, @Query("size") int size);
}
