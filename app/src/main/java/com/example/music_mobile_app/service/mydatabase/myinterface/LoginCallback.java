package com.example.music_mobile_app.service.mydatabase.myinterface;

import com.example.music_mobile_app.model.mydatabase.User;

public interface LoginCallback {
    void onSuccess(User user);
    void onFailure(String message);
}