package com.example.music_mobile_app.backend;

import com.example.music_mobile_app.backend.model.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SpotifyService {
    private static final String BASE_URL = "https://api.spotify.com/v1/";

    private SpotifyAPI spotifyAPI;

    public SpotifyService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        spotifyAPI = retrofit.create(SpotifyAPI.class);
    }

    public void getUserProfile(String authToken, Callback<User> callback) {
        Call<User> call = spotifyAPI.getUserProfile("Bearer " + authToken);
        call.enqueue(callback);
    }
    public void updateUserProfile(String authToken, User user, Callback<Void> callback) {
        Call<Void> call = spotifyAPI.updateUserProfile("Bearer " + authToken, user);
        call.enqueue(callback);
    }

}