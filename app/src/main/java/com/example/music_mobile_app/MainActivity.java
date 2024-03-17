package com.example.music_mobile_app;

import static com.example.music_mobile_app.ui.HomeFragment.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;


import com.example.music_mobile_app.backend.SpotifyService;
import com.example.music_mobile_app.backend.model.User;
import com.example.music_mobile_app.backend.model.UserImage;
import com.example.music_mobile_app.ui.ArtistFragment;
import com.example.music_mobile_app.ui.HomeFragment;
import com.example.music_mobile_app.ui.MainFragment;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Callback;
import retrofit.RetrofitError;
import retrofit2.Call;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    private String authToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Hide action bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();

        setContentView(R.layout.activity_main);
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.fragment_container, new MainFragment()).commit();

        authToken = getIntent().getStringExtra(LoginActivity.AUTH_TOKEN);
        System.out.println(authToken);
        getUserProfile();
    }

    private void getUserProfile() {
        SpotifyService spotifyService = new SpotifyService();
        spotifyService.getUserProfile(authToken, new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User userProfile = response.body();
                    if (userProfile != null) {
                        String displayName = userProfile.getDisplayName();
                        String imageUrl = null;
                        List<UserImage> images = userProfile.getImages();
                        if (images != null && !images.isEmpty()) {
                            // Choose the first image as default
                            UserImage largestImage = images.get(0);

                            // Iterate through images to find a larger one
                            for (UserImage image : images) {
                                if (image.getWidth() > largestImage.getWidth() && image.getHeight() > largestImage.getHeight()) {
                                    largestImage = image;
                                }
                            }
                            imageUrl = largestImage.getUrl();
                        }

                        SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("displayName", displayName);
                        editor.putString("imageUrl", imageUrl);
                        editor.apply();

                    }
                } else {
                    Log.e(TAG, "Failed to get user profile: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e(TAG, "Failed to get user profile", t);
            }
        });
    }

}