package com.example.music_mobile_app.network.service;

import com.example.music_mobile_app.model.extension.User;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RloginService {
    @GET("/SpotifyLogin/{idSpotify}")
    Call<User> loginWithMyDatabase(@Path("idSpotify") String idSpotify);

}
