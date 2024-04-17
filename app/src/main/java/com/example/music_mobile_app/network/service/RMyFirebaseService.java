package com.example.music_mobile_app.network.service;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RMyFirebaseService {
    @POST("/FirebaseToken/{token}")
    Call<Boolean> postToken(@Path("token") String token);
}
