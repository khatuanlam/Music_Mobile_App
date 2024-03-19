package com.example.music_mobile_app.manager.Service;

import com.example.music_mobile_app.manager.AuthManager.constant.ConstantVariable;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

public class RetrofitClient {
    private static final String BASE_URL = "https://api.spotify.com/v1/";
    private static String authToken = ConstantVariable.ACCESS_TOKEN; // Biến để lưu trữ token

    public static void setAuthToken(String token) {
        authToken = token;
    }

    public SpotifyApiService getClient() {
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
        return retrofit.create(SpotifyApiService.class);
    }
}
