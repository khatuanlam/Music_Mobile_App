package com.example.music_mobile_app;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.music_mobile_app.model.User;
import com.example.music_mobile_app.model.UserImage;
import com.example.music_mobile_app.network.mSpotifyService;
import com.example.music_mobile_app.repository.sqlite.MusicDatabaseHelper;
import com.example.music_mobile_app.service.mydatabase.impl.LoginServiceImpl;
import com.example.music_mobile_app.service.mydatabase.myinterface.LoginCallback;
import com.example.music_mobile_app.service.mydatabase.myinterface.LoginService;
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

    public LoginService loginService;
    public static MusicDatabaseHelper musicDatabaseHelper;
    public static String authToken;

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int REQUEST_CODE_STORAGE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager manager = getSupportFragmentManager();
        loginService = new LoginServiceImpl();
        musicDatabaseHelper = new MusicDatabaseHelper(this);

//        SharedPreferences sharedPreferences = getSharedPreferences("Authentication", Context.MODE_PRIVATE);
//        authToken = sharedPreferences.getString("AUTH_TOKEN", "Not found authtoken");
        manager.beginTransaction().replace(R.id.fragment_container, new MainFragment()).commit();

//        setServiceAPI();

//        getUserProfile();
        createNotificationChannel("firebase's notification","Firsebase Notification", NotificationManager.IMPORTANCE_DEFAULT);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                Log.i("KHOI TAO ADS SDK","THANH CONG");
            }
        });


        loginService.loginWithMyDatabase("id của account spotify", new LoginCallback() {
            @Override
            public void onSuccess(com.example.music_mobile_app.model.mydatabase.User user) {
                com.example.music_mobile_app.ui.mydatabase.MainFragment.userId = user.getId();
            }

            @Override
            public void onFailure(String message) {
                Log.i("MLogin Activity", message);
            }
        });
        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_STORAGE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                Log.i(TAG, "Storage permission denied");
            }
        }
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
    public void createNotificationChannel(String channelId, String channelName, int importance) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
            // Configure the channel (optional)
            channel.setDescription("Kenh thong bao cua Firebase");
//            channel.setLightColor(Color.GREEN); // Optional LED light color
            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 100}); // Optional vibration pattern

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }
    }

}