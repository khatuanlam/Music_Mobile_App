package com.example.music_mobile_app.repository.mydatabase.impl;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.music_mobile_app.model.mydatabase.Song;
import com.example.music_mobile_app.repository.mydatabase.SongRepository;
import com.example.music_mobile_app.retrofit.mydatabase.MyDbRetrofit;
import com.example.music_mobile_app.retrofit.mydatabase.SongService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SongRepositoryImpl implements SongRepository {

    private Retrofit retrofit;
    public SongRepositoryImpl()
    {
        this.retrofit = MyDbRetrofit.getRetrofit();
    }
    private MutableLiveData<List<Song>> songListLiveData = new MutableLiveData<>();

    @Override
    public LiveData<List<Song>> getAllSongs() {
        SongService songService = retrofit.create(SongService.class);
        Call<List<Song>> call = songService.getAllSongs();
        call.enqueue(new Callback<List<Song>>() {
            @Override
            public void onResponse(Call<List<Song>> call, Response<List<Song>> response) {
                if (response.isSuccessful()) {
                    songListLiveData.setValue(response.body());
                } else {
                    Log.i("GET ALL SONG","LOI");
                }
            }

            @Override
            public void onFailure(Call<List<Song>> call, Throwable t) {
                Log.i("GET ALL SONG","that bai");
                Log.i("GET ALL SONG",t.getMessage());
            }
        });
        return songListLiveData;
    }
}
