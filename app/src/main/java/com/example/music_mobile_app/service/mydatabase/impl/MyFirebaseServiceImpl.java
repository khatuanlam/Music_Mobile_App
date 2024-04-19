package com.example.music_mobile_app.service.mydatabase.impl;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.music_mobile_app.model.mydatabase.Album;
import com.example.music_mobile_app.model.mydatabase.Playlist;
import com.example.music_mobile_app.retrofit.mydatabase.MyDbRetrofit;
import com.example.music_mobile_app.retrofit.mydatabase.RAlbumService;
import com.example.music_mobile_app.retrofit.mydatabase.RMyFirebaseService;
import com.example.music_mobile_app.service.mydatabase.myinterface.AlbumService;
import com.example.music_mobile_app.service.mydatabase.myinterface.MyFirebaseService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MyFirebaseServiceImpl implements MyFirebaseService {

    private RMyFirebaseService rMyFirebaseService;

    public MyFirebaseServiceImpl() {
        Retrofit retrofit = MyDbRetrofit.getRetrofit();
        this.rMyFirebaseService = retrofit.create(RMyFirebaseService.class);
    }

    @Override
    public Boolean postToken(String token) {
        Call<Boolean> call = rMyFirebaseService.postToken(token);
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()) {
                    Log.i("POST TOKEN", "success");
                } else {
                    Log.i("POST TOKEN", "un-success");
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.i("POST TOKEN", "failed");
            }
        });
        return true;
    }
}
