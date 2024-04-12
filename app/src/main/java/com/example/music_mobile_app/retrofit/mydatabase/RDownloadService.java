package com.example.music_mobile_app.retrofit.mydatabase;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RDownloadService {

    @GET("/BaiHat/{name}")
    Call<ResponseBody> downloadMp3(@Path("name") String fileName);
}
