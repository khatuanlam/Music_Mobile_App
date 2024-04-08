package com.example.music_mobile_app;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.music_mobile_app.manager.ListManager;

import kaaes.spotify.webapi.android.models.Image;

import com.example.music_mobile_app.manager.VariableManager;
import com.example.music_mobile_app.network.mSpotifyService;
import com.example.music_mobile_app.ui.MainFragment;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyCallback;
import kaaes.spotify.webapi.android.SpotifyError;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.UserPrivate;

public class MainActivity extends FragmentActivity {

    public static mSpotifyService mSpotifyService;
    public static SpotifyService spotifyService;
    public static String authToken;
    private TextView tv_playerField;
    public static final ListManager listManager = ListManager.getInstance();
    public static final VariableManager varManager = VariableManager.getInstance();
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                Log.i("KHOI TAO ADS SDK", "THANH CONG");
            }
        });

        FragmentManager manager = getSupportFragmentManager();
        SharedPreferences sharedPreferences = getSharedPreferences("Authentication", Context.MODE_PRIVATE);
        authToken = sharedPreferences.getString("AUTH_TOKEN", "Not found authtoken");


        setServiceAPI();

        getUserProfile();

        manager.beginTransaction().replace(R.id.fragment_container, new MainFragment()).commit();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    private void getUserProfile() {
        UserPrivate userPrivate = varManager.getUser();
        if (userPrivate.id == null) {
            spotifyService.getMe(new SpotifyCallback<UserPrivate>() {
                @Override
                public void failure(SpotifyError spotifyError) {
                    Log.e(TAG, spotifyError.getMessage());
                }

                @Override
                public void success(UserPrivate userPrivate, retrofit.client.Response response) {
                    UserPrivate userProfile = userPrivate;
                    varManager.setUser(userProfile);
                    if (userProfile != null) {
                        String displayName = userProfile.display_name;
                        String imageUrl = null;
                        List<Image> images = userProfile.images;
                        if (images != null && !images.isEmpty()) {
                            // Choose the first image as default
                            Image largestImage = images.get(0);

                            // Iterate through images to find a larger one
                            for (Image image : images) {
                                if (image.width > largestImage.height && image.height > largestImage.height) {
                                    largestImage = image;
                                }
                            }
                            imageUrl = largestImage.url;
                        }
                        getUserProfile();

                        SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("displayName", displayName);
                        editor.putString("imageUrl", imageUrl);
                        editor.putString("userId", userProfile.id);
                        editor.apply();

                        // Save user data
                        VariableManager.getInstance().setUser(userProfile);
                    }
                }
            });
        }
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