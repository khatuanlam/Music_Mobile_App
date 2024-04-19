package com.example.music_mobile_app.retrofit.mydatabase;

import com.example.music_mobile_app.model.mydatabase.Album;
import com.example.music_mobile_app.model.mydatabase.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RloginService {
    @POST("/SpotifyLogin/{idSpotify}")
    Call<User> loginWithMyDatabase(@Path("idSpotify") String idSpotify);

}
