package com.example.music_mobile_app.service.mydatabase.impl;

import android.util.Log;

import com.example.music_mobile_app.network.service.DBRetrofit;
import com.example.music_mobile_app.network.service.RMyFirebaseService;
import com.example.music_mobile_app.service.mydatabase.extension_interface.MyFirebaseService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MyFirebaseServiceImpl implements MyFirebaseService {

    private RMyFirebaseService rMyFirebaseService;

    public MyFirebaseServiceImpl() {
        Retrofit retrofit = DBRetrofit.getRetrofit();
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

