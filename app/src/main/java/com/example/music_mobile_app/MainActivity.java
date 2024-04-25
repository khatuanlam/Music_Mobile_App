package com.example.music_mobile_app;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.widget.Toast;

import com.example.music_mobile_app.manager.PlaybackManager;
import com.example.music_mobile_app.model.UserImage;
import com.example.music_mobile_app.network.mSpotifyService;
import com.example.music_mobile_app.receiver.MyCorruptInternetReceiver;
import com.example.music_mobile_app.receiver.MyDownloadReceiver;
import com.example.music_mobile_app.repository.sqlite.MusicDatabaseHelper;
import com.example.music_mobile_app.service.mydatabase.impl.LoginServiceImpl;
import com.example.music_mobile_app.service.mydatabase.myinterface.LoginCallback;
import com.example.music_mobile_app.service.mydatabase.myinterface.LoginService;

import android.widget.TextView;
import android.widget.Toast;

import com.example.music_mobile_app.manager.ListManager;

import kaaes.spotify.webapi.android.models.Image;

import com.example.music_mobile_app.manager.VariableManager;
import com.example.music_mobile_app.network.mSpotifyAPI;
import com.example.music_mobile_app.repository.sqlite.MusicDatabaseHelper;
import com.example.music_mobile_app.service.mydatabase.impl.LoginServiceImpl;
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

    public static mSpotifyAPI mSpotifyAPI;
    public static SpotifyService spotifyService;
    public static String authToken;

    // Login in our server
    public LoginService loginService;
    public static MusicDatabaseHelper musicDatabaseHelper;
    private TextView tv_playerField;
    public static final ListManager listManager = ListManager.getInstance();
    public static final VariableManager varManager = VariableManager.getInstance();
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_CODE_STORAGE = 100;

    public MyCorruptInternetReceiver myCorruptInternetReceiver;
    public MyDownloadReceiver myDownloadReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Kiểm tra trạng thái kết nối mạng
        if (!isNetworkAvailable()) {
            // Hiển thị thông báo khi không có kết nối mạng
            Toast.makeText(this, "Không có kết nối mạng", Toast.LENGTH_SHORT).show();
        }

        loginService = new LoginServiceImpl();
        musicDatabaseHelper = new MusicDatabaseHelper(this);

        FragmentManager manager = getSupportFragmentManager();

        loginService = new LoginServiceImpl();
        musicDatabaseHelper = new MusicDatabaseHelper(this);

        SharedPreferences sharedPreferences = getSharedPreferences("Authentication", Context.MODE_PRIVATE);
        authToken = sharedPreferences.getString("AUTH_TOKEN", "Not found authtoken");

        setServiceAPI();

        getUserProfile();

        createNotificationChannel("firebase's notification", "Firsebase Notification",
                NotificationManager.IMPORTANCE_DEFAULT, "Kenh thong bao cua Firebase");
        createNotificationChannel("download's notification", "Download Notification",
                NotificationManager.IMPORTANCE_DEFAULT, "Kenh thong bao cua Download");

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                Log.i("KHOI TAO ADS SDK", "THANH CONG");
            }
        });

        myCorruptInternetReceiver = new MyCorruptInternetReceiver();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(myCorruptInternetReceiver, intentFilter);

        myDownloadReceiver = new MyDownloadReceiver();
        IntentFilter intentFilter1 = new IntentFilter("com.example.MY_DOWNLOAD_BROADCAST");
        registerReceiver(myDownloadReceiver, intentFilter1);

        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_STORAGE);

        manager.beginTransaction().replace(R.id.fragment_container, new MainFragment()).commit();

        createNotificationChannel("firebase's notification", "Firsebase Notification", NotificationManager.IMPORTANCE_DEFAULT, "FireBase Notification");
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                Log.i("KHOI TAO ADS SDK", "THANH CONG");
            }
        });
        loginService.loginWithMyDatabase("id của account spotify", new LoginCallback() {
            @Override
            public void onSuccess(com.example.music_mobile_app.model.mydatabase.User user) {
                com.example.music_mobile_app.ui.ExtensionFragment.userId = user.id;
            }

            @Override
            public void onFailure(String message) {
                Log.i("MLogin Activity", message);
            }
        });
        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_STORAGE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_STORAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                Log.i(TAG, "Storage permission denied");
            }
        }
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
                        // Update sửa lỗi không hiển thị avatar khi user chưa set avatar
                        else {
                            imageUrl = "android.resource://" + getPackageName() + "/drawable/default_avt";
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
                        Log.i("ID USER", userProfile.id);
                        loginService.loginWithMyDatabase(userProfile.id, new LoginCallback() {
                            @Override
                            public void onSuccess(com.example.music_mobile_app.model.mydatabase.User user) {
                                SharedPreferences sharedPreferences = getSharedPreferences("UserIdInMyDatabase",
                                        Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putLong("userIdMyDb", user.getId());
                                editor.apply();
                            }

                            @Override
                            public void onFailure(String message) {
                                Log.i("MLogin Activity", message);
                            }
                        });
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
        mSpotifyAPI = new mSpotifyAPI(authToken);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // And we will finish off here.
        // Clear cache
        ListManager.getInstance().clear();
//        // Disconnect Spotify
//        PlaybackManager.Disconnect();
    }

    @Override
    protected void onStart() {
        super.onStart();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (!PlaybackManager.checkSpotify(this)) {
            builder.setTitle("Spotify Install")
                    .setMessage("You should install Spotify to get full access!!!")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Xử lý khi nhấn nút OK
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.spotify.music"));
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Xử lý khi nhấn nút Cancel
                        }
                    })
                    .show();
        }

    }

    public void createNotificationChannel(String channelId, String channelName, int importance, String description) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
            channel.setDescription(description);
            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 100}); // Optional
            // vibration
            // pattern

            NotificationManager notificationManager = (NotificationManager) getSystemService(
                    Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }
    }

    // Phương thức để kiểm tra trạng thái kết nối mạng
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Hủy đăng ký BroadcastReceiver ở đây
        unregisterReceiver(myDownloadReceiver);
    }
}