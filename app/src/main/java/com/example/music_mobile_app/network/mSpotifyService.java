package com.example.music_mobile_app.network;

import com.example.music_mobile_app.model.User;

import java.util.HashMap;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Pager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class mSpotifyService {
    private static final String BASE_URL = "https://api.spotify.com/v1/";

    private mSpotifyAPI spotifyAPI;

    private String authToken;

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public mSpotifyService(String authToken) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        spotifyAPI = retrofit.create(mSpotifyAPI.class);
        this.authToken = "Bearer" + authToken;
    }

    public void getUserProfile(Callback<User> callback) {
        Call<User> call = spotifyAPI.getUserProfile(this.authToken);
        call.enqueue(callback);
    }

    public void updateUserProfile(User user, Callback<Void> callback) {
        Call<Void> call = spotifyAPI.updateUserProfile(this.authToken, user);
        call.enqueue(callback);
    }

    public void getRecentlyTracks(Callback<Pager<Track>> callback) {
        Map<String, Object> options = new HashMap<>();
        options.put(SpotifyService.LIMIT, 10);
        Call<Pager<Track>> call = spotifyAPI.getRecentlyTracks("Bearer" + this.authToken, options);
        call.enqueue(callback);
    }

//    viết ra đây nè má, cái bên kia là interface thôiản
    //gảnh chỉ t kaays dsach đi :)
    //à ko, dcm 50k
    //ban be dui de
    public Call<Pager<Artist>> getfollowArtist(String id, Callback<Pager<Artist>> callback){
        Call<Pager<Artist>> call = spotifyAPI.followArtist(id,"Bearer" + this.authToken);
        call.enqueue(callback);
        return call;
    }
    public Call<Pager<Artist>> getunfollowArtist(String id, Callback<Pager<Artist>> callback){
        Call<Pager<Artist>> call = spotifyAPI.unfollowArtists(id, "Bearer" + this.authToken);
        call.enqueue(callback);
        return call;
    }

    public Call<Pager<Artist>> getisFollowingArtist(String id, Callback<Pager<Artist>> callback){
        Call<Pager<Artist>> call = spotifyAPI.isFollowingArtists(id, "Bearer" + this.authToken);
        call.enqueue(callback);
        return call;
    }
}


