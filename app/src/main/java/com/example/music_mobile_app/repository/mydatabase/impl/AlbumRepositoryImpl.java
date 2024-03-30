package com.example.music_mobile_app.repository.mydatabase.impl;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.music_mobile_app.model.mydatabase.Album;
import com.example.music_mobile_app.repository.mydatabase.AlbumRepository;
import com.example.music_mobile_app.retrofit.mydatabase.MyDbRetrofit;
import com.example.music_mobile_app.retrofit.mydatabase.AlbumService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AlbumRepositoryImpl implements AlbumRepository {

    private Retrofit retrofit;
    public AlbumRepositoryImpl()
    {
        this.retrofit = MyDbRetrofit.getRetrofit();
    }
    private MutableLiveData<List<Album>> albumListLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Album>> popular_albumListLiveData = new MutableLiveData<>();
    private MutableLiveData<Album> albumById = new MutableLiveData<>();

    @Override
    public LiveData<List<Album>> getAllAlbums() {
        AlbumService albumService = retrofit.create(AlbumService.class);
        Call<List<Album>> call = albumService.getAllAlbums();
        call.enqueue(new Callback<List<Album>>() {
            @Override
            public void onResponse(Call<List<Album>> call, Response<List<Album>> response) {
                if (response.isSuccessful()) {
                    albumListLiveData.setValue(response.body());
                } else {
                    Log.i("getAllAlbums","LOI");
                }
            }

            @Override
            public void onFailure(Call<List<Album>> call, Throwable t) {
                Log.i("getAllAlbums","that bai");
                Log.i("getAllAlbums",t.getMessage());
            }
        });
        return albumListLiveData;
    }

    @Override
    public LiveData<Album> getAlbumById(long id) {
        AlbumService albumService = retrofit.create(AlbumService.class);
        Call<Album> call = albumService.getAlbumById(id);
        call.enqueue(new Callback<Album>() {
            @Override
            public void onResponse(Call<Album> call, Response<Album> response) {
                if (response.isSuccessful()) {
                    albumById.setValue(response.body());
                } else {
                    Log.i("getAlbumById","LOI");
                }
            }

            @Override
            public void onFailure(Call<Album> call, Throwable t) {
                Log.i("getAlbumById","that bai");
                Log.i("getAlbumById",t.getMessage());
            }
        });
        return albumById;
    }

    @Override
    public LiveData<List<Album>> getTopPopularityAlbums() {
        AlbumService albumService = retrofit.create(AlbumService.class);
        Call<List<Album>> call = albumService.getTopPopularAlbums(0, 10);
        call.enqueue(new Callback<List<Album>>() {
            @Override
            public void onResponse(Call<List<Album>> call, Response<List<Album>> response) {
                if (response.isSuccessful()) {
                    popular_albumListLiveData.setValue(response.body());
                } else {
                    Log.i("getTopPopularityAlbums","LOI");
                }
            }

            @Override
            public void onFailure(Call<List<Album>> call, Throwable t) {
                Log.i("getTopPopularityAlbums","that bai");
                Log.i("getTopPopularityAlbums",t.getMessage());
            }
        });
        return popular_albumListLiveData;
    }
}
