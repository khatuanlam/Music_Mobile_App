package com.example.music_mobile_app.network;

import com.example.music_mobile_app.manager.PlaybackManager;
import com.example.music_mobile_app.model.User;

import java.util.Map;

import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.Track;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.QueryMap;

public interface mSpotifyAPI {

    @GET("me")
    Call<User> getUserProfile(@Header("Authorization") String accessToken);

    @PUT("me")
    Call<Void> updateUserProfile(
            @Header("Authorization") String authToken,
            @Body User user
    );

    @GET("me/player")
    Call<PlaybackManager> getPlayer();

    @PUT("me/player/play")
    Call<Void> startPlayer();

    @PUT("me/player/pause")
    Call<Void> pausePlayer();

    @POST("me/player/next")
    Call<Void> skipToNext();

    @POST("me/player/previous")
    Call<Void> skipToPrevious();

    @PUT("me/player/repeat")
    Call<Void> setRepeat();

    @GET("me/player/currently-playing")
    Call<Track> getCurrentTrack();

    @GET("me/player/recently-played")
    Call<Pager<Track>> getRecentlyTracks(@Header("Authorization") String accessToken, @QueryMap Map<String, Object> var1);

    @GET("/me/following/contains")
    boolean isFollowingArtists();

}