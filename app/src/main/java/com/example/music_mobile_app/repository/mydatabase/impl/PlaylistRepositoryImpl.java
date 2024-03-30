package com.example.music_mobile_app.repository.mydatabase.impl;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.music_mobile_app.model.mydatabase.Playlist;
import com.example.music_mobile_app.repository.mydatabase.PlaylistRepository;
import com.example.music_mobile_app.retrofit.mydatabase.MyDbRetrofit;
import com.example.music_mobile_app.retrofit.mydatabase.PlaylistService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PlaylistRepositoryImpl implements PlaylistRepository {

    private Retrofit retrofit;
    public PlaylistRepositoryImpl()
    {
        this.retrofit = MyDbRetrofit.getRetrofit();
    }
    private MutableLiveData<List<Playlist>> playlistListLiveData = new MutableLiveData<>();

    private MutableLiveData<Playlist> playlistById = new MutableLiveData<>();

    @Override
    public LiveData<List<Playlist>> getAllPlaylists() {
        PlaylistService playlistService = retrofit.create(PlaylistService.class);
        Call<List<Playlist>> call = playlistService.getAllPlaylists();
        call.enqueue(new Callback<List<Playlist>>() {
            @Override
            public void onResponse(Call<List<Playlist>> call, Response<List<Playlist>> response) {
                if (response.isSuccessful()) {
                    playlistListLiveData.setValue(response.body());
                } else {
                    Log.i("getAllPlaylists","LOI");
                }
            }

            @Override
            public void onFailure(Call<List<Playlist>> call, Throwable t) {
                Log.i("getAllPlaylists","that bai");
                Log.i("getAllPlaylists",t.getMessage());
            }
        });
        return playlistListLiveData;
    }

    @Override
    public LiveData<Playlist> getPlaylistById(long id) {
        PlaylistService playlistService = retrofit.create(PlaylistService.class);
        Call<Playlist> call = playlistService.getPlaylistById(id);
        call.enqueue(new Callback<Playlist>() {
            @Override
            public void onResponse(Call<Playlist> call, Response<Playlist> response) {
                if (response.isSuccessful()) {
                    playlistById.setValue(response.body());
                } else {
                    Log.i("getPlaylistById","LOI");
                }
            }

            @Override
            public void onFailure(Call<Playlist> call, Throwable t) {
                Log.i("getPlaylistById","that bai");
                Log.i("getPlaylistById",t.getMessage());
            }
        });
        return playlistById;
    }


}
