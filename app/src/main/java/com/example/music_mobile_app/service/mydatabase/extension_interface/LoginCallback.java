package com.example.music_mobile_app.service.mydatabase.extension_interface;

import com.example.music_mobile_app.model.extension.User;

public interface LoginCallback {
    void onSuccess(User user);

    void onFailure(String message);
}