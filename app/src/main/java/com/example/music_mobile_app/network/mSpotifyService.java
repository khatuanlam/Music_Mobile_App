package com.example.music_mobile_app.network;

import com.example.music_mobile_app.model.User;

import java.util.HashMap;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Pager;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
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

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        // Thêm HttpLoggingInterceptor để log ra các yêu cầu API
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.addInterceptor(loggingInterceptor);

        httpClient.addInterceptor(chain -> {
            Request original = chain.request();
            Request.Builder requestBuilder = original.newBuilder()
                    .header("Authorization", "Bearer " + authToken)
                    .method(original.method(), original.body());

            // Loại bỏ việc mã hóa các ký tự đặc biệt trong URL
            String url = original.url().toString();
            url = url.replace("%3F", "?").replace("%3D", "=");
            Request request = requestBuilder.url(url).build();

            return chain.proceed(request);
        });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();

        spotifyAPI = retrofit.create(mSpotifyAPI.class);
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
        Call<Pager<Track>> call = spotifyAPI.getRecentlyTracks(this.authToken, options);
        call.enqueue(callback);
    }


}


