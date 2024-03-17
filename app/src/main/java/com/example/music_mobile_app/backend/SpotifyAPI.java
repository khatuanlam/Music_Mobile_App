package com.example.music_mobile_app.backend;

import com.example.music_mobile_app.backend.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PUT;

public interface SpotifyAPI {
    @GET("me")
    Call<User> getUserProfile(@Header("Authorization") String accessToken);

    @PUT("me")
    Call<Void> updateUserProfile(
            @Header("Authorization") String authToken,
            @Body User user
    );
}