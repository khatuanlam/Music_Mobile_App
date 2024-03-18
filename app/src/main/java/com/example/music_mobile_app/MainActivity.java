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


//import kaaes.spotify.webapi.android.SpotifyService;
import com.example.music_mobile_app.model.User;
import com.example.music_mobile_app.model.UserImage;
import com.example.music_mobile_app.ui.MainFragment;
import com.example.music_mobile_app.network.SpotifyService;

import java.util.List;
import java.util.Objects;
import retrofit2.Callback;
import retrofit2.Call;
import retrofit2.Response;





public class MainActivity extends AppCompatActivity {

    public static SpotifyService spotifyService;

    public static String authToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Hide action bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();

        setContentView(R.layout.activity_main);
        FragmentManager manager = getSupportFragmentManager();
        authToken = getIntent().getStringExtra(LoginActivity.AUTH_TOKEN);

//        Bundle bundle = new Bundle();
//        bundle.putString(LoginActivity.AUTH_TOKEN, authToken);
//        Fragment fragment = new MainFragment();
//        fragment.setArguments(bundle);
        manager.beginTransaction().replace(R.id.fragment_container, new MainFragment()).commit();
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