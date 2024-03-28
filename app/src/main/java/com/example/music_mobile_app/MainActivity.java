package com.example.music_mobile_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.example.music_mobile_app.model.User;
import com.example.music_mobile_app.model.UserImage;
import com.example.music_mobile_app.network.mSpotifyService;
import com.example.music_mobile_app.ui.MainFragment;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends FragmentActivity {

    public static mSpotifyService mSpotifyService;
    public static SpotifyService spotifyService;
    public static String authToken;

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                Log.i("KHOI TAO ADS SDK", "THANH CONG");
            }
        });



        setContentView(R.layout.activity_main);

        FragmentManager manager = getSupportFragmentManager();

        SharedPreferences sharedPreferences = getSharedPreferences("Authentication", Context.MODE_PRIVATE);
        authToken = sharedPreferences.getString("AUTH_TOKEN", "Not found authtoken");
        manager.beginTransaction().replace(R.id.fragment_container, new MainFragment()).commit();

        setServiceAPI();

        getUserProfile();
    }


    private void getUserProfile() {
        mSpotifyService.getUserProfile(new Callback<User>() {
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
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e(TAG, "Failed to get user profile", t);

            }
        });
    }


    private void setServiceAPI() {
        Log.d(TAG, "Setting Spotify API Service");
        SpotifyApi api = new SpotifyApi();
        api.setAccessToken(authToken);
        // Get kaaes spotify service
        spotifyService = api.getService();
        // Get project service
        mSpotifyService = new mSpotifyService(authToken);
    }


    @Override
    protected void onStop() {
        super.onStop();
        // Aaand we will finish off here.
    }
}