package com.example.music_mobile_app.service.mydatabase.impl;

import android.util.Log;

import com.example.music_mobile_app.model.mydatabase.User;
import com.example.music_mobile_app.retrofit.mydatabase.MyDbRetrofit;
import com.example.music_mobile_app.retrofit.mydatabase.RloginService;
import com.example.music_mobile_app.service.mydatabase.myinterface.LoginCallback;
import com.example.music_mobile_app.service.mydatabase.myinterface.LoginService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginServiceImpl implements LoginService {

    private final RloginService rloginService;

    public LoginServiceImpl() {
        Retrofit retrofit = MyDbRetrofit.getRetrofit();
        this.rloginService = retrofit.create(RloginService.class);
    }

    @Override
    public void loginWithMyDatabase(String idSpofity, LoginCallback callback) {
        User user = null;
        Call<User> call = rloginService.loginWithMyDatabase(idSpofity);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    Log.i("MLogin Service", "successfull");
                    User user = response.body();
                    callback.onSuccess(user);
                } else {
                    Log.i("MLogin Service", "un-successfull");
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.i("MLogin Service", "failed");
                Log.i("MLogin Service", t.getMessage());
                callback.onFailure("Login failed: " + t.getMessage());
            }
        });
    }
}
