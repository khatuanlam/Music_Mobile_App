package com.example.music_mobile_app.network;

import com.example.music_mobile_app.model.User;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class mSpotifyAPI {
    private static final String BASE_URL = "https://localhost:8080";

    private mSpotifyService spotifyAPI;

    private String authToken;

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public mSpotifyAPI(String authToken) {

        // Khởi tạo retrofit client
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        // Thêm HttpLoggingInterceptor để log ra các yêu cầu API
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        httpClient.addInterceptor(loggingInterceptor);

        httpClient.addInterceptor(chain -> {
            Request original = chain.request();
            Request.Builder requestBuilder = original.newBuilder().header("Authorization", "Bearer " + authToken).method(original.method(), original.body());

            // Loại bỏ việc mã hóa các ký tự đặc biệt trong URL
            String url = original.url().toString();
            url = url.replace("%3F", "?").replace("%3D", "=");
            Request request = requestBuilder.url(url).build();

            return chain.proceed(request);
        });

        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).client(httpClient.build()).build();

        spotifyAPI = retrofit.create(mSpotifyService.class);
    }

}