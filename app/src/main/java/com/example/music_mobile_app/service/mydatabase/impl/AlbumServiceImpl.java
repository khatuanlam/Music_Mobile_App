package com.example.music_mobile_app.service.mydatabase.impl;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.music_mobile_app.model.Album;
import com.example.music_mobile_app.network.service.DBRetrofit;
import com.example.music_mobile_app.network.service.RAlbumService;
import com.example.music_mobile_app.service.mydatabase.extension_interface.AlbumService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AlbumServiceImpl implements AlbumService {

    private final RAlbumService albumService;

    public AlbumServiceImpl() {
        Retrofit retrofit = DBRetrofit.getRetrofit();
        this.albumService = retrofit.create(RAlbumService.class);
    }

    @Override
    public LiveData<List<Album>> getAllAlbums() {
        final MutableLiveData<List<Album>> albums = new MutableLiveData<>();
        Call<List<Album>> call = albumService.getAllAlbums();
        call.enqueue(new Callback<List<Album>>() {
            @Override
            public void onResponse(Call<List<Album>> call, Response<List<Album>> response) {
                if (response.isSuccessful()) {
                    albums.setValue(response.body());
                } else {
                    Log.i("getAllAlbums", "LOI");
                    albums.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<List<Album>> call, Throwable t) {
                Log.i("getAllAlbums", "that bai");
                Log.i("getAllAlbums", t.getMessage());
                albums.setValue(null);

            }
        });
        return albums;
    }

    @Override
    public LiveData<Album> getAlbumById(long id) {
        final MutableLiveData<Album> album = new MutableLiveData<>();
        Call<Album> call = albumService.getAlbumById(id);
        call.enqueue(new Callback<Album>() {
            @Override
            public void onResponse(Call<Album> call, Response<Album> response) {
                if (response.isSuccessful()) {
                    album.setValue(response.body());
                } else {
                    Log.i("getAlbumById", "LOI");
                    album.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<Album> call, Throwable t) {
                Log.i("getAlbumById", "that bai");
                Log.i("getAlbumById", t.getMessage());
                album.setValue(null);

            }
        });
        return album;
    }

    @Override
    public LiveData<List<Album>> getTopPopularityAlbums() {
        final MutableLiveData<List<Album>> albums = new MutableLiveData<>();
        Call<List<Album>> call = albumService.getTopPopularAlbums(0, 10);
        call.enqueue(new Callback<List<Album>>() {
            @Override
            public void onResponse(Call<List<Album>> call, Response<List<Album>> response) {
                if (response.isSuccessful()) {
                    albums.setValue(response.body());
                } else {
                    Log.i("getTopPopularityAlbums", "LOI");
                    albums.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<List<Album>> call, Throwable t) {
                Log.i("getTopPopularityAlbums", "that bai");
                Log.i("getTopPopularityAlbums", t.getMessage());
                albums.setValue(null);
            }
        });
        return albums;
    }
}
